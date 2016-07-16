package xyz.dongxiaoxia.commons.persistence.uya.jdbc.basedao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.annotation.Table;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.statementcreater.IStatementCreater;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.util.Common;
import xyz.dongxiaoxia.commons.utils.config.PropertiesLoader;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
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
        if (sqlCreaterClass != null && !sqlCreaterClass.equalsIgnoreCase("")) {
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
        if (procCreaterClass != null && !procCreaterClass.equals("")) {
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

    protected <T> List<T> populateData(ResultSet resultSet, Class<T> clazz) throws Exception {
        List<T> dataList = new ArrayList<T>();
        List<Field> fieldList = null;
        if(clazz.getAnnotation(Table.class)!=null)
            fieldList = Common.getAllFields(clazz);

        ResultSetMetaData rsmd = resultSet.getMetaData();
        int columnsCount = rsmd.getColumnCount();
        List<String> columnNameList = new ArrayList<String>();
        for(int i = 0; i < columnsCount; i++){
            columnNameList.add(rsmd.getColumnLabel(i+1).toLowerCase());
        }
        boolean notEntityBean = false;
        if(fieldList==null||fieldList.size()==0){
            notEntityBean = true;
        }
        while (resultSet.next()) {
            if(notEntityBean && columnsCount == 1){
                dataList.add(wrapperSingleColumn2Object(resultSet,clazz));
            }else if(notEntityBean && columnsCount>1){
                throw new SQLException("The class type [ " + clazz.getName() + " ] has no Annotation and ResultSet has " + columnsCount + " columns.");
            }else{
                T bean = wrapperColumn2Bean(resultSet, columnNameList, fieldList, clazz);
                dataList.add(bean);
            }
        }
        return dataList;
    }

    public static List<Object> populateData(ResultSet resultSet, Class<?>[] classes) throws Exception {
        List<Object> dataList = new ArrayList<Object>();
        while (resultSet.next()) {
            dataList.add(wrapperColumn2ObjectArray(resultSet, classes));
        }
        return dataList;
    }

    public void printlnSqlAndTime(String sql, long startTime) {
        if (printlnSqlAndTime) {
            log.info("Execute Sql: " + sql + " Time:" + (System.currentTimeMillis() - startTime) + " ms");
        }
    }

    public static <T> T wrapperSingleColumn2Object(ResultSet resultSet, Class<T> clazz) throws Exception {
        return conver(resultSet, 1, clazz);
    }
    private <T> T wrapperColumn2Bean(ResultSet resultSet, List<String> columnNameList, List<Field> fieldList, Class<T> clazz) throws Exception{
        T bean = clazz.newInstance();
        for (Field f : fieldList) {
            String columnName = Common.getDBCloumnName(clazz, f).toLowerCase();
            if (columnNameList.contains(columnName)) {
                Object columnValueObj = null;
                Class<?> filedCls = f.getType();
                if (filedCls == int.class || filedCls == Integer.class) {
                    columnValueObj = resultSet.getInt(columnName);
                } else if (filedCls == String.class) {
                    columnValueObj = resultSet.getString(columnName);
                } else if (filedCls == boolean.class || filedCls == Boolean.class) {
                    columnValueObj = resultSet.getBoolean(columnName);
                } else if (filedCls == byte.class || filedCls == Byte.class) {
                    columnValueObj = resultSet.getByte(columnName);
                } else if (filedCls == short.class || filedCls == Short.class) {
                    columnValueObj = resultSet.getShort(columnName);
                } else if (filedCls == long.class || filedCls == Long.class) {
                    columnValueObj = resultSet.getLong(columnName);
                } else if (filedCls == float.class || filedCls == Float.class) {
                    columnValueObj = resultSet.getFloat(columnName);
                } else if (filedCls == double.class || filedCls == Double.class) {
                    columnValueObj = resultSet.getDouble(columnName);
                } else if (filedCls == BigDecimal.class) {
                    columnValueObj = resultSet.getBigDecimal(columnName);
                } else {
                    columnValueObj = resultSet.getObject(columnName);
                }

                if (columnValueObj != null) {
                    Method setterMethod = Common.getSetterMethod(clazz, f);
                    setterMethod.invoke(bean, new Object[] { columnValueObj });
                }
            }
        }
        return bean;
    }

    public static Object wrapperColumn2ObjectArray(ResultSet resultSet, Class<?>[] classes) throws Exception {
        int count = classes.length;
        Object[] objects = new Object[count];
        while (count > 0) {
            Class<?> clazz = classes[count-1];
            Object object = conver(resultSet, count, clazz);
            objects[--count] = object;
        }
        return objects;
    }

    public static <T> T conver(ResultSet resultSet ,int index,Class<T> clazz) throws SQLException{
        Object value = null;
        if (clazz == int.class || clazz == Integer.class) {
            value = resultSet.getInt(index);
        } else if (clazz == String.class) {
            value = resultSet.getString(index);
        } else if (clazz == boolean.class || clazz == Boolean.class) {
            value = resultSet.getBoolean(index);
        } else if (clazz == byte.class || clazz == Byte.class) {
            value = resultSet.getByte(index);
        } else if (clazz == short.class || clazz == Short.class) {
            value = resultSet.getShort(index);
        } else if (clazz == long.class || clazz == Long.class) {
            value = resultSet.getLong(index);
        } else if (clazz == float.class || clazz == Float.class) {
            value = resultSet.getFloat(index);
        } else if (clazz == double.class || clazz == Double.class) {
            value = resultSet.getDouble(index);
        } else if (clazz == BigDecimal.class) {
            value = resultSet.getBigDecimal(index);
		/* */
        } else {
            value = resultSet.getObject(index);
        }
        return (T) value;
    }
}