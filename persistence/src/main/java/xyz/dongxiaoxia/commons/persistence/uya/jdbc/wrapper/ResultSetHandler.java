package xyz.dongxiaoxia.commons.persistence.uya.jdbc.wrapper;

import java.sql.ResultSet;

/**
 * 数据库结果集处理接口
 * <p>
 * Created by dongxiaoxia on 2016/7/25.
 */
public interface ResultSetHandler<T> {

    T handle(ResultSet resultSet);
}
