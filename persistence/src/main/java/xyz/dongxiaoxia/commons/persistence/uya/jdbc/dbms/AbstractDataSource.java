package xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbms;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author dongxiaoxia
 * @create 2016-07-12 14:41
 */
public abstract class AbstractDataSource implements DataSource {

    /**
     * 获得一个只需要读的数据库链接，处理主库压力大的情况下，降低主库压力
     * @return
     * @throws SQLException
     */
    public Connection GetReadConnection() throws SQLException {
        throw new SQLException("Not Implemented");
    }

    public abstract Connection getConnection()throws SQLException;

    public PrintWriter getLogWriter() throws SQLException {
        throw new SQLException("Not Implemented");
    }

    public void setLogWriter(PrintWriter out) throws SQLException {
        throw new SQLException("Not Implemented");
    }

    public void setLoginTimeout(int seconds) throws SQLException {
        throw new SQLException("Not Implemented");
    }

    public int getLoginTimeout() throws SQLException {
        throw new SQLException("Not Implemented");
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new SQLException("Not Implemented");
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new SQLException("Not Implemented");
    }

    public Connection getConnection(String username, String password)
            throws SQLException {
        throw new SQLException("Not Implemented");
    }
}
