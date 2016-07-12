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
 * @author dongxiaoxia
 * @create 2016-07-11 15:15
 */
public class ConnectionPool {
    private static final Logger log = LoggerFactory.getLogger(ConnectionPool.class);
    private Stack<Connection> AvailableConn = new Stack<>();//Connection stack
    private List<Connection> Pool = new ArrayList<>();//Here store all connections.
    private DBConfig dbconfig;//Pool's configuration.
    private PoolState state = new PoolState();

    public ConnectionPool(){

    }
    public ConnectionPool(DBConfig config) {
        try {
            dbconfig = config;
            loadDrivers();
            init();
            registerExcetEven();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * when application exiting destroy all connections
     */
    private void registerExcetEven() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                log.info("application exiting,begin remove all connections.");
                for (Connection connection : Pool) {
                    try {
                        if (connection != null && connection.isClosed()) {
                            connection.close();
                        }
                    } catch (SQLException e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
        });
    }

    private void init() throws Exception {
        for (int i = 0; i < dbconfig.getMinPoolSize(); i++) {
            Connection connection = createConn();
            if (connection != null) {
                AvailableConn.push(connection);
            }
        }
    }

    private void loadDrivers() {
        try {
            Driver driver = (Driver) Class.forName(dbconfig.getDriversClass()).newInstance();
            DriverManager.registerDriver(driver);
            DriverManager.setLoginTimeout(1);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public synchronized Connection Get() throws Exception {
        int freeCount = AvailableConn.size();
        state.setNoWorkCount(freeCount);
        if (freeCount > 0) {
            Connection conn = AvailableConn.pop();
            if (conn == null) {
                Pool.remove(conn);
            }
            log.debug("Connection get " + conn + "connection size is " + Pool.size() + " FreeConnCOunt is " + AvailableConn.size());
            return conn;
        } else if (Pool.size() < dbconfig.getMaxPoolSize()) {
            Connection conn = createConn();
            log.debug("Connection get " + conn + "connection size is " + Pool.size() + " FreeConnCOunt is " + AvailableConn.size());
            return conn;
        } else {
            throw new Exception("db connection pool is full");
        }
    }

    private synchronized Connection createConn() throws Exception {
        Connection connection = null;
        try {
            if (dbconfig.getUsername() == null) {
                connection = DriverManager.getConnection(dbconfig.getConnectionUrl());
            } else {
                connection = DriverManager.getConnection(dbconfig.getConnectionUrl(), dbconfig.getUsername(), dbconfig.getPassword());
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (connection != null && !connection.isClosed()) {
                Pool.add(connection);
                log.debug("this conn is create " + connection + "connection size is " + Pool.size() + " FreeConnCount is " + AvailableConn.size());
            }
        }
        return connection;
    }

    public synchronized void Release(Connection conn) {
        if (conn != null) {
            try {
                if (dbconfig.getAutoShrink()//is allow auto shrink pool
                        && state.getNoWorkCount(dbconfig.getIdleTimeOut() * 1000) > 0) {//check no work connection
                    Destory(conn);
                    state.setNoWorkCount(AvailableConn.size());
                } else {
                    if (!conn.isClosed()) {
                        AvailableConn.push(conn);
                    } else {
                        Destory(conn);
                    }
                }
            } catch (SQLException e) {
                log.error(e.getMessage(), e);
            }
        }
        log.debug("Release method connection size is " + Pool.size() + " FreeConnCount is " + AvailableConn.size());
    }

    private synchronized void Destory(Connection conn) throws SQLException {
        if (conn != null) {
            try {
                conn.close();
                Pool.remove(conn);
                log.debug(" close one connection !!!" + conn + " connection size is" + Pool.size() + " FreeConnCount is " + AvailableConn.size());
            } finally {
                conn = null;
            }
        }
    }

    public int getAllCount() {
        return Pool.size();
    }

    public class PoolState {
        private long duration;
        private int noWorkCount = 0;

        public synchronized void setNoWorkCount(int noWorkCount) {
            if (this.noWorkCount <= 2 && noWorkCount > 2) {
                duration = System.currentTimeMillis();
                this.noWorkCount = noWorkCount;
            }
        }

        public synchronized int getNoWorkCount(long duration) {
            if (System.currentTimeMillis() - this.duration > duration) {
                return (noWorkCount - 2);
            }
            return 0;
        }
    }
}
