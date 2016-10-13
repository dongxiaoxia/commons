package xyz.dongxiaoxia.commons.persistence.uya.jdbc.basedao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.exceptions.DataAccessException;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.statementcreater.IStatementCreater;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.wrapper.Wrapper;
import xyz.dongxiaoxia.commons.utils.PropertiesLoader;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 实现IDAO的抽象基类
 *
 * @author dongxiaoxia
 * @create 2016-07-11 14:30
 */
public abstract class AbstractDAOHandler implements IDAO {

    private static Logger log = LoggerFactory.getLogger(AbstractDAOHandler.class);

    protected IStatementCreater psCreater;
    protected ConnectionHelper connHelper;
    protected Wrapper wrapper;
    /**
     * 默认查询超时时间
     */
    protected int queryTimeOut = 2;

    /**
     * 默认添加/修改超时时间
     */
    protected int insertUpdateTimeOut = 5;

    /**
     * 默认是否输出执行SQL和执行时间
     */
    protected boolean printlnSqlAndTime = false;

    /**
     * 默认数据库配置文件名称
     */
    private static final String DB_CONFIG_NAME = "db.properties";

    protected AbstractDAOHandler() {

    }

    protected AbstractDAOHandler(IStatementCreater creater, ConnectionHelper connectionHelper, xyz.dongxiaoxia.commons.persistence.uya.jdbc.wrapper.Wrapper wrapper, PropertiesLoader propertiesLoader) {
        this.psCreater = creater;
        this.connHelper = connectionHelper;
        this.wrapper = wrapper;
        this.queryTimeOut = propertiesLoader.getInterger("QueryTimeOut");
        this.insertUpdateTimeOut = propertiesLoader.getInterger("InsertUpdateTimeOut");
        this.printlnSqlAndTime = propertiesLoader.getBoolean("PrintlnSqlAndTime");
    }

    /**
     * 打印sql语句与执行消耗时间
     *
     * @param sql
     * @param startTime
     */
    protected void printlnSqlAndTime(String sql, long startTime) {
        if (printlnSqlAndTime) {
            log.info("-------------------------------------------Execute Sql   Time:" + (System.currentTimeMillis() - startTime) + " ms)-------------------------------------------");
            log.info(sql);
            log.info("----------------------------------------------------------------------------------------------------------------");
        }
    }

    /**
     * 重新抛出异常
     *
     * @param cause 发生的异常
     * @param sql   sql语句
     */
    protected void reThrow(Exception cause, String sql) {
        String causeMessage = cause.getMessage();
        if (causeMessage == null) {
            causeMessage = "";
        }
        StringBuffer msg = new StringBuffer(causeMessage);
        msg.append(" Error SQL: ");
        msg.append(sql);
        throw new DataAccessException(msg.toString(), cause);
    }

    /**
     * 填充参数
     * @param stmt
     * @param params
     * @throws SQLException
     */
    protected void fillStatement(PreparedStatement stmt, Object... params)
            throws SQLException{
        // todo
    }
}