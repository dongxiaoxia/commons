package xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbhelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbconnectionpool.Config;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbconnectionpool.Config.Server;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbconnectionpool.ConnectionPool;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbconnectionpool.DBConfig;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbconnectionpool.SaveNode;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 * Created in 2015年1月13日 下午3:44:13 by Tang Boming<br>
 */
public class DistributedDBHelper {
	private static Logger log = LoggerFactory.getLogger(DistributedDBHelper.class);
	protected int DBCount = 1;
	protected int dbCount4Compute = 0;
	protected int tableCount4Compute = 0;
	private final HashMap<Integer, ConnectionPool> connectedPool = new HashMap<Integer, ConnectionPool>();
	private final HashMap<Integer, ConnectionPool> connectedPoolBack = new HashMap<Integer, ConnectionPool>();

	public DistributedDBHelper(int tableCount, String configPath) throws Exception {
		log.info("dbconfig path:" + configPath);
		this.tableCount4Compute = getTableComputeCount(tableCount);
		createConnectionPool(configPath);
		this.DBCount = getConnectedpool().size();
		this.dbCount4Compute = DBCount - 1;
	}

	/**
	 * MOVE_BIT是以2为底数，每个库的的表总数的对数
	 * 
	 * @param tableCount
	 * @return
	 */
	final protected int getMoveBit(int tableCount) {
		return (int) (Math.log(tableCount) / Math.log(2));
	}

	final protected int getTableComputeCount(int tableCount) {
		return tableCount - 1;
	}

	final public HashMap<Integer, ConnectionPool> getConnectedpool() {
		return connectedPool;
	}

	final public HashMap<Integer, ConnectionPool> getConnectedpoolback() {
		return connectedPoolBack;
	}

	final public int getTableCount4Compute() {
		return tableCount4Compute;
	}

	final public synchronized void releaseSlaver(Integer dbid, Connection conn) {
		if (conn != null)
			getConnectedpoolback().get(dbid).Release(conn);
	}

	public static interface ConnHandler {
		public void releaseConn(Connection conn);
	}

	final private void createConnectionPool(String configPath) throws Exception {
		List<Server> serverList = Config.getConfig(configPath);
		System.out.println(serverList.size());
		for (int i = 0; i < serverList.size(); i++) {
			Server s = serverList.get(i);
			String masterAddress = "jdbc:mysql://" + s.getMasterAddress();
			String slaverAddress = "jdbc:mysql://" + s.getSlaverAddress();
			String driverClass = s.getDriverClass();
			String userName = s.getUserName();
			String password = s.getPassWord();
			int minPoolSize = s.getMinPoolSize();
			int maxPoolSize = s.getMaxPoolSize();
			int idleTimeout = s.getIdleTimeout();
			boolean isAutoShrink = s.isAutoShrink();
			if (s.isMasterForbid()) {
				getConnectedpool().put(i, null);
			} else {
				System.out.println(masterAddress);

				DBConfig config = createDBConfig(masterAddress, driverClass, userName, password, minPoolSize, maxPoolSize, idleTimeout, isAutoShrink);
				ConnectionPool pool = new ConnectionPool(config);
				System.out.println("jdbc:mysql://" + s.getMasterAddress() + ": " + pool.GetAllCount());
				getConnectedpool().put(i, pool);
			}

			if (s.isSlaverForbid()) {
				getConnectedpoolback().put(i, null);
			} else {
				System.out.println(slaverAddress);
				DBConfig config = createDBConfig(slaverAddress, driverClass, userName, password, minPoolSize, maxPoolSize, idleTimeout, isAutoShrink);
				ConnectionPool pool = new ConnectionPool(config);
				System.out.println(slaverAddress + " : " + pool.GetAllCount());
				getConnectedpoolback().put(i, pool);
			}

			/*
			 * for (int j = s.getMinRange(); j <= s.getMaxRange(); j++) {
			 * cpdbsRela.put(j, i); }
			 */
		}
	}

	final public DBConfig createDBConfig(String connetionURL, String driversClass, String userName, String passWord, int minPoolSize, int maxPoolSize, int idleTimeout, Boolean autoShrink) {

		DBConfig config = new DBConfig();
		config.setConnectionUrl(connetionURL);
		config.setDriversClass(driversClass);
		config.setUsername(userName);
		config.setPassword(passWord);
		config.setMinPoolSize(minPoolSize);
		config.setMaxPoolSize(maxPoolSize);
		config.setIdleTimeOut(idleTimeout);
		config.setAutoShrink(autoShrink);
		return config;
	}

	public static enum DBType {
		Master, Slaver
	}

	final public boolean checkDB(int dbIndex, DBType dbType) {
		if (dbType == DBType.Master) {
			if (getConnectedpool().get(dbIndex) != null) {
				return true;
			}
		} else {
			if (getConnectedpoolback().get(dbIndex) != null) {
				return true;
			}
		}
		return false;
	}

	final protected synchronized Connection getConn(int dbid) throws Exception {
		if (getConnectedpool() != null) {
			return getConnectedpool().get(dbid).Get();
		}
		return null;
	}

	final public synchronized void releaseMaster(Integer dbid, Connection conn) {
		if (conn != null)
			getConnectedpool().get(dbid).Release(conn);
	}

	final public synchronized Connection getConnBack(int dbid) throws Exception {
		if (getConnectedpoolback() != null) {
			return getConnectedpoolback().get(dbid).Get();
		}
		return null;
	}

	final protected String getCatalog(Connection conn) throws SQLException {
		String catalog = null;
		try {
			catalog = conn.getCatalog();
		} catch (SQLException e) {
			log.error("获取数据库名称失败：", e);
			throw e;
		}
		return catalog;
	}

	/**
	 * 
	 * @param dbIndex
	 *            确定位于那一个库
	 * @param tableIndex
	 *            确定位于那一个表
	 * @return
	 */
	protected SaveNode getSaveNode(int dbIndex, int tableIndex) {
		int dbIndex1 = dbIndex & dbCount4Compute;
		int tableIndex1 = tableIndex & tableCount4Compute;
		return new SaveNode(dbIndex1, tableIndex1);
	}
}