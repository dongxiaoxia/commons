package xyz.dongxiaoxia.commons.persistence.uya.jdbc.wrapper;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by dongxiaoxia on 2016/7/23.
 */
public interface Wrapper {

    <T> T convert(ResultSet resultSet, int index, Class<T> clazz) throws SQLException;

    <T> T wrapperSingleColumn2Object(ResultSet resultSet, Class<T> clazz) throws Exception;
    Object[] wrapperSingleColumn2toArray(ResultSet resultSet) throws Exception;
    <T> T wrapperSingleColumn2BeanList(ResultSet resultSet, Class<T> clazz) throws Exception;
    Map<String,Object> wrapperSingleColumn2Map(ResultSet resultSet) throws Exception;

    Object wrapperColumn2ObjectArray(ResultSet resultSet, Class<?>[] classes) throws Exception;

    <T> T wrapperColumn2Bean(ResultSet resultSet, List<String> columnNameList, List<Field> fieldList, Class<T> clazz) throws Exception;

    <T> List<T> populateData(ResultSet resultSet, Class<T> clazz) throws Exception;

    List<Object> populateData(ResultSet resultSet, Class<?>[] classes) throws Exception;
}
