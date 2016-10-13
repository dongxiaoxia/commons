package xyz.dongxiaoxia.commons.persistence.uya.jdbc.basedao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbconnectionpool.ConnectionPool;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbconnectionpool.ConnectionPoolFactory;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbconnectionpool.DBConfig;
import xyz.dongxiaoxia.commons.utils.PropertiesLoader;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据库连接帮助类
 *
 * @author dongxiaoxia
 * @create 2016-07-11 14:35
 */
public class ConnectionHelper {

    private static final Logger log = LoggerFactory.getLogger(ConnectionHelper.class);

    private ConnectionPool connectionPool;

    private final ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

    public ConnectionHelper(URL url) throws Exception {
        String configPath = url.getPath();
        log.info("creating ConnectionHelper configPath:" + configPath);
        PropertiesLoader propertiesLoader = new PropertiesLoader(url.openStream());
        log.info("init ConnectionPool...");

        //提供数据库切换功能
        String swapDataSource = propertiesLoader.getProperty("swapDataSource");
        swapDataSource = swapDataSource.equals("") ? null : swapDataSource;// TODO: 2016/7/12
        if (swapDataSource != null) {
            connectionPool = getDataSource(configPath, swapDataSource);
            if (connectionPool == null) {
                throw new Exception("conn pool is null:" + configPath);
            }
        } else {
            connectionPool = createConnPool(propertiesLoader);
        }
        log.info("init ConnectionPool success connection count:" + connectionPool.getAllCount());
        if (connectionPool.getAllCount() == 0) {
            log.warn("success create 0 connection,please check config!!!");
        }
    }

    public void lockConn(Connection conn) {
        threadLocal.set(conn);
    }

    public void unLockConn() {
        threadLocal.set(null);
    }

    public ConnectionPool getConnectionPool() {
        return connectionPool;
    }

    /**
     * 创建连接池
     *
     * @param loader
     * @return
     */
    private ConnectionPool createConnPool(PropertiesLoader loader) throws SQLException {
        String url = loader.getProperty("url");
        String driver = loader.getProperty("driver");
        String username = loader.getProperty("username");
        String password = loader.getProperty("password");
        int minPoolSize = loader.getInterger("minPoolSize");
        int maxPoolSize = loader.getInterger("maxPoolSize");
        int idleTimeOut = loader.getInterger("idleTimeOut");
        boolean autoShrink = loader.getBoolean("autoShrink");

        log.debug("ConnectionPool URL:" + url);
        log.debug("ConnectionPool Driver:" + driver);
        log.debug("ConnectionPool UserName:***");
        log.debug("ConnectionPool PassWord:***");
        log.debug("ConnectionPool MinPoolSize:" + minPoolSize);
        log.debug("ConnectionPool MaxPoolSize:" + maxPoolSize);
        log.debug("ConnectionPool IdleTimeout:" + idleTimeOut);
        log.debug("ConnectionPool AutoShrink:" + autoShrink);

        DBConfig dbConfig = new DBConfig();
        dbConfig.setUrl(url);
        dbConfig.setDriver(driver);
        dbConfig.setUsername(username);
        dbConfig.setPassword(password);
        dbConfig.setMinPoolSize(minPoolSize);
        dbConfig.setMaxPoolSize(maxPoolSize);
        dbConfig.setIdleTimeOut(idleTimeOut);
        dbConfig.setAutoShrink(autoShrink);
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
            conn = connectionPool.get();
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
            conn = connectionPool.GetReadConnection();
        }
        return conn;
    }

    /**
     * 释放连接
     *
     * @param conn 数据库连接
     */
    public void release(Connection conn) {
        Connection connection = threadLocal.get();
        if (connection == null || connection.hashCode() != conn.hashCode()) {
            connectionPool.release(conn);
            log.debug("this conn is release " + conn);
        }
    }
}
