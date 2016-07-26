package xyz.dongxiaoxia.commons.persistence.uya.jdbc.wrapper;


import java.sql.ResultSet;
import java.util.List;

/**
 * Created by dongxiaoxia on 2016/7/25.
 */
public class ListHandler<T> implements ResultSetHandler<List<T>> {
    private Wrapper wrapper = new BeanWrapper();
    private final Class<T> type;

    public ListHandler(Class<T> type){
        this.type = type;
    }
    @Override
    public List<T> handle(ResultSet resultSet) {
        try {
            return wrapper.populateData(resultSet, type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
