package xyz.dongxiaoxia.commons.persistence.uya.jdbc.basedao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.statementcreater.IStatementCreater;
import xyz.dongxiaoxia.commons.utils.config.PropertiesLoader;

/**
 * @author dongxiaoxia
 * @create 2016-07-11 14:30
 */
public abstract class DAOBase implements IDAO {

    private static  Logger log = LoggerFactory.getLogger(DAOBase.class);

    protected IStatementCreater psCreater;
    protected ConnectionHelper connHelper;

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
     * 默认数据库配置文件路径（jar包所在目录）
     */
    private static final String DB_CONFIG_PATH = "db.properties";

    protected DAOBase() {

    }

    /**
     * 创建DAO实例（默认读取jar所在目录下的db.properties文件）
     * <p>
     * <b>*.properties格式:</b><br />
     * ConnetionURL:jdbc:sqlserver://127.0.0.1:12345;DatabaseName=DBName<br />
     * DriversClass:com.microsoft.sqlserver.jdbc.SQLServerDriver<br />
     * UserName:un<br />
     * PassWord:pw<br />
     * MinPoolSize:5<br />
     * MaxPoolSize:30<br />
     * IdleTimeout:30<br />
     * SqlCreaterClass:com.baiyz.can.lib.dao.sqlcreate.SqlServerSQLCreater<br />
     * ProcCreaterClass:com.baiyz.can.lib.dao.sqlcreate.AutoCreateProcParaCreater<br />
     * </p>
     *
     * @return DAO实例
     */
    public static DAOHelper createInstance() throws Exception {
        return createDAO(DB_CONFIG_PATH);
    }

    /**
     * 创建DAO实例
     *
     * @param configPath 配置文件路径
     *                   <p>
     *                   <b>*.properties格式:</b><br />
     *                   ConnetionURL:jdbc:sqlserver://127.0.0.1:12345;DatabaseName=DBName<br />
     *                   DriversClass:com.microsoft.sqlserver.jdbc.SQLServerDriver<br />
     *                   UserName:un<br />
     *                   PassWord:pw<br />
     *                   MinPoolSize:5<br />
     *                   MaxPoolSize:30<br />
     *                   IdleTimeout:30<br />
     *                   SqlCreaterClass:com.baiyz.can.lib.dao.sqlcreate.SqlServerSQLCreater<br />
     *                   ProcCreaterClass:com.baiyz.can.lib.dao.sqlcreate.AutoCreateProcParaCreater<br />
     *                   </p>
     * @return DAO实例
     * @throws Exception
     */
    public static DAOHelper createInstance(String configPath) throws Exception {
        return createDAO(configPath);
    }

    private static DAOHelper createDAO(String configPath) throws Exception {
        ConnectionHelper ch = new ConnectionHelper(configPath);
        PropertiesLoader propertiesLoader = new PropertiesLoader(configPath);
        DAOHelper dao = new DAOHelper(ch);

        DAOBase sqlDao = null;
        String sqlCreaterClass = propertiesLoader.getProperty("SqlCreaterClass");
        if (sqlCreaterClass !=null && !sqlCreaterClass.equalsIgnoreCase("")){
            log.info("init SqlCreaterClass:" + sqlCreaterClass);
            IStatementCreater creater = (IStatementCreater) Class.forName(sqlCreaterClass).newInstance();
            sqlDao = new DAOHandler(creater);
            sqlDao.connHelper = ch;
            sqlDao.queryTimeOut = propertiesLoader.getInterger("QueryTimeOut");
            sqlDao.insertUpdateTimeOut = propertiesLoader.getInterger("InsertUpdateTimeOut");
            sqlDao.printlnSqlAndTime = propertiesLoader.getBoolean("PrintlnSqlAndTime");
        }

        DAOBase procDAO = null;
        String procCreaterClass = propertiesLoader.getProperty("ProcCreaterClass");
        if (procCreaterClass != null && !procCreaterClass.equals("")){
            log.info("init ProcCreaterClass:" + procCreaterClass);
            IStatementCreater creater = (IStatementCreater) Class.forName(procCreaterClass).newInstance();
            procDAO = new DAOHandler(creater);
            procDAO.connHelper = ch;
            procDAO.queryTimeOut = propertiesLoader.getInterger("QueryTimeOut");
            procDAO.insertUpdateTimeOut = propertiesLoader.getInterger("InsertUpdateTimeOut");
        }

        dao.sql = sqlDao;
        dao.proc = procDAO;
        log.info("create DAOHelper success!");
        return dao;
    }

    public void printlnSqlAndTime(String sql,long startTime){
        if (printlnSqlAndTime){
            log.info("Execute Sql: " + sql + " Time:" + (System.currentTimeMillis() - startTime) + " ms");
        }
    }
}