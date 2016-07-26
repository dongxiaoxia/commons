package xyz.dongxiaoxia.commons.persistence.uya.jdbc.wrapper;

import java.sql.ResultSet;

/**
 * Created by dongxiaoxia on 2016/7/26.
 */
public class ArrayHandler implements ResultSetHandler<Object[]> {
    @Override
    public Object[] handle(ResultSet resultSet) {
        try {
            return new BeanWrapper().wrapperSingleColumn2toArray(resultSet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
