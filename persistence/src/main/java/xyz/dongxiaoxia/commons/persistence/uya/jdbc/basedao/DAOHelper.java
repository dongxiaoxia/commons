package xyz.dongxiaoxia.commons.persistence.uya.jdbc.basedao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.exceptions.DataAccessException;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.statementcreater.IStatementCreater;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.util.DbUtil;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.wrapper.BeanWrapper;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.wrapper.ResultSetHandler;
import xyz.dongxiaoxia.commons.utils.config.PropertiesLoader;

import java.net.URL;
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
     * exec proc handle（存储过程）
     */
    public IDAO proc = null;

    private ConnectionHelper connHelper;

    public DAOHelper(ConnectionHelper connHelper) {
        this.connHelper = connHelper;
    }

    public DAOHelper(ConnectionHelper connHelper, IDAO sql, IDAO proc) {
        this.connHelper = connHelper;
        this.sql = sql;
        this.proc = proc;
    }

    /**
     * <b>*.properties格式:</b><br />
     * url:jdbc:sqlserver://127.0.0.1:12345;DatabaseName=DBName<br />
     * driver:com.microsoft.sqlserver.jdbc.SQLServerDriver<br />
     * username:un<br />
     * password:pw<br />
     * minPoolSize:5<br />
     * maxPoolSize:30<br />
     * idleTimeOut:30<br />
     * SqlCreaterClass:xyz.dongxiaoxia.commons.persistence.uya.jdbc.statementcreater.MysqlPSCreater<br />
     * ProcCreaterClass:xyz.dongxiaoxia.commons.persistence.uya.jdbc.statementcreater.ProcCSCreater<br />
     * </p>
     *
     * @return DAO实例
     * <p>
     */
    public static DAOHelper createDAO(URL url) throws Exception {
        String path = url.getPath();
        if (path.contains("jar")) {
            log.warn("can not find database config ,will use the default db.config:" + url.getPath());
        } else {
            log.info("database config path:" + url.getPath());
        }
        //创建连接池
        ConnectionHelper ch = new ConnectionHelper(url);
        PropertiesLoader propertiesLoader = new PropertiesLoader(url.openStream());


        //创建一般增删改查的IDAO实例
        AbstractDAOHandler sqlDao = null;
        String sqlCreaterClass = propertiesLoader.getProperty("SqlCreaterClass");
        if (sqlCreaterClass != null && !sqlCreaterClass.equalsIgnoreCase("")) {
            log.info("init SqlCreaterClass:" + sqlCreaterClass);
            IStatementCreater creater = (IStatementCreater) Class.forName(sqlCreaterClass).newInstance();
            sqlDao = new DAOHandler(creater, ch, new BeanWrapper(), propertiesLoader);
        }
        //创建处理存储过程的IDAO实例
        AbstractDAOHandler procDAO = null;
        String procCreaterClass = propertiesLoader.getProperty("ProcCreaterClass");
        if (procCreaterClass != null && !procCreaterClass.equals("")) {
            log.info("init ProcCreaterClass:" + procCreaterClass);
            IStatementCreater creater = (IStatementCreater) Class.forName(procCreaterClass).newInstance();
            procDAO = new DAOHandler(creater, ch, new BeanWrapper(), propertiesLoader);
        }
        DAOHelper dao = new DAOHelper(ch, sqlDao, procDAO);
        log.info("create DAOHelper success!");
        return dao;
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
        } finally {
            DbUtil.closeStatement(ps);
            connHelper.release(conn);
        }
    }

    public <T> T execQuery(String sql, ResultSetHandler<T> handler) throws Exception {
        Connection conn = null;
        ResultSet rs = null;
        Statement stmt = null;
        try {
            conn = connHelper.getReadConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            return handler.handle(rs);
        } catch (Exception e) {
            log.error("execQuery error sql:" + sql, e);
            throw e;
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closeStatement(stmt);
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
        } finally {
            DbUtil.closeStatement(cs);
            connHelper.release(conn);
        }
    }


    /**
     * 执行事务任务
     *
     * @param tran
     * @throws Exception
     */
    public void execTransaction(ITransaction tran) {
        // 事务开始
        beginTransaction();
        try {
            tran.exec();
            //事务提交
            commitTransaction();
        } catch (Exception e) {
            //事务回滚
            rollbackTransaction();
            throw new DataAccessException(e);
        } finally {
            //事务结束
            endTransaction();
        }
    }

    /**
     * 开启事务(默认级别TRANSACTION_READ_COMMITTED)
     *
     * @throws Exception
     */
    public void beginTransaction() {
        beginTransaction(Connection.TRANSACTION_READ_COMMITTED);
    }

    /**
     * 开启事务
     *
     * @param level 事务级别
     * @throws Exception
     */
    public void beginTransaction(int level) {
        Connection conn = null;
        try {
            conn = connHelper.get();
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage(), e);
        }
        if (conn != null) {
            try {
                conn.setAutoCommit(false);
                conn.setTransactionIsolation(level);
                connHelper.lockConn(conn);
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
        } else {
            throw new DataAccessException("conn is null when beginTransaction");
        }
    }

    /**
     * 提交事务
     *
     * @throws Exception
     */
    public void commitTransaction() {
        Connection conn = null;
        try {
            conn = connHelper.get();
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage(), e);
        }
        if (conn != null) {
            try {
                conn.commit();
            } catch (SQLException e) {
                throw new DataAccessException(e.getMessage(), e);
            }
        } else {
            throw new DataAccessException("conn is null when commitTransaction");
        }
    }

    /**
     * 回滚事务
     *
     * @throws Exception
     */
    public void rollbackTransaction() {
        Connection conn = null;
        try {
            conn = connHelper.get();
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage(), e);
        }
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                throw new DataAccessException(e.getMessage(), e);
            }
        } else {
            throw new DataAccessException("conn is null when rollbackTransaction");
        }
    }

    /**
     * 结束事务
     *
     * @throws Exception
     */
    public void endTransaction() {
        Connection conn = null;
        try {
            conn = connHelper.get();
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage(), e);
        }
        if (conn != null) {
            try {
                //恢复默认
                conn.setAutoCommit(true);
                conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            } catch (Exception e) {
                throw new DataAccessException(e.getMessage(), e);
            } finally {
                connHelper.unLockConn();
                connHelper.release(conn);
            }
        } else {
            throw new DataAccessException("conn is null when endTransaction");
        }
    }
}
