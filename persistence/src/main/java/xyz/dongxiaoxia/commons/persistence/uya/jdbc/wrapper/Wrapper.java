package xyz.dongxiaoxia.commons.persistence.uya.jdbc.wrapper;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by dongxiaoxia on 2016/7/23.
 */
public interface Wrapper {

    <T> T conver(ResultSet resultSet, int index, Class<T> clazz) throws SQLException;

    <T> T wrapperSingleColumn2Object(ResultSet resultSet, Class<T> clazz) throws Exception;

    Object wrapperColumn2ObjectArray(ResultSet resultSet, Class<?>[] classes) throws Exception;

    <T> T wrapperColumn2Bean(ResultSet resultSet, List<String> columnNameList, List<Field> fieldList, Class<T> clazz) throws Exception;
}
