package xyz.dongxiaoxia.commons.persistence.uya.jdbc.basedao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbhelper.DBHelper;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.exceptions.DataAccessException;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.statementcreater.IStatementCreater;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.util.DbUtil;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.wrapper.BeanWrapper;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.wrapper.ResultSetHandler;
import xyz.dongxiaoxia.commons.utils.PropertiesLoader;

import java.net.URL;
import java.sql.*;

/**
 * 辅助类 用来引出 sql 和 proc，不直接使用DAOHandler,而是使用本类。
 * 一个程序对应一个DAOHandler,即要求DAOHandler单例,没有特殊情况，使用DBHelper类创建本类对象
 * 一般来说 sql 和 proc 对象已经满足了常规的需求，为了自定义数据库操作，提供了几个方法供客户端使用。
 *
 * @author dongxiaoxia
 * @create 2016-07-11 14:15
 * @see DBHelper#getDbHelper()
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

    private ConnectionHelper connectionHelper;

    public DAOHelper(ConnectionHelper connectionHelper) {
        this.connectionHelper = connectionHelper;
    }

    public DAOHelper(ConnectionHelper connectionHelper, IDAO sql, IDAO proc) {
        this.connectionHelper = connectionHelper;
        this.sql = sql;
        this.proc = proc;
    }

    /**
     * * <b>*.properties格式:</b><br />
     * url:jdbc:sqlserver://127.0.0.1:12345;DatabaseName=DBName<br />
     * driver:com.microsoft.sqlserver.jdbc.SQLServerDriver<br />
     * username:un<br />
     * password:pw<br />
     * minPoolSize:5<br />
     * maxPoolSize:30<br />
     * idleTimeOut:30<br />
     * SqlCreaterClass:xyz.dongxiaoxia.commons.persistence.uya.jdbc.statementcreater.MysqlPSCreater<br />
     * ProcCreaterClass:xyz.dongxiaoxia.commons.persistence.uya.jdbc.statementcreater.ProcCSCreater<br />
     *
     * @param url 配置文件URL对象
     * @return DAO实例
     * @throws Exception 创建DAOHelper过程中可能发生的异常
     * @see xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbconnectionpool.DBConfig
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

    /**
     * 获取ConnectionHelper
     *
     * @return ConnectionHelper
     */
    public ConnectionHelper getConnectionHelper() {
        return this.connectionHelper;
    }

    /**
     * 执行更改数据库操作
     *
     * @param sql     sql语句
     * @param handler PrepareStatement预处理语句处理对象
     * @return sql语句执行结果
     */
    public Object execInsert(String sql, IPreparedStatementHandler handler) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = connectionHelper.get();
            ps = conn.prepareStatement(sql);
            return handler.exec(ps);
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage() + "ExecInsert ERROR SQL:" + sql, e);
        } finally {
            DbUtil.closeStatement(ps);
            connectionHelper.release(conn);
        }
    }

    /**
     * 执行数据库查询操作
     *
     * @param sql     sql查询语句
     * @param handler 数据库结果集处理对象
     * @param <T>     返回结果类型
     * @return 查询结果
     */
    public <T> T execQuery(String sql, ResultSetHandler<T> handler) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement stmt = null;
        try {
            conn = connectionHelper.getReadConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery(sql);
            return handler.handle(rs);
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage() + "ExecQuery ERROR SQL:" + sql, e);
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closeStatement(stmt);
            connectionHelper.release(conn);
        }
    }

    /**
     * 执行存储过程
     *
     * @param sql     存储过程sql语句
     * @param handler CallableStatement处理对象
     * @return 存储过程执行返回结果
     */
    public Object execProc(String sql, ICallableStatementHandler handler) {
        Connection conn = null;
        CallableStatement cs = null;
        try {
            conn = connectionHelper.get();
            cs = conn.prepareCall(sql);
            return handler.exec(cs);
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage() + "ExecCustomProc ERROR SQL: " + sql, e);
        } finally {
            DbUtil.closeStatement(cs);
            connectionHelper.release(conn);
        }
    }

    /**
     * 执行事务任务
     *
     * @param transaction 事务处理操作对象
     */
    public void execTransaction(ITransaction transaction) {
        // 事务开始
        beginTransaction();
        try {
            transaction.exec();
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
     */
    public void beginTransaction() {
        beginTransaction(Connection.TRANSACTION_READ_COMMITTED);
    }

    /**
     * 开启事务
     *
     * @param level 事务级别
     */
    public void beginTransaction(int level) {
        Connection conn = null;
        try {
            conn = connectionHelper.get();
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage(), e);
        }
        if (conn != null) {
            try {
                conn.setAutoCommit(false);
                conn.setTransactionIsolation(level);
                connectionHelper.lockConn(conn);
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
        } else {
            throw new DataAccessException("conn is null when beginTransaction");
        }
    }

    /**
     * 提交事务
     */
    public void commitTransaction() {
        Connection conn = null;
        try {
            conn = connectionHelper.get();
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
     */
    public void rollbackTransaction() {
        Connection conn = null;
        try {
            conn = connectionHelper.get();
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage(), e);
        }
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                throw new DataAccessException(e.getMessage() + " Rollback ERROR", e);
            }
        } else {
            throw new DataAccessException("conn is null when rollbackTransaction");
        }
    }

    /**
     * 结束事务
     */
    public void endTransaction() {
        Connection conn = null;
        try {
            conn = connectionHelper.get();
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
                connectionHelper.unLockConn();
                connectionHelper.release(conn);
            }
        } else {
            throw new DataAccessException("conn is null when endTransaction");
        }
    }
}
