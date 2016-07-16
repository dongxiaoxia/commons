package xyz.dongxiaoxia.commons.persistence.uya.jdbc.basedao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbconnectionpool.ConnectionPool;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbconnectionpool.ConnectionPoolFactory;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbconnectionpool.DBConfig;
import xyz.dongxiaoxia.commons.utils.config.PropertiesLoader;

import java.io.File;
import java.sql.Connection;

/**
 * @author dongxiaoxia
 * @create 2016-07-11 14:35
 */
public class ConnectionHelper {

    private static final Logger log = LoggerFactory.getLogger(ConnectionHelper.class);

    private ConnectionPool connPool;

    private final ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

    public ConnectionHelper(String configPath) throws Exception {
        log.info("creating DAOHelper configPath:" + configPath);
        PropertiesLoader propertiesLoader = new PropertiesLoader(configPath);
        log.info("init ConnectionPool...");

        //提供数据库切换功能
        String swapDataSource = propertiesLoader.getProperty("swapDataSource");
        swapDataSource = swapDataSource.equals("") ? null : swapDataSource;// TODO: 2016/7/12
        if (swapDataSource != null) {
            connPool = getDataSource(configPath, swapDataSource);
            if (connPool == null) {
                throw new Exception("conn pool is null:" + configPath);
            }
        } else {
            connPool = createConnPool(propertiesLoader);
        }
        log.info("init ConnectionPool success connection count:" + connPool.GetAllCount());
        if (connPool.GetAllCount() == 0) {
            log.warn("success create 0 connection,please check config!!!");
        }
    }

    public void lockConn(Connection conn) {
        threadLocal.set(conn);
    }

    public void unLockConn() {
        threadLocal.set(null);
    }

    public ConnectionPool getConnPool() {
        return connPool;
    }

    /**
     * 创建连接池
     *
     * @param propertiesLoader
     * @return
     */
    private ConnectionPool createConnPool(PropertiesLoader propertiesLoader) {
        log.debug("ConnectionPool ConnectionURL:" + propertiesLoader.getProperty("ConnectionURL"));
        log.debug("ConnectionPool DriversClass:" + propertiesLoader.getProperty("DriversClass"));
        log.debug("ConnectionPool UserName:***");
        log.debug("ConnectionPool PassWord:***");
        log.debug("ConnectionPool MinPoolSize:" + propertiesLoader.getProperty("MinPoolSize"));
        log.debug("ConnectionPool MaxPoolSize:" + propertiesLoader.getProperty("MaxPoolSize"));
        log.debug("ConnectionPool IdleTimeout:" + propertiesLoader.getProperty("IdleTimeout"));
        log.debug("ConnectionPool AutoShrink:" + propertiesLoader.getProperty("AutoShrink"));

        DBConfig dbConfig = new DBConfig();
        dbConfig.setConnectionUrl(propertiesLoader.getProperty("ConnectionURL"));
        dbConfig.setDriversClass(propertiesLoader.getProperty("DriversClass"));
        dbConfig.setUsername(propertiesLoader.getProperty("UserName"));
        dbConfig.setPassword(propertiesLoader.getProperty("PassWord"));
        dbConfig.setMinPoolSize(propertiesLoader.getInterger("MinPoolSize"));
        dbConfig.setMaxPoolSize(propertiesLoader.getInterger("MaxPoolSize"));
        dbConfig.setIdleTimeOut(propertiesLoader.getInterger("IdleTimeout"));
        dbConfig.setAutoShrink(propertiesLoader.getBoolean("AutoShrink"));
        return new ConnectionPool(dbConfig);
    }

    /**
     * 根据Utility的配置文件和数据库切换配置来获得ConnectionPool
     *
     * @param configPath     Utility的配置文件
     * @param swapDataSource 数据库切换配置, 和Utility的配置文件在同一目录下
     * @return
     * @throws Exception
     */
    private ConnectionPool getDataSource(String configPath, String swapDataSource) throws Exception {
        String realPath = new File(configPath).getParent() + "/" + swapDataSource;
        return ConnectionPoolFactory.createPool(realPath);
    }

    /**
     * 获取链接
     *
     * @return
     * @throws Exception
     */
    public Connection get() throws Exception {
        Connection conn = threadLocal.get();
        if (conn == null) {
            conn = connPool.Get();
        }
        return conn;
    }

    /**
     * 获得可能只读的数据库连接
     *
     * @return
     * @throws Exception
     */
    public Connection getReadConnection() throws Exception {
        Connection conn = threadLocal.get();
        if (conn == null) {
            conn = connPool.GetReadConnection();
        }
        return conn;
    }

    /**
     * 释放连接
     *
     * @param conn
     */
    public void release(Connection conn) {
        Connection tconn = threadLocal.get();
        if (tconn == null || (tconn != null && (tconn.hashCode() != conn.hashCode()))) {
            log.debug("this conn is release " + conn);
            connPool.Release(conn);
        }
    }
}
