package xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbconnectionpool;

import xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbms.AbstractDataSource;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.util.JdbcUtil;

import java.sql.Connection;

/**
 * @author dongxiaoxia
 * @create 2016-07-12 14:57
 */
public class SwapConnectionPool extends ConnectionPool {

    private AbstractDataSource dataSource;

    public SwapConnectionPool(AbstractDataSource abstractDataSource) {
        this.dataSource = dataSource;
    }

    public int GetAllCount()
    {
        return -1;
    }

    public int GetFreeConnCount()
    {
        return -1;
    }

    public synchronized Connection Get() throws Exception
    {
        return dataSource.getConnection();
    }

    public synchronized void Release(Connection connection)
    {
        JdbcUtil.closeConnection(connection);
    }

    public Connection GetReadConnection() throws Exception {
        return dataSource.GetReadConnection();
    }
}
