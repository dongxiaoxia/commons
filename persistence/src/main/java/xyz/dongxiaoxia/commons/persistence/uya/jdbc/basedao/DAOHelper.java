package xyz.dongxiaoxia.commons.persistence.uya.jdbc.basedao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbconnectionpool.ConnectionPool;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.statementcreater.IStatementCreater;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.statementcreater.MysqlPSCreater;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.statementcreater.SqlServerPSCreater;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.util.JdbcUtil;

import java.sql.*;

/**
 * 辅助类 用来引出 sql 和 proc
 *
 * @author dongxiaoxia
 * @create 2016-07-11 14:15
 */
public class DAOHelper {
    private static Logger log = LoggerFactory.getLogger(DAOHelper.class);
    /**
     * exec sql handle
     */
    public IDAO sql = null;

    /**
     * exec proc handle
     */
    public IDAO proc = null;


//	public DAOHelper(ConnectionPool connPool){
//		this.connPool = connPool;
//	}

    public DAOHelper(ConnectionHelper connHelper) {
        this.connHelper = connHelper;
    }

    private ConnectionHelper connHelper;
    @Deprecated
    public ConnectionPool getConnPool(){
        return connHelper.getConnPool();
    }


    public ConnectionHelper getConnHelper() {
        return this.connHelper;
    }


    public Object execInsert(String sql, IPreparedStatementHandler handler) throws Exception {
        return execWithPara(sql, handler);
    }

    public Object execWithPara(String sql, IPreparedStatementHandler handler) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = connHelper.get();
            ps = conn.prepareStatement(sql);
            return handler.exec(ps);
        } catch (Exception e) {
            log.error("execQuery error sql:" + sql, e);
            throw e;
        }
        finally{
            JdbcUtil.closeStatement(ps);
            connHelper.release(conn);
        }
    }

    public Object execQuery(String sql, IRowCallbackHandler handler) throws Exception {
        Connection conn = null;
        ResultSet rs = null;
        Statement stmt = null;
        try {
            // 2011-05-24 使用只读连接
//			conn = connHelper.get();
            conn = connHelper.getReadConnection();


            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            return handler.exec(rs);
        } catch (Exception e) {
            log.error("execQuery error sql:" + sql, e);
            throw e;
        }
        finally{
            JdbcUtil.closeResultSet(rs);
            JdbcUtil.closeStatement(stmt);
            connHelper.release(conn);
        }
    }

    public Object execProc(String sql, ICallableStatementHandler handler) throws Exception {
        Connection conn = null;
        CallableStatement cs = null;
        try {
            conn = connHelper.get();
            cs = conn.prepareCall(sql);
            return handler.exec(cs);
        } catch (SQLException e) {
            log.error("execCustomProc error " + sql);
            throw e;
        }
        finally{
            JdbcUtil.closeStatement(cs);
            connHelper.release(conn);
        }
    }

    @Deprecated
    public Exception execTransaction(ITransactionHandler handler) throws Exception {
        Connection conn = null;
        Exception exception = null;
        try {
            IStatementCreater sqlServerCreater = new SqlServerPSCreater();
            IStatementCreater mysqlCreater = new MysqlPSCreater();
            conn = connHelper.get();
            conn.setAutoCommit(false);
            try {
                handler.exec(conn, sqlServerCreater, mysqlCreater);
            } catch (Exception ex) {
                exception = ex;
            }
        } catch (SQLException e) {
            log.error("execCustomProc error " + sql);
            throw e;
        }
        finally{
            try {
                conn.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
            connHelper.release(conn);
        }

        return exception;
    }

    /**
     * 执行事务任务
     * @param tran
     * @throws Exception
     */
    public void execTransaction(ITransaction tran) throws Exception {
        // 事务开始
        beginTransaction();

        try {
            tran.exec();
            //事务提交
            commitTransaction();
        } catch(Exception ex) {
            //事务回滚
            rollbackTransaction();

            throw ex;
        } finally {
            //事务结束
            endTransaction();
        }
    }

    /**
     * 开启事务(默认级别TRANSACTION_READ_COMMITTED)
     * @throws Exception
     */
    public void beginTransaction() throws Exception {
        beginTransaction(Connection.TRANSACTION_READ_COMMITTED);
    }

    /**
     * 开启事务
     * @param level 事务级别
     * @throws Exception
     */
    public void beginTransaction(int level) throws Exception {
        Connection conn = connHelper.get();
        if(conn != null) {
            try {
                conn.setAutoCommit(false);
                conn.setTransactionIsolation(level);
                connHelper.lockConn(conn);
            } catch(Exception ex) {
                log.error(ex.getMessage(),ex);
            }
        } else {
            throw new Exception("conn is null when beginTransaction");
        }
    }

    /**
     * 提交事务
     * @throws Exception
     */
    public void commitTransaction() throws Exception {
        Connection conn = connHelper.get();
        if(conn != null) {
            conn.commit();
        } else {
            throw new Exception("conn is null when commitTransaction");
        }
    }

    /**
     * 回滚事务
     * @throws Exception
     */
    public void rollbackTransaction() throws Exception {
        Connection conn = connHelper.get();
        if(conn != null) {
            conn.rollback();
        } else {
            throw new Exception("conn is null when rollbackTransaction");
        }
    }

    /**
     * 结束事务
     * @throws Exception
     */
    public void endTransaction() throws Exception {
        Connection conn = connHelper.get();
        if(conn != null) {
            try{
                //恢复默认
                conn.setAutoCommit(true);
                conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            }finally{
                connHelper.unLockConn();
                connHelper.release(conn);
            }
        } else {
            throw new Exception("conn is null when endTransaction");
        }
    }
}
