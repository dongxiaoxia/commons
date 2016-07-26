package xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbconnectionpool;

import xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbms.AbstractDataSource;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.util.DbUtil;

import java.sql.Connection;

/**
 * 该ConnectionPool封装SWAP DataSource实现
 * @author dongxiaoxia
 * @create 2016-07-12 14:57
 */
public class SwapConnectionPool extends ConnectionPool {

    private AbstractDataSource dataSource;

    public SwapConnectionPool(AbstractDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public int GetAllCount()
    {
        return -1;
    }

    public int getFreeConnCount()
    {
        return -1;
    }

    public synchronized Connection get() throws Exception
    {
        return dataSource.getConnection();
    }

    public synchronized void release(Connection connection)
    {
        DbUtil.closeConnection(connection);
    }

    public Connection GetReadConnection() throws Exception {
        return dataSource.getReadConnection();
    }
}
