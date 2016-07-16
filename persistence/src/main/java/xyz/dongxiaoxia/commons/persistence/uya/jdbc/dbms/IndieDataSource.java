package xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbms;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbms.config.ClusterConfig;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbms.config.DbConfig;

import java.sql.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 与{@link ClusterDataSource}不同的是 IndieDataSource提供的是独立的读写数据库.
 * 当发现数据库访问异常后会将自动向数据库发送指令使 master / slave 进行对调.并切换 当前使用的数据库连接. 只适用 与 sql server
 * 数据库.
 * 
 * @author wangwt
 */
public class IndieDataSource extends AbstractDataSource {
	private static Logger log = LoggerFactory.getLogger(IndieDataSource.class);
	private static final int DB_CONNECTION_FAILD = 8001;
	private TransferQueue<DbConfig> dbconfigs;
	private volatile DbDataSource currentDataSource;
	private static int configCount;
	private ConcurrentHashMap<String, DbConfig> managerDataSources;
	private static final String CHECK_DB_STATE_SQL = "{call P_GetMSState(?)}";
	private static final String CHANGE_MASTER_DB_SQL = "{call P_WakeUP(?)}";

	// private static ConcurrentHashMap<String, DbMonitor> monitorMap = new
	// ConcurrentHashMap<String, DbMonitor>();

	// P_Resume @dbName

	public IndieDataSource(ClusterConfig clusterConfig) throws SQLException {
		log.debug("开始初始化线程池");
		dbconfigs = new LinkedTransferQueue<DbConfig>();
		managerDataSources = new ConcurrentHashMap<String, DbConfig>();
		configCount = clusterConfig.getDbConfigList().size();
		for (DbConfig dbconfig : clusterConfig.getDbConfigList()) {
			if (!dbconfig.getDriversClass().startsWith("com.microsoft.sqlserver")) {
				throw new SQLException("当前数据库连接池仅仅支持 SQL Server 数据库");
			}
			if (dbconfig.getManagerDbConfig() != null) {
				managerDataSources.put(dbconfig.getManagerDbConfig().getConnectionURL(), dbconfig.getManagerDbConfig());
			}
			// monitorMap.put(dbconfig.getConnetionURL(), new DbStateMonitor());
			dbconfigs.add(dbconfig);
		}
		try {
			currentDataSource = createDataSource();
			// currentDataSource.addMonitor(monitorMap.get(currentDataSource.getName()));
		} catch (Exception e) {
			throw new SQLException("指定的" + clusterConfig.getName() + "配置文件集合异常:" + e.getMessage());
		}
	}

    @Override
    public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return this.getParentLogger();
    }

    private class DbState {
		/**
		 * 0 = 已挂起 主体数据库正在运行，但没有向镜像服务器发送任何日志。数据库的镜像副本不可用。
		 * 
		 * 1 = 已断开 主体服务器实例无法连接到其伙伴实例或见证服务器实例（如果存在）
		 * 
		 * 2 = 正在同步
		 * 镜像数据库的内容滞后于主体数据库的内容。主体服务器实例正在向镜像服务器实例发送日志记录，这会对镜像数据库应用更改，使其前滚。
		 * 在数据库镜像会话开始时，镜像数据库和主体数据库处于同步状态。
		 * 
		 * 3 = 挂起故障转移 在主体服务器实例上，手动故障转移（角色切换）已经开始，但镜像服务器实例尚未接受。
		 * 
		 * 4 = 已同步 镜像数据库包含的数据与主体数据库相同。只有在同步状态下，才能进行手动和自动故障转移。
		 * 
		 * NULL(-1) = 数据库没有联机。不存在数据库镜像会话，并且没有要在“镜像”页上报告的活动。
		 */
		private int mirroringState = -1;
		/**
		 * 1 = 主体数据库 2 = 镜像数据库 NULL(-1) = 数据库没有联机。
		 */
		private int mirroringRole = -1;

		public DbState(int mirroring_state, int mirroring_role) {
			this.mirroringState = mirroring_state;
			this.mirroringRole = mirroring_role;
		}

		public boolean isMasterDb() {
			if (mirroringRole == 1) {
				return true;
			} else {
				return false;
			}
		}

		public boolean canChange() {
			if (mirroringState == 1) {
				return true;
			} else {
				return false;
			}
		}

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return String.format("mirroringRole:%s mirroringState%s", mirroringRole, mirroringState);
		}

	}

	/**
	 * @return 检查数据库状态
	 * @throws SQLException
	 */
	private DbState checkDbState(Connection conn, String dbname) throws SQLException {
		CallableStatement cs = null;
		try {
			cs = conn.prepareCall(CHECK_DB_STATE_SQL);
			cs.setString(1, dbname);
			ResultSet rs = cs.executeQuery();
			if (rs.next()) {
				return new DbState(rs.getInt(1), rs.getInt(2));
			} else {
				return new DbState(-1, -1);
			}
		} catch (SQLException e) {
			throw new SQLException(e);
		}
	}

	private String getDbName(DbConfig config) {
		String[] vs = config.getConnectionURL().split("DatabaseName=");
		return vs[1];
	}

	/**
	 * 执行命令进行主从切换
	 */
	private boolean changeMasterDb(Connection conn, String dbname) {
		CallableStatement cs = null;
		try {
			cs = conn.prepareCall(CHANGE_MASTER_DB_SQL);
			cs.setString(1, dbname);
			cs.execute();
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	private boolean isSameDbHost(DbConfig config1, DbConfig config2) {
		String host1 = config1.getConnectionURL().split("DatabaseName=")[0];
		String host2 = config2.getConnectionURL().split("DatabaseName=")[0];
		if (host1.equals(host2)) {
			return true;
		} else {
			return false;
		}
	}

	protected DbDataSource createDataSource() throws SQLException {
		DbDataSource datasource = null;
		int i = 0;
		while (datasource == null) {
			DbConfig mconfig = dbconfigs.poll();
			if (mconfig != null) {
				if (datasource == null) {
					datasource = createDataSource(mconfig);
				}
			}
			dbconfigs.offer(mconfig);
			i++;
			if (i >= configCount) {
				break;
			}
		}
		if (datasource == null) {
			throw new SQLException("未发现有效的数据库连接.", null, DB_CONNECTION_FAILD);
		}
		return datasource;
	}

	private DbDataSource createDataSource(DbConfig dbconfig) {
		long start = System.currentTimeMillis();
		// DbDataSource dbds = new DbDataSource(dbconfig, monitor);
		DbDataSource dbds = new DbDataSource(dbconfig);
		if (dbds.isAlive()) {
			dbds.setName(dbconfig.getConnectionURL());
			// dbds.removeMonitor(monitor);
			log.info("创建连接成功" + dbconfig.getConnectionURL() + "耗时:" + (System.currentTimeMillis() - start));
			return dbds;
		} else {
			log.info("创建连接失败" + dbconfig.getConnectionURL() + "耗时:" + (System.currentTimeMillis() - start));
			return null;
		}
	}

	@Override
	public Connection GetReadConnection() throws SQLException {
		// TODO Auto-generated method stub
		try {
			Connection con = getDataSource().getConnection();
			return con;
		} catch (SQLException e) {
			if (e.getSQLState().equals("08S01")) {
				currentDataSource.setAlive(false);
			}
			throw new SQLException(e);
		}
	}

	private void reConnection() throws SQLException {
		DbDataSource newDataSource = null;
		// 重试所有的配置文件,并且进行连接,直到找到一个可用的连接
		// 如果所有的连接均不可用则抛出异常
		log.debug("开始创建新的数据库连接");
		newDataSource = createDataSource();
		// 二次检查
		if (currentDataSource == null || !currentDataSource.isAlive()) {
			DbConfig mcfg = managerDataSources.get(newDataSource.getConfig().getManagerDbConfig().getConnectionURL());
			DbDataSource mds = new DbDataSource(mcfg);
			String dbname = getDbName(newDataSource.getConfig());
			DbState dstate = checkDbState(mds.getConnection(), dbname);
			mds.shutdown();
			// 防止切库指令失效 主库 和从库都能够访问, 此时主库down 写入从库 从而造成双写的可能性
			if (dstate.isMasterDb()) {
				// 移除原来的连接池的监控
				if (!newDataSource.getName().equals(currentDataSource.getName())) {
					currentDataSource.shutdown();
				}
				// newDataSource.addMonitor(monitorMap.get(newDataSource.getName()));
				currentDataSource = newDataSource;
				currentDataSource.setAlive(true);
			} else {
				throw new SQLException("检测当前主从关系异常,当前存活的为Slave数据库, Master数据库无法连接.", null, 1455);
			}
		}
	}

	private void changeMasterDb() throws SQLException {
		log.info("创建数据库连接失败，尝试切库");
		for (String key : managerDataSources.keySet()) {
			DbConfig mcfg = managerDataSources.get(key);
			if (!isSameDbHost(currentDataSource.getConfig(), mcfg)) {
				DbDataSource mds = null;
				try {
					log.debug("开始创建DBManager连接:" + mcfg.getConnectionURL());
					mds = new DbDataSource(mcfg);
					log.debug("创建DBManager结果:" + mds.isAlive());
					String dbname = getDbName(currentDataSource.getConfig());
					DbState dstate = checkDbState(mds.getConnection(), dbname);
					log.info("检查数据库: " + mds.getName() + "返回状态--" + dstate.toString());
					if (!dstate.isMasterDb() && dstate.canChange()) {
						if (currentDataSource != null && currentDataSource.isAlive()) {
							log.info("发现数据库可用了，无需切换了。");
						} else {
							boolean state = changeMasterDb(mds.getConnection(), dbname);
							log.info("执行了写切库存储过程,执行状态:" + (state ? "成功" : "失败"));
							if (state) {
								log.info("财务数据库[" + dbname + "]切库存储过程执行成功,请重新建立主从关系.");
							}
						}
					}
				} finally {
					mds.shutdown();
				}
			}
		}
	}

	AtomicBoolean locked = new AtomicBoolean(false);

	public DbDataSource getDataSource() throws SQLException {
		log.debug("当前数据库连接：" + currentDataSource.toString());
		if (currentDataSource != null && currentDataSource.isAlive()) {
			return currentDataSource;
		}

		if (locked.get()) {
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
			}
			if (currentDataSource != null && currentDataSource.isAlive()) {
				return currentDataSource;
			}
			throw new SQLException("数据库异常,正在检测中,当前无可用连接", "08S01");
		}

		synchronized (this) {
			log.debug("检测前加锁 ,开始检测可用数据库连接");
			if (currentDataSource != null && currentDataSource.isAlive()) {
				return currentDataSource;
			}
			locked.set(true);
			try {
				reConnection();
				return currentDataSource;
			} catch (SQLException e) {
				// 所有的连接均不可用, 例如: master 正处在异常中, 但是slave还没有启动
				// 又或者 本机的网络异常,master 和 slave都不能访问. 此处必须保证 多个pmc服务不能重复的发送指令

				if (e.getErrorCode() != DB_CONNECTION_FAILD)
					throw new SQLException("无可用连接", e.getSQLState() + "_" + e.getErrorCode(), e);
				try {
					changeMasterDb();
					reConnection();
					return currentDataSource;
				} catch (SQLException e1) {
					throw new SQLException("切库后暂无可用连接", e1.getSQLState() + "_" + e1.getErrorCode(), e1);
				} finally {
					locked.set(false);
				}
			} finally {
				locked.set(false);
			}
		}

	}

	public Connection getConnection() throws SQLException {
		return this.getDataSource().getConnection();
	}

}
