package xyz.dongxiaoxia.commons.persistence.uya.jdbc.basedao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbhelper.DBHelper;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.exceptions.DataAccessException;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.statementcreater.IStatementCreater;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.util.Common;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.util.DbUtil;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.util.OutSQL;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.util.SqlInjectHelper;
import xyz.dongxiaoxia.commons.utils.config.PropertiesLoader;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author dongxiaoxia
 * @create 2016-07-11 15:02
 */
public class DAOHandler extends AbstractDAOHandler {
    private static Logger log = LoggerFactory.getLogger(DAOHandler.class);

    public DAOHandler(IStatementCreater creater, ConnectionHelper connectionHelper, xyz.dongxiaoxia.commons.persistence.uya.jdbc.wrapper.Wrapper wrapper, PropertiesLoader propertiesLoader) {
        super(creater, connectionHelper, wrapper, propertiesLoader);
    }

    @Override
    public Object insert(Object bean) {
        return insert(bean, insertUpdateTimeOut);
    }

    public Object[] insert(String sql, Object... param) {
        Connection conn = null;
        PreparedStatement ps = null;
        Object[] ids = null;
        try {
            conn = connHelper.get();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.addBatch(sql);
            ps.setQueryTimeout(insertUpdateTimeOut);
            if (param != null) {
                for (int i = 0; i < param.length; i++) {
                    Common.setPara(ps, param[i], i + 1);
                }
            }
            long startTime = System.currentTimeMillis();
            ps.executeUpdate();
            printlnSqlAndTime(sql, startTime);
            List<Object> idList = new ArrayList<>();
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()) {
                idList.add(rs.getObject(1));
            }
            ids = idList.toArray(new Object[0]);
        } catch (Exception e) {
            reThrow(e, sql);
        } finally {
            DbUtil.closeStatement(ps);
            connHelper.release(conn);
        }
        return ids;
    }

    @Override
    public void updateEntity(Object bean) {
        updateEntity(bean, queryTimeOut);
    }

    @Override
    public <I> void updateByID(Class<?> clazz, String updateStatement, I id) {
        updateByID(clazz, updateStatement, id, insertUpdateTimeOut);
    }

    @Override
    public void updateByCustom(Class<?> clazz, String updateStatement, String condition) {
        updateByCustom(clazz, updateStatement, condition, insertUpdateTimeOut);
    }

    @Override
    public <I> void deleteByID(Class<?> clazz, I id) {
        deleteByID(clazz, id, queryTimeOut);
    }

    @Override
    public <I> void deleteByIDS(Class<?> clazz, I[] ids) {
        deleteByIDS(clazz, ids, queryTimeOut);
    }

    @Override
    public void deleteByCustom(Class<?> clazz, String condition) {
        deleteByCustom(clazz, condition, queryTimeOut);
    }

    @Override
    public <I> Object get(Class<?> clazz, I id) {
        return get(clazz, id, queryTimeOut);
    }

    @Override
    public Object getByCustom(Class<?> clazz, String columns, String condition, String orderBy) {
        return get(clazz, columns, condition, orderBy, queryTimeOut);
    }

    @Override
    public List<?> getListByCustom(Class<?> clazz, String columns, String condition, String orderBy) {
        return getListByCustom(clazz, columns, condition, orderBy, queryTimeOut);
    }

    @Override
    public List<?> getListByPage(Class<?> clazz, String condition, String columns, int page, int pageSize, String orderBy) {
        return getListByPage(clazz, condition, columns, page, pageSize, orderBy, queryTimeOut);
    }

    @Override
    public List<Object[]> customSql(String sql, int columnCount) {
        return customSql(sql, columnCount, queryTimeOut);
    }

    @Override
    public void customSqlNoReturn(String sql) {
        customSqlNoReturn(sql, queryTimeOut);
    }

    @Override
    public int getCount(Class<?> clazz, String condition) {
        return getCount(clazz, condition, queryTimeOut);
    }

    @Override
    public <T, I> List<T> getListByIDS(Class<T> clazz, I[] ids) {
        return getListByIDS(clazz, ids, queryTimeOut);
    }

    @Override
    public void updateByCustom(Class<?> clazz, Map<String, Object> kv, Map<String, Object> condition) {
        updateByCustom(clazz, kv, condition, insertUpdateTimeOut);
    }

    @Override
    public void deleteByCustom(Class<?> clazz, Map<String, Object> condition) {
        deleteByCustom(clazz, condition, queryTimeOut);
    }

    @Override
    public <T> List<T> getListByCustom(Class<T> clazz, String columns, Map<String, Object> condition, String orderBy) {
        return getListByCustom(clazz, columns, condition, orderBy, queryTimeOut);
    }

    @Override
    public <T> List<T> getListByPage(Class<T> clazz, Map<String, Object> condition, String columns, int page, int pageSize, String orderBy) {
        return getListByPage(clazz, condition, columns, page, pageSize, orderBy, queryTimeOut);
    }

    @Override
    public int getCount(Class<?> clazz, Map<String, Object> condition) {
        return getCount(clazz, condition, queryTimeOut);
    }


    @Override
    public <T> List<T> getListBySQL(Class<T> clazz, String sql, Object... param) {
        return getListBySQL(clazz, sql, queryTimeOut, param);
    }

    @Override
    public int execBySQL(String sql, Object... param) {
        return execBySQL(sql, insertUpdateTimeOut, param);
    }

    @Override
    public int getCountBySQL(String sql, Object... param) {
        return getCountBySQL(sql, queryTimeOut, param);
    }

    @Override
    public int getCountBySQL(String sql, int timeOut, Object... param) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int count = 0;
        try {
            conn = connHelper.getReadConnection();
            ps = conn.prepareStatement(sql);
            ps.setQueryTimeout(timeOut);

            if (param != null) {
                for (int i = 0; i < param.length; i++) {
                    Common.setPara(ps, param[i], i + 1);
                }
            }
            long startTime = System.currentTimeMillis();
            rs = ps.executeQuery();
            printlnSqlAndTime(sql, startTime);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            reThrow(e, sql);
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closeStatement(ps);
            connHelper.release(conn);
        }
        return count;
    }

    @Override
    public <T> List<T> getListBySQL(Class<T> clazz, String sql, int timeOut, Object... param) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        List<T> dataList = null;
        try {
            conn = connHelper.getReadConnection();
            ps = conn.prepareStatement(sql);
            ps.setQueryTimeout(timeOut);

            if (param != null) {
                for (int i = 0; i < param.length; i++) {
                    Common.setPara(ps, param[i], i + 1);
                }
            }
            long startTime = System.currentTimeMillis();
            rs = ps.executeQuery();
            printlnSqlAndTime(sql, startTime);
            dataList = wrapper.populateData(rs, clazz);
        } catch (Exception e) {
            reThrow(e, sql);
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closeStatement(ps);
            connHelper.release(conn);
        }
        return dataList;
    }

    @Override
    public List<Object> getListBySQL(Class<?>[] classes, String sql, Object... param) {
        return getListBySQL(classes, sql, queryTimeOut, param);
    }

    @Override
    public List<Object> getListBySQL(Class<?>[] classes, String sql, int timeOut, Object... param) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        List<Object> dataList = null;
        try {
            conn = connHelper.getReadConnection();
            ps = conn.prepareStatement(sql);
            ps.setQueryTimeout(timeOut);

            if (param != null) {
                for (int i = 0; i < param.length; i++) {
                    Common.setPara(ps, param[i], i + 1);
                }
            }
            long startTime = System.currentTimeMillis();
            rs = ps.executeQuery();
            printlnSqlAndTime(sql, startTime);
            dataList = wrapper.populateData(rs, classes);
        } catch (Exception e) {
            reThrow(e, sql);
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closeStatement(ps);
            connHelper.release(conn);
        }
        return dataList;
    }

    @Override
    public int execBySQL(String sql, int timeOut, Object... param) {
        Connection conn = null;
        PreparedStatement ps = null;
        int result = 0;
        try {
            conn = connHelper.get();
            ps = conn.prepareStatement(sql);
            ps.setQueryTimeout(timeOut);

            if (param != null) {
                for (int i = 0; i < param.length; i++) {
                    Common.setPara(ps, param[i], i + 1);
                }
            }
            long startTime = System.currentTimeMillis();
            result = ps.executeUpdate();
            printlnSqlAndTime(sql, startTime);
        } catch (Exception e) {
            reThrow(e, sql);
        } finally {
            DbUtil.closeStatement(ps);
            connHelper.release(conn);
        }
        return result;
    }

    @Override
    public int[] execBatchSql(String sql, List<Object[]> paramList, boolean useTransaction) {
        Connection conn = null;
        PreparedStatement ps = null;
        int[] results = null;
        try {
            conn = connHelper.get();
            if (useTransaction) {
                DBHelper.getDbHelper().beginTransaction();
            }
            ps = conn.prepareStatement(sql);
            ps.setQueryTimeout(insertUpdateTimeOut);

            for (Object[] param : paramList) {
                for (int i = 0; i < param.length; i++) {
                    Common.setPara(ps, param[i], i + 1);
                }
                ps.addBatch();
            }

            long startTime = System.currentTimeMillis();
            results = ps.executeBatch();
            printlnSqlAndTime(sql, startTime);
            if (useTransaction) {
                DBHelper.getDbHelper().commitTransaction();
            }
        } catch (Exception e) {
            if (useTransaction) {
                try {
                    DBHelper.getDbHelper().rollbackTransaction();
                } catch (Exception e1) {
                    log.error("rollbackTransaction error sql:" + sql, e1);
                    throw new DataAccessException(e1);
                }
            }
            reThrow(e, sql);
        } finally {
            DbUtil.closeStatement(ps);
            if (useTransaction) {
                try {
                    DBHelper.getDbHelper().endTransaction();
                } catch (Exception e) {
                    log.error("endTransaction error sql:" + sql, e);
                }
            } else {
                connHelper.release(conn);
            }
        }
        return results;
    }

    @Override
    public void updateByCustom(Class<?> clazz, Map<String, Object> kv, Map<String, Object> condition, int timeOut) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public void deleteByCustom(Class<?> clazz, Map<String, Object> condition, int timeOut) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public <T> List<T> getListByCustom(Class<T> clazz, String columns, Map<String, Object> condition, String orderBy, int timeOut) {
        //TODO
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public <T> List<T> getListByPage(Class<T> clazz, Map<String, Object> condition, String columns, int page, int pageSize, String orderBy, int timeOut) {
        //TODO
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public int getCount(Class<?> clazz, Map<String, Object> condition, int timeOut) {
        //TODO
        throw new UnsupportedOperationException("Not supported");
    }


    public <T, I> List<T> getListByIDS(Class<T> clazz, I[] ids, int timeOut) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        List<T> dataList = null;
        OutSQL sql = new OutSQL();
        try {
            conn = connHelper.getReadConnection();
            ps = psCreater.createGetByIDS(clazz, conn, ids, sql);
            ps.setQueryTimeout(timeOut);
            long startTime = System.currentTimeMillis();
            rs = ps.executeQuery();
            printlnSqlAndTime(sql.getRealSql(), startTime);
            dataList = wrapper.populateData(rs, clazz);
        } catch (Exception e) {
            reThrow(e, sql.getSql());
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closeStatement(ps);
            connHelper.release(conn);
        }
        return dataList;
    }


    public Object insert(Object bean, int timeOut) {
        Class<?> beanCls = bean.getClass();

        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        Object rst = null;
        OutSQL sql = new OutSQL();
        try {
            conn = connHelper.get();
            ps = psCreater.createInsert(bean, conn, sql);
            ps.setQueryTimeout(timeOut);
            long startTime = System.currentTimeMillis();
            ps.executeUpdate();
            printlnSqlAndTime(sql.getRealSql(), startTime);
            boolean isProc = false;
            Class<?>[] clsAry = ps.getClass().getInterfaces();
            for (Class<?> cls : clsAry) {
                if (cls == CallableStatement.class) {
                    isProc = true;
                    break;
                }
            }

            List<java.lang.reflect.Field> identityFields = Common.getIdentityFields(beanCls);
            if (isProc) {
                if (identityFields.size() == 1) {
                    rst = ((CallableStatement) ps).getObject(Common.getDBCloumnName(beanCls, identityFields.get(0)));
                }
            } else {
                if (identityFields.size() == 1) {
                    rs = ps.getGeneratedKeys();
                    if (rs.next()) {
                        List<Field> idFieldList = Common.getIdFields(beanCls);
                        if (idFieldList.size() == 1) {
                            if (idFieldList.get(0).getType() == int.class
                                    || idFieldList.get(0).getType() == Integer.class) {
                                rst = rs.getInt(1);
                            } else if (idFieldList.get(0).getType() == long.class
                                    || idFieldList.get(0).getType() == Long.class) {
                                rst = rs.getLong(1);
                            } else if (idFieldList.get(0).getType() == String.class) {
                                rst = rs.getString(1);
                            } else {
                                rst = rs.getObject(1);
                            }
                        } else {
                            rst = rs.getObject(1);
                        }
                    }
                } else if (identityFields.size() == 0) {
                    List<java.lang.reflect.Field> idFields = Common.getIdFields(beanCls);
                    if (idFields.size() == 1) {
                        Field id = idFields.get(0);
                        id.setAccessible(true);
                        rst = id.get(bean);
                    }
                }
            }
        } catch (Exception e) {
            reThrow(e, sql.getSql());
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closeStatement(ps);
            connHelper.release(conn);
        }
        return rst;
    }

    public void updateEntity(Object entity, int timeOut) {
        Connection conn = null;
        PreparedStatement ps = null;
        OutSQL sql = new OutSQL();
        try {
            conn = connHelper.get();
            ps = psCreater.createUpdateEntity(entity, conn, sql);
            ps.setQueryTimeout(timeOut);
            long startTime = System.currentTimeMillis();
            ps.executeUpdate();
            printlnSqlAndTime(sql.getRealSql(), startTime);
        } catch (Exception e) {
            reThrow(e, sql.getSql());
        } finally {
            DbUtil.closeStatement(ps);
            connHelper.release(conn);
        }
    }

    public <I> void updateByID(Class<?> clazz, String updateStatement, I id, int timeOut) {
        Connection conn = null;
        PreparedStatement ps = null;
        OutSQL sql = new OutSQL();
        try {
            conn = connHelper.get();
            ps = psCreater.createUpdateByID(clazz, conn, updateStatement, id, sql);
            ps.setQueryTimeout(timeOut);
            long startTime = System.currentTimeMillis();
            ps.executeUpdate();
            printlnSqlAndTime(sql.getRealSql(), startTime);
        } catch (Exception e) {
            reThrow(e, sql.getSql());
        } finally {
            DbUtil.closeStatement(ps);
            connHelper.release(conn);
        }
    }

    public void updateByCustom(Class<?> clazz, String updateStatement, String condition, int timeOut) {
        condition = SqlInjectHelper.simpleFilterSql(condition);
        updateStatement = SqlInjectHelper.simpleFilterSql(updateStatement);

        Connection conn = null;
        PreparedStatement ps = null;
        OutSQL sql = new OutSQL();
        try {
            conn = connHelper.get();
            ps = psCreater.createUpdateByCustom(clazz, conn, updateStatement, condition, sql);
            ps.setQueryTimeout(timeOut);
            long startTime = System.currentTimeMillis();
            ps.executeUpdate();
            printlnSqlAndTime(sql.getRealSql(), startTime);
        } catch (Exception e) {
            reThrow(e, sql.getSql());
        } finally {
            DbUtil.closeStatement(ps);
            connHelper.release(conn);
        }
    }

    public <I> void deleteByID(Class<?> clazz, I id, int timeOut) {
        Connection conn = null;
        PreparedStatement ps = null;
        OutSQL sql = new OutSQL();
        try {
            conn = connHelper.get();
            ps = psCreater.createDelete(clazz, conn, id, sql);
            ps.setQueryTimeout(timeOut);
            long startTime = System.currentTimeMillis();
            ps.execute();
            printlnSqlAndTime(sql.getRealSql(), startTime);
        } catch (Exception e) {
            reThrow(e, sql.getSql());
        } finally {
            DbUtil.closeStatement(ps);
            connHelper.release(conn);
        }
    }

    public <I> void deleteByIDS(Class<?> clazz, I[] ids, int timeOut) {
        Connection conn = null;
        PreparedStatement ps = null;
        OutSQL sql = new OutSQL();
        try {
            conn = connHelper.get();
            ps = psCreater.createDeleteByIDS(clazz, conn, ids, sql);
            ps.setQueryTimeout(timeOut);
            long startTime = System.currentTimeMillis();
            ps.execute();
            printlnSqlAndTime(sql.getRealSql(), startTime);
        } catch (Exception e) {
            reThrow(e, sql.getSql());
        } finally {
            DbUtil.closeStatement(ps);
            connHelper.release(conn);
        }
    }

    public void deleteByCustom(Class<?> clazz, String condition, int timeOut) {
        condition = SqlInjectHelper.simpleFilterSql(condition);

        Connection conn = null;
        PreparedStatement ps = null;
        OutSQL sql = new OutSQL();
        try {
            conn = connHelper.get();
            ps = psCreater.createDeleteByCustom(clazz, conn, condition, sql);
            ps.setQueryTimeout(timeOut);
            long startTime = System.currentTimeMillis();
            ps.execute();
            printlnSqlAndTime(sql.getRealSql(), startTime);
        } catch (Exception e) {
            reThrow(e, sql.getSql());
        } finally {
            DbUtil.closeStatement(ps);
            connHelper.release(conn);
        }
    }

    public <I> Object get(Class<?> clazz, I id, int timeOut) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        List<?> dataList = null;
        OutSQL sql = new OutSQL();
        try {
            conn = connHelper.getReadConnection();
            ps = psCreater.createGetEntity(clazz, conn, id, sql);
            ps.setQueryTimeout(timeOut);
            long startTime = System.currentTimeMillis();
            rs = ps.executeQuery();
            printlnSqlAndTime(sql.getRealSql(), startTime);
            dataList = wrapper.populateData(rs, clazz);
        } catch (Exception e) {
            reThrow(e, sql.getSql());
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closeStatement(ps);
            connHelper.release(conn);
        }
        if (dataList != null && dataList.size() > 0) {
            return dataList.get(0);
        } else {
            return null;
        }
    }

    public Object get(Class<?> clazz, String columns, String condition, String orderBy, int timeOut) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        List<?> dataList = null;
        OutSQL sql = new OutSQL();
        try {
            conn = connHelper.getReadConnection();
            ps = psCreater.createGetByCustom(clazz, conn, columns, condition, orderBy, sql);
            ps.setQueryTimeout(timeOut);
            long startTime = System.currentTimeMillis();
            rs = ps.executeQuery();
            printlnSqlAndTime(sql.getRealSql(), startTime);
            dataList = wrapper.populateData(rs, clazz);
        } catch (Exception e) {
            reThrow(e, sql.getSql());
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closeStatement(ps);
            connHelper.release(conn);
        }
        if (dataList != null && dataList.size() > 0) {
            return dataList.get(0);
        } else {
            return null;
        }
    }

    public List<?> getListByCustom(Class<?> clazz, String columns, String condition, String orderBy, int timeOut) {
        columns = SqlInjectHelper.simpleFilterSql(columns);
        condition = SqlInjectHelper.simpleFilterSql(condition);
        orderBy = SqlInjectHelper.simpleFilterSql(orderBy);
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        List<?> dataList = null;
        OutSQL sql = new OutSQL();
        try {
            conn = connHelper.getReadConnection();
            ps = psCreater.createGetByCustom(clazz, conn, columns, condition, orderBy, sql);
            ps.setQueryTimeout(timeOut);
            long startTime = System.currentTimeMillis();
            rs = ps.executeQuery();
            printlnSqlAndTime(sql.getRealSql(), startTime);
            dataList = wrapper.populateData(rs, clazz);
        } catch (Exception e) {
            reThrow(e, sql.getSql());
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closeStatement(ps);
            connHelper.release(conn);
        }
        return dataList;
    }

    public List<?> getListByPage(Class<?> clazz,
                                 String condition,
                                 String columns,
                                 int page,
                                 int pageSize,
                                 String orderBy,
                                 int timeOut) {
        columns = SqlInjectHelper.simpleFilterSql(columns);
        condition = SqlInjectHelper.simpleFilterSql(condition);
        orderBy = SqlInjectHelper.simpleFilterSql(orderBy);
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        List<?> dataList = null;
        OutSQL sql = new OutSQL();
        try {
            conn = connHelper.getReadConnection();
            ps = psCreater.createGetByPage(clazz, conn, condition, columns, page, pageSize, orderBy, sql);
            ps.setQueryTimeout(timeOut);
            long startTime = System.currentTimeMillis();
            rs = ps.executeQuery();
            printlnSqlAndTime(sql.getRealSql(), startTime);
            dataList = wrapper.populateData(rs, clazz);
        } catch (Exception e) {
            reThrow(e, sql.getSql());
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closeStatement(ps);
            connHelper.release(conn);
        }
        return dataList;
    }

    public int getCount(Class<?> clazz,
                        String condition,
                        int timeOut) {
        condition = SqlInjectHelper.simpleFilterSql(condition);
        int count = 0;
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        OutSQL sql = new OutSQL();
        try {
            conn = connHelper.getReadConnection();
            ps = psCreater.createGetCount(clazz, conn, condition, sql);
            ps.setQueryTimeout(timeOut);
            long startTime = System.currentTimeMillis();
            rs = ps.executeQuery();
            printlnSqlAndTime(sql.getRealSql(), startTime);
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            reThrow(e, sql.getSql());
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closeStatement(ps);
            connHelper.release(conn);
        }
        return count;
    }

    public List<Object[]> customSql(String sql,
                                    int columnCount,
                                    int timeOut) {
        List<Object[]> list = new ArrayList<>();
        Connection conn = null;
        ResultSet rs = null;
        Statement stmt = null;
        try {
            conn = connHelper.get();
            stmt = conn.createStatement();
            stmt.setQueryTimeout(timeOut);
            long startTime = System.currentTimeMillis();
            rs = stmt.executeQuery(sql);
            printlnSqlAndTime(sql, startTime);
            while (rs.next()) {
                Object[] objAry = new Object[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    objAry[i] = rs.getObject(i + 1);
                }
                list.add(objAry);
            }
        } catch (Exception e) {
            reThrow(e, sql);
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closeStatement(stmt);
            connHelper.release(conn);
        }
        return list;
    }

    public void customSqlNoReturn(String sql, int timeOut) {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = connHelper.get();
            stmt = conn.createStatement();
            stmt.setQueryTimeout(timeOut);
            long startTime = System.currentTimeMillis();
            stmt.execute(sql);
            printlnSqlAndTime(sql, startTime);
        } catch (Exception e) {
            reThrow(e, sql);
        } finally {
            DbUtil.closeStatement(stmt);
            connHelper.release(conn);
        }
    }

    @Override
    public <T> T getSingleRecord(Class<T> clazz, String sql, Object... param) {
        return getSingleRecord(clazz, sql, queryTimeOut, param);
    }

    @Override
    public <T> T getSingleRecord(Class<T> clazz, String sql, int timeOut, Object... param) {
        List<T> list = getListBySQL(clazz, sql, param);
        if (list == null || list.isEmpty())
            return null;
        if (list.size() > 1)
            throw new DataAccessException("There are More than one Record in this ResultSet");
        return list.get(0);
    }
}
