package xyz.dongxiaoxia.commons.persistence.uya.jdbc.statementcreater;

import xyz.dongxiaoxia.commons.persistence.uya.jdbc.util.Common;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.util.OutSQL;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MysqlPSCreater extends PSCreaterBase {
    @Override
    public PreparedStatement createDeleteByCustom(Class<?> clazz,
                                                  Connection conn,
                                                  String condition,
                                                  OutSQL sql) throws Exception {

        StringBuilder sbSql = new StringBuilder("DELETE FROM ");
        sbSql.append(Common.getTableName(clazz));
        sbSql.append(" WHERE ");
        if (condition == null || "".equals(condition)) {
            condition = "1=2";
        }
        sbSql.append(condition);

        sql.setSql(sbSql.toString());
        sql.setRealSql(sbSql.toString());
        return conn.prepareStatement(sql.getSql());
    }

    @Override
    public PreparedStatement createUpdateByCustom(Class<?> clazz,
                                                  Connection conn,
                                                  String updateStatement,
                                                  String condition,
                                                  OutSQL sql)
            throws Exception {

        StringBuilder sbSql = new StringBuilder("UPDATE ");
        sbSql.append(Common.getTableName(clazz));
        sbSql.append(" SET ");
        sbSql.append(updateStatement);
        sbSql.append(" WHERE ");
        if (condition == null || condition.trim().equals("")) {
            condition = "1=2";
        }
        sbSql.append(condition);

        sql.setSql(sbSql.toString());
        sql.setRealSql(sbSql.toString());
        return conn.prepareStatement(sql.getSql());
    }

    @Override
    public PreparedStatement createUpdateEntity(Object bean, Connection conn, OutSQL sql)
            throws Exception {

        Class<?> clazz = bean.getClass();
        List<Field> idFields = Common.getIdFields(clazz);
        if (idFields.size() == 0) {
            throw new Exception("无法根据实体更新：主键不存在 ");
        }

        List<Field> listField = Common.getUpdatableFields(clazz);
        if (listField.size() > 0) {
            StringBuilder sbSql = new StringBuilder("UPDATE ");
            StringBuilder realSql = new StringBuilder("UPDATE ");
            sbSql.append(Common.getTableName(clazz));
            realSql.append(Common.getTableName(clazz));

            boolean isFirst = true;
            for (Field aListField : listField) {
                if (isFirst) {
                    sbSql.append(" SET ");
                } else {
                    sbSql.append(", ");
                }
                sbSql.append("`");
                sbSql.append(Common.getDBCloumnName(clazz, aListField));
                sbSql.append("`");
                sbSql.append("=?");
                isFirst = false;
            }

            sbSql.append(" WHERE ");
            isFirst = true;
            for (Field idField : idFields) {
                if (!isFirst) {
                    sbSql.append(" AND ");
                }
                sbSql.append("`");
                sbSql.append(Common.getDBCloumnName(clazz, idField));
                sbSql.append("`");
                sbSql.append("=?");
                isFirst = false;
            }

            sql.setSql(sbSql.toString());
            PreparedStatement ps = conn.prepareStatement(sql.getSql());

            int index = 1;
            for (int i = 0; i < listField.size(); i++) {
                Method m = Common.getGetterMethod(clazz, listField.get(i));
                Object value = m.invoke(bean, new Object[]{});
                Common.setPara(ps, value, index);
                index++;

                if (i > 0) {
                    realSql.append(", ");
                } else {
                    realSql.append(" SET ");
                }
                realSql.append("`");
                realSql.append(Common.getDBCloumnName(clazz, listField.get(i)));
                realSql.append("`");
                realSql.append("=");
                realSql.append(value);
            }

            realSql.append(" WHERE ");

            for (int i = 0; i < idFields.size(); i++) {
                Method m = Common.getGetterMethod(clazz, idFields.get(i));
                Object value = m.invoke(bean, new Object[]{});
                Common.setPara(ps, value, index);
                index++;

                if (i > 0) {
                    sbSql.append(" AND ");
                }
                realSql.append("`");
                realSql.append(Common.getDBCloumnName(clazz, idFields.get(i)));
                realSql.append("`");
                realSql.append("=");
                realSql.append(value);
            }
            sql.setRealSql(realSql.toString());
            return ps;

        } else {
            throw new Exception("表实体没有字段");
        }
    }

    @Override
    public PreparedStatement createInsert(Object bean, Connection conn, OutSQL sql)
            throws Exception {

        Class<?> clazz = bean.getClass();
        StringBuilder sbSql = new StringBuilder("INSERT INTO ");
        StringBuilder realSql = new StringBuilder("INSERT INTO ");

       //针对表结构相同、可以通过一个实体类动态修改表名
        String tableName = Common.getTableRename(clazz, bean);
        if (null == tableName || "".equals(tableName)) {
            tableName = Common.getTableName(clazz);
        }

        sbSql.append(tableName);
        sbSql.append("(");

        realSql.append(tableName);
        realSql.append("(");

        List<Field> listField = Common.getInsertableFields(clazz);

        StringBuilder sbColumn = new StringBuilder();
        StringBuilder sbValue = new StringBuilder();
        boolean isFirst = true;
        List<Object> values = new ArrayList<>();
        for (Field field : listField) {
            Method m = Common.getGetterMethod(clazz, field);
            Object value = m.invoke(bean, new Object[]{});
            if (value == null)
                continue;

            values.add(value);

            if (!isFirst) {
                sbColumn.append(", ");
                sbValue.append(", ");
            }
            sbColumn.append("`");
            sbColumn.append(Common.getDBCloumnName(clazz, field));
            sbColumn.append("`");


            sbValue.append("?");
            isFirst = false;
        }

        sbSql.append(sbColumn);
        sbSql.append(") VALUES (");
        sbSql.append(sbValue);
        sbSql.append(")");

        realSql.append(sbColumn);
        realSql.append(") VALUES (");

        sql.setSql(sbSql.toString());
        PreparedStatement ps = conn.prepareStatement(sql.getSql(), Statement.RETURN_GENERATED_KEYS);

        StringBuilder realValue = new StringBuilder();
        boolean isFirstRealValue = true;
        for (int i = 0; i < values.size(); i++) {
            Common.setPara(ps, values.get(i), i + 1);

            if (!isFirstRealValue) {
                realValue.append(", ");
            }
            realValue.append(values.get(i));
            isFirstRealValue = false;
        }
        realSql.append(realValue);
        realSql.append(")");
        sql.setRealSql(realSql.toString());

        return ps;
    }

    @Override
    public PreparedStatement createGetByCustom(Class<?> clazz,
                                               Connection conn,
                                               String columns,
                                               String condition,
                                               String orderBy,
                                               OutSQL sql) throws Exception {
        StringBuilder sbSql = new StringBuilder("SELECT ");
        if (columns == null || columns.trim().equals("")) {
            sbSql.append("*");
        } else {
            sbSql.append(columns);
        }
        sbSql.append(" FROM ");
        sbSql.append(Common.getTableName(clazz));
        if (condition != null && !condition.trim().equals("")) {
            sbSql.append(" WHERE ");
            sbSql.append(condition);
        }

        if (orderBy != null && !orderBy.trim().equals("")) {
            sbSql.append(" ORDER BY ");
            sbSql.append(orderBy);
        }

        sql.setSql(sbSql.toString());
        sql.setRealSql(sbSql.toString());
        return conn.prepareStatement(sql.getSql());
    }

    @Override
    public <I> PreparedStatement createGetEntity(Class<?> clazz,
                                                 Connection conn,
                                                 I id,
                                                 OutSQL sql) throws Exception {
        String idColumnName;
        List<Field> fieldList = Common.getIdFields(clazz);
        if (fieldList.size() != 1) {
            throw new Exception("无法根据主键ID获取数据：主键不存在 或 有两个以上的主键");
        } else {
            idColumnName = Common.getDBCloumnName(clazz, fieldList.get(0));
        }
        StringBuilder realSql = new StringBuilder();
        StringBuilder sbSql = new StringBuilder("SELECT * ");

        sbSql.append(" FROM ");
        sbSql.append(Common.getTableName(clazz));
        sbSql.append(" WHERE ");
        sbSql.append("`");
        sbSql.append(idColumnName);
        sbSql.append("`");
        realSql.append(sbSql.toString());
        sbSql.append("=?");
        realSql.append("=");
        realSql.append(id);

        sql.setSql(sbSql.toString());
        sql.setRealSql(realSql.toString());
        PreparedStatement ps = conn.prepareStatement(sql.getSql());
        Common.setPara(ps, id, 1);
        return ps;
    }


    @Override
    public PreparedStatement createGetByPage(Class<?> clazz,
                                             Connection conn,
                                             String condition,
                                             String columns,
                                             int start,
                                             int pageSize,
                                             String orderBy,
                                             OutSQL sql) throws Exception {

//		int offset = pageSize * (page - 1);

        StringBuilder sbSql = new StringBuilder("SELECT ");
        if (columns == null || columns.trim().equalsIgnoreCase("")) {
            sbSql.append("*");
        } else {
            sbSql.append(columns);
        }
        sbSql.append(" FROM ");
        sbSql.append(Common.getTableName(clazz));
        if (condition != null && !condition.equalsIgnoreCase("")) {
            sbSql.append(" WHERE ");
            sbSql.append(condition);
        }
        if (orderBy != null && !orderBy.equalsIgnoreCase("")) {
            sbSql.append(" ORDER BY ");
            sbSql.append(orderBy);
        }
        sbSql.append(" LIMIT ");
        sbSql.append(start);
        sbSql.append(",");
        sbSql.append(pageSize);

        sql.setSql(sbSql.toString());
        sql.setRealSql(sbSql.toString());
        return conn.prepareStatement(sql.getSql());
    }


    @Override
    public <I> PreparedStatement createDelete(Class<?> clazz,
                                              Connection conn,
                                              I id,
                                              OutSQL sql) throws Exception {
        String idColumnName ;
        List<Field> fieldList = Common.getIdFields(clazz);
        if (fieldList.size() != 1) {
            throw new Exception("无法根据主键删除：主键不存在 或 有两个以上的主键");
        } else {
            idColumnName = Common.getDBCloumnName(clazz, fieldList.get(0));
        }

        StringBuilder realSql = new StringBuilder();
        StringBuilder sbSql = new StringBuilder("DELETE FROM ");

        sbSql.append(Common.getTableName(clazz));
        sbSql.append(" WHERE ");
        sbSql.append("`");
        sbSql.append(idColumnName);
        sbSql.append("`");
        realSql.append(sbSql.toString());
        sbSql.append("=?");
        realSql.append("=");
        realSql.append(id);

        sql.setSql(sbSql.toString());
        sql.setRealSql(realSql.toString());
        PreparedStatement ps = conn.prepareStatement(sql.getSql());
        Common.setPara(ps, id, 1);
        return ps;
    }

    @Override
    public PreparedStatement createGetCount(Class<?> clazz,
                                            Connection conn,
                                            String condition,
                                            OutSQL sql) throws Exception {
        StringBuilder sbSql = new StringBuilder("SELECT COUNT(0) FROM ");
        sbSql.append(Common.getTableName(clazz));
        if (condition != null && !condition.trim().equals("")) {
            sbSql.append(" WHERE ");
            sbSql.append(condition);
        }

        sql.setSql(sbSql.toString());
        sql.setRealSql(sbSql.toString());
        return conn.prepareStatement(sql.getSql());
    }

    @Override
    public <I> PreparedStatement createDeleteByIDS(Class<?> clazz,
                                                   Connection conn, I[] ids, OutSQL sql) throws Exception {
        StringBuilder realSql = new StringBuilder();
        StringBuilder sbSql = new StringBuilder("DELETE FROM ");
        sbSql.append(Common.getTableName(clazz));
        sbSql.append(" WHERE ");

        List<Field> fieldList = Common.getIdFields(clazz);
        if (fieldList.size() != 1) {
            throw new Exception("无法根据主键ID删除数据：主键不存在 或 有两个以上的主键");
        } else {
            sbSql.append(Common.getDBCloumnName(clazz, fieldList.get(0)));
        }

        sbSql.append(" IN (");
        realSql.append(sbSql.toString());

        for (int i = 0; i < ids.length; i++) {
            if (i > 0) {
                sbSql.append(",");
            }
            sbSql.append("?");
        }
        sbSql.append(")");

        sql.setSql(sbSql.toString());
        PreparedStatement ps = conn.prepareStatement(sql.getSql());
        int index = 1;
        for (int i = 0; i < ids.length; i++, index++) {
            Common.setPara(ps, ids[i], index);
            if (i > 0) {
                realSql.append(",");
            }
            realSql.append(ids[i]);
        }

        realSql.append(")");
        sql.setRealSql(realSql.toString());

        return ps;
    }

    @Override
    public <I> PreparedStatement createUpdateByID(Class<?> clazz,
                                                  Connection conn,
                                                  String updateStatement,
                                                  I id,
                                                  OutSQL sql)
            throws Exception {

        String idName;
        List<Field> fieldList = Common.getIdFields(clazz);
        if (fieldList.size() != 1) {
            throw new Exception("无法根据主键ID删除数据：主键不存在 或 有两个以上的主键");
        } else {
            idName = Common.getDBCloumnName(clazz, fieldList.get(0));
        }

        StringBuilder realSql = new StringBuilder();
        StringBuilder sbSql = new StringBuilder("UPDATE ");
        sbSql.append(Common.getTableName(clazz));
        sbSql.append(" SET ");
        sbSql.append(updateStatement);
        sbSql.append(" WHERE ");
        sbSql.append(idName);
        realSql.append(sbSql.toString());
        sbSql.append("=?");
        realSql.append("=");
        realSql.append(id);

        sql.setSql(sbSql.toString());
        sql.setRealSql(realSql.toString());
        PreparedStatement ps = conn.prepareStatement(sql.getSql());
        Common.setPara(ps, id, 1);
        return ps;
    }
}