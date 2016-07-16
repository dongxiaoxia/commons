package xyz.dongxiaoxia.commons.cache.mybatis;

/**
 * @author dongxiaoxia
 * @create 2016-07-08 11:45
 */
public interface Cache {
    String getId();

    void putObject(String key, Object value);

    Object getObject(String key);

    Object removeObject(String key);

    void clear();

    int getSize();

}
