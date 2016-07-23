package xyz.dongxiaoxia.commons.persistence.uya.jdbc.wrapper;

import xyz.dongxiaoxia.commons.persistence.uya.jdbc.util.Common;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by dongxiaoxia on 2016/7/23.
 */
public class BeanWrapper implements Wrapper {
    public <T> T conver(ResultSet resultSet, int index, Class<T> clazz) throws SQLException {
        Object value;
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

    public <T> T wrapperSingleColumn2Object(ResultSet resultSet, Class<T> clazz) throws Exception {
        return conver(resultSet, 1, clazz);
    }

    public Object wrapperColumn2ObjectArray(ResultSet resultSet, Class<?>[] classes) throws Exception {
        int count = classes.length;
        Object[] objects = new Object[count];
        while (count > 0) {
            Class<?> clazz = classes[count - 1];
            Object object = conver(resultSet, count, clazz);
            objects[--count] = object;
        }
        return objects;
    }

    public <T> T wrapperColumn2Bean(ResultSet resultSet, List<String> columnNameList, List<Field> fieldList, Class<T> clazz) throws Exception {
        T bean = clazz.newInstance();
        for (Field f : fieldList) {
            String columnName = Common.getDBCloumnName(clazz, f).toLowerCase();
            if (columnNameList.contains(columnName)) {
                Object columnValueObj;
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
                    setterMethod.invoke(bean, columnValueObj);
                }
            }
        }
        return bean;
    }
}
