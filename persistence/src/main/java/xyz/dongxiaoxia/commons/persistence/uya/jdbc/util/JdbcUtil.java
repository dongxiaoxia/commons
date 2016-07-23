package xyz.dongxiaoxia.commons.persistence.uya.jdbc.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author dongxiaoxia
 * @create 2016-07-12 10:53
 */
public class JdbcUtil {
    private static final Logger log = LoggerFactory.getLogger(JdbcUtil.class);

    public static void closeConnection(Connection conn) {
        if (conn == null) {
            return;
        }
        try {
            conn.close();
        } catch (SQLException e) {
            log.error("Could not close JDBC Connection", e);
        } catch (Throwable e) {
            log.error("Unexpected exceptions on closing JDBC Connection", e);
        }
    }

    public static void closeStatement(Statement stmt) {
        if (stmt == null) {
            return;
        }
        try {
            stmt.close();
        } catch (SQLException e) {
            log.error("Could not close JDBC Statement", e);
        } catch (Throwable e) {
            log.error("Unexpected exceptions on closing JDBC Statement", e);
        }
    }

    public static void closeResultSet(ResultSet rs) {
        if (rs == null) {
            return;
        }
        try {
            rs.close();
        } catch (SQLException ex) {
            log.error("Could not close JDBC ResultSet", ex);
        } catch (Throwable ex) {
            log.error("Unexpected exceptions on closing JDBC ResultSet", ex);
        }
    }

    public static void close(ResultSet rs, Statement stmt, Connection connection) {
        closeResultSet(rs);
        closeStatement(stmt);
        closeConnection(connection);
    }
}
