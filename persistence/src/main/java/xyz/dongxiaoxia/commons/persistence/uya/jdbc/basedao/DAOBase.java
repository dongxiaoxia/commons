package xyz.dongxiaoxia.commons.persistence.uya.jdbc.basedao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.annotation.Table;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.statementcreater.IStatementCreater;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.util.Common;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.wrapper.BeanWrapper;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.wrapper.Wrapper;
import xyz.dongxiaoxia.commons.utils.config.PropertiesLoader;

import java.lang.reflect.Field;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 实现IDAO的抽象基类
 *
 * @author dongxiaoxia
 * @create 2016-07-11 14:30
 */
public abstract class DAOBase implements IDAO {

    private static Logger log = LoggerFactory.getLogger(DAOBase.class);

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

    protected DAOBase() {

    }

    /**
     * 默认配置文件在jar包根目录下，可以在项目资源文件根目录下重名覆盖
     * 创建DAOHelper实例（默认读取jar所在目录下的db.properties文件）
     *
     * @return DAOHelper实例
     * @throws Exception
     */
    public static DAOHelper createInstance() throws Exception {
        return createDAO(DAOBase.class.getClassLoader().getResource(DB_CONFIG_NAME));
    }

    /**
     * @param url 配置文件定位
     * @return
     * @throws Exception
     */
    public static DAOHelper createInstance(URL url) throws Exception {
        return createDAO(url);
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
    private static DAOHelper createDAO(URL url) throws Exception {
        String path = url.getPath();
        if (path.contains("jar")) {
            log.warn("can not find database config ,will use the default db.config:" + url.getPath());
        } else {
            log.info("database config path:" + url.getPath());
        }
        //创建连接池
        ConnectionHelper ch = new ConnectionHelper(url);
        PropertiesLoader propertiesLoader = new PropertiesLoader(url.openStream());
        DAOHelper dao = new DAOHelper(ch);

        //创建一般增删改查的IDAO实例
        DAOBase sqlDao = null;
        String sqlCreaterClass = propertiesLoader.getProperty("SqlCreaterClass");
        if (sqlCreaterClass != null && !sqlCreaterClass.equalsIgnoreCase("")) {
            log.info("init SqlCreaterClass:" + sqlCreaterClass);
            IStatementCreater creater = (IStatementCreater) Class.forName(sqlCreaterClass).newInstance();
            sqlDao = new DAOHandler(creater);
            sqlDao.connHelper = ch;
            sqlDao.wrapper = new BeanWrapper();
            sqlDao.queryTimeOut = propertiesLoader.getInterger("QueryTimeOut");
            sqlDao.insertUpdateTimeOut = propertiesLoader.getInterger("InsertUpdateTimeOut");
            sqlDao.printlnSqlAndTime = propertiesLoader.getBoolean("PrintlnSqlAndTime");
        }
        //创建处理存储过程的IDAO实例
        DAOBase procDAO = null;
        String procCreaterClass = propertiesLoader.getProperty("ProcCreaterClass");
        if (procCreaterClass != null && !procCreaterClass.equals("")) {
            log.info("init ProcCreaterClass:" + procCreaterClass);
            IStatementCreater creater = (IStatementCreater) Class.forName(procCreaterClass).newInstance();
            procDAO = new DAOHandler(creater);
            procDAO.connHelper = ch;
            procDAO.wrapper = new BeanWrapper();
            procDAO.queryTimeOut = propertiesLoader.getInterger("QueryTimeOut");
            procDAO.insertUpdateTimeOut = propertiesLoader.getInterger("InsertUpdateTimeOut");
        }
        dao.sql = sqlDao;
        dao.proc = procDAO;
        log.info("create DAOHelper success!");
        return dao;
    }

    protected <T> List<T> populateData(ResultSet resultSet, Class<T> clazz) throws Exception {
        List<T> dataList = new ArrayList<>();
        List<Field> fieldList = null;
        if (clazz.getAnnotation(Table.class) != null)
            fieldList = Common.getAllFields(clazz);

        ResultSetMetaData rsmd = resultSet.getMetaData();
        int columnsCount = rsmd.getColumnCount();
        List<String> columnNameList = new ArrayList<>();
        for (int i = 0; i < columnsCount; i++) {
            columnNameList.add(rsmd.getColumnLabel(i + 1).toLowerCase());
        }
        boolean notEntityBean = false;
        if (fieldList == null || fieldList.size() == 0) {
            notEntityBean = true;
        }
        while (resultSet.next()) {
            if (notEntityBean && columnsCount == 1) {
                dataList.add(wrapper.wrapperSingleColumn2Object(resultSet, clazz));
            } else if (notEntityBean && columnsCount > 1) {
                throw new SQLException("The class type [ " + clazz.getName() + " ] has no Annotation and ResultSet has " + columnsCount + " columns.");
            } else {
                T bean = wrapper.wrapperColumn2Bean(resultSet, columnNameList, fieldList, clazz);
                dataList.add(bean);
            }
        }
        return dataList;
    }

    public  List<Object> populateData(ResultSet resultSet, Class<?>[] classes) throws Exception {
        List<Object> dataList = new ArrayList<>();
        while (resultSet.next()) {
            dataList.add(wrapper.wrapperColumn2ObjectArray(resultSet, classes));
        }
        return dataList;
    }

    public void printlnSqlAndTime(String sql, long startTime) {
        if (printlnSqlAndTime) {
            log.info("-------------------------------------------Execute Sql   Time:" + (System.currentTimeMillis() - startTime) + " ms)-------------------------------------------");
            log.info(sql);
            log.info("----------------------------------------------------------------------------------------------------------------");
        }
    }

}