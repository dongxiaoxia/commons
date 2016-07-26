package xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbconnectionpool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 数据库连接池
 *
 * @author dongxiaoxia
 * @create 2016-07-11 15:15
 */
public class ConnectionPool {
    private static Logger log = LoggerFactory.getLogger(ConnectionPool.class);
    private Stack<Connection> availableConn = new Stack<>(); //Connections stack
    private DBConfig dbConfig; //Pool's configuration.
    private List<Connection> Pool = new ArrayList<>(); //Here store all connections.
    private PoolState state = new PoolState();

    public ConnectionPool() {

    }

    public ConnectionPool(DBConfig config) throws SQLException {
        try {
            dbConfig = config;
            loadDrivers();
            init();
            registerExitEven();
        } catch (Exception e) {
            throw new SQLException(e.getMessage(),e);
        }
    }

    /**
     * 获取所有的连接数量
     *
     * @return 所有的连接数量
     */
    public int getAllCount() {
        return Pool.size();
    }

    /**
     * 获取可用的连接数量
     *
     * @return 可用的连接数量
     */
    public int getFreeConnCount() {
        return availableConn.size();
    }

    /**
     * 获取数据库连接
     *
     * @return 数据库连接
     * @throws Exception
     */
    public synchronized Connection get() throws SQLException {
        int freeCount = getFreeConnCount();
        state.setNoWorkCount(freeCount);
        if (freeCount > 0) {
            Connection conn = availableConn.pop();
            if (conn != null) {
                Pool.remove(conn);
            }
            log.debug("Connection get " + conn + " connection size is " + getAllCount() + " FreeConnCount is " + getFreeConnCount());
            return conn;
        } else if (getAllCount() < dbConfig.getMaxPoolSize()) {
            Connection conn = createConn();
            log.debug(" Connection get " + conn + " connection size is " + getAllCount() + " FreeConnCount is " + getFreeConnCount());
            return conn;
        } else {
            throw new SQLException("Database Connection Pool is full");
        }
    }

    /**
     * 2011-05-24 获得一个只需要读的数据库连接,处理主库压力大的情况下，降低主库压力
     *
     * @return 一个只供读的数据库连接，可能是主库，也有可能是从库
     * @throws Exception
     */
    public Connection GetReadConnection() throws Exception {
        return get(); //TODO 获取只读数据库连接
    }

    /**
     * 释放数据库连接
     *
     * @param conn 连接
     */
    public synchronized void release(Connection conn) {
        if (conn != null) {
            try {
                if (dbConfig.getAutoShrink() //is allow auto shrink pool
                        && state.getNoWorkCount(dbConfig.getIdleTimeOut() * 1000) > 0//check no work connections count
                        ) {
                    destroy(conn);
                    state.setNoWorkCount(getFreeConnCount());
                } else {
                    if (!conn.isClosed()) {
                        availableConn.push(conn);
                    } else {
                        destroy(conn);
                    }
                }
            } catch (SQLException e) {
                log.error(e.getMessage(), e);
            }
        }
        log.debug("release method connection size is " + getAllCount() + " FreeConnCount is " + getFreeConnCount());
    }


    /**
     * 销毁数据库连接
     *
     * @param conn 连接
     * @throws SQLException
     */
    private synchronized void destroy(Connection conn) throws SQLException {
        if (conn != null) {
            try {
                conn.close();
                Pool.remove(conn); //remove this connection from pool
                log.debug(" close one connection!!!" + conn + " connection size is " + getAllCount() + " FreeConnCount is " + getFreeConnCount());
            } finally {
                conn = null;
            }
        }
    }

    /**
     * 创建数据库连接
     *
     * @return 数据库连接
     * @throws Exception
     */
    private synchronized Connection createConn() throws SQLException {
        Connection conn = null;
        try {
            if (dbConfig.getUsername() == null) {
                conn = DriverManager.getConnection(dbConfig.getUrl());
            } else {
                conn = DriverManager.getConnection(dbConfig.getUrl(), dbConfig.getUsername(), dbConfig.getPassword());
            }
        } finally {
            if (conn != null && !conn.isClosed()) {
                log.debug(" this conn is create " + conn + " connection size is " + getAllCount() + " FreeConnCount is " + getFreeConnCount());
                Pool.add(conn);
            }
        }
        return conn;
    }

    /**
     * 加载JDBC驱动类
     */
    private void loadDrivers() throws SQLException {
        try {
            Driver driver = (Driver) Class.forName(dbConfig.getDriver()).newInstance();
            DriverManager.registerDriver(driver);
            DriverManager.setLoginTimeout(1);
        } catch (Exception e) {
            throw new SQLException(e.getMessage() + " 加载JDBC驱动失败", e);
        }
    }

    /**
     * 初始化数据库连接池
     *
     * @throws Exception
     */
    private void init() throws Exception {
        for (int i = 0; i < dbConfig.getMinPoolSize(); i++) {
            Connection conn = createConn();
            if (conn != null) {
                availableConn.push(conn);
            }
        }
    }

    /*
     * when application exiting destroy all connections
     */
    private void registerExitEven() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("application exiting,begin remove all connections.");
                for (Connection conn : Pool) {
                    try {
                        if (conn != null && !conn.isClosed()) {
                            conn.close();
                        }
                    } catch (SQLException e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
        });
    }


    /**
     * 数据库连接池状态
     */
    public class PoolState {
        private long duration;
        private int noWorkCount = 0;

        public synchronized void setNoWorkCount(int noWorkCout) {
            if (this.noWorkCount <= 2 && noWorkCout > 2) {
                duration = System.currentTimeMillis();
            }
            this.noWorkCount = noWorkCout;
        }

        public synchronized int getNoWorkCount(long duration) {
            if ((System.currentTimeMillis() - this.duration > duration)) {
                return (noWorkCount - 2);
            }
            return 0;
        }
    }
}
