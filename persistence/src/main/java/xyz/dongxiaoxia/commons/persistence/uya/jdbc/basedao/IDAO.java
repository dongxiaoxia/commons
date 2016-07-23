package xyz.dongxiaoxia.commons.persistence.uya.jdbc.basedao;

import java.util.List;
import java.util.Map;

/**
 * @author dongxiaoxia
 * @create 2016-07-11 14:32
 */
public interface IDAO {

    /**
     * insert a entity
     *
     * @param entity 实体对象
     * @return 实体对象
     */
    Object insert(Object entity);

    /**
     * insert a entity
     *
     * @param entity  实体对象
     * @param timeOut 超时时间
     * @return 实体对象
     */
    Object insert(Object entity, int timeOut);

    Object[] insert(String sql, Object... param);

    /**
     * update a entity
     *
     * @param entity
     */
    void updateEntity(Object entity);

    /**
     * update a entity
     *
     * @param entity 实体对象
     * @param timeOut 超时时间
     */
    void updateEntity(Object entity, int timeOut);

    /**
     * update a entity
     *
     * @param id
     */
    <I> void updateByID(Class<?> clazz, String updateStatement, I id);

    /**
     * update a entity
     *
     * @param timeOut 超时时间
     */
    <I> void updateByID(Class<?> clazz, String updateStatement, I id, int timeOut);

    /**
     * update column by custom
     *
     * @param clazz
     * @param updateStatement
     * @param condition
     */
    void updateByCustom(Class<?> clazz, String updateStatement, String condition);

    /**
     * update by custom
     *
     * @param clazz
     * @param updateStatement
     * @param condition
     * @param timeOut
     */
    void updateByCustom(Class<?> clazz, String updateStatement, String condition, int timeOut);

    /**
     * update column by custom
     *
     * @param clazz
     * @param kv
     * @param condition
     */
    void updateByCustom(Class<?> clazz, Map<String, Object> kv, Map<String, Object> condition);

    /**
     * update by custom
     *
     * @param clazz
     * @param condition
     * @param timeOut
     */
    void updateByCustom(Class<?> clazz, Map<String, Object> kv, Map<String, Object> condition, int timeOut);

    /**
     * delete record by id
     *
     * @param <I>
     * @param clazz
     * @param id
     */
    <I> void deleteByID(Class<?> clazz, I id);

    /**
     * delete record by id
     *
     * @param <I>
     * @param clazz
     * @param id
     * @param timeOut
     */
    <I> void deleteByID(Class<?> clazz, I id, int timeOut);

    /**
     * delete record by id array
     *
     * @param <I>
     * @param clazz
     * @param ids
     */
    <I> void deleteByIDS(Class<?> clazz, I[] ids);

    /**
     * delete record by id array
     *
     * @param <I>
     * @param clazz
     * @param ids
     * @param timeOut
     */
    <I> void deleteByIDS(Class<?> clazz, I[] ids, int timeOut);

    /**
     * delete by custom
     *
     * @param clazz
     * @param condition
     */
    void deleteByCustom(Class<?> clazz, String condition);

    /**
     * delete records by custom
     *
     * @param clazz
     * @param condition
     * @param timeOut
     */
    void deleteByCustom(Class<?> clazz, String condition, int timeOut);

    /**
     * delete by custom
     *
     * @param clazz
     * @param condition
     */
    void deleteByCustom(Class<?> clazz, Map<String, Object> condition);

    /**
     * 根据主键获得实体，需注意的是不论传入的是int还是string，都会转换成id=''的形式
     * 因为如果传的的是String，如果不是主键则有可能有多则数据，这个时间最好的是用getListByCustom方法
     *
     * @param <I>
     * @param clazz
     * @param id
     * @return
     */
    <I> Object get(Class<?> clazz, I id);

    /**
     * get entity
     *
     * @param <I>
     * @param clazz
     * @param id
     * @param timeOut
     * @return
     */
    <I> Object get(Class<?> clazz, I id, int timeOut);

    Object getByCustom(Class<?> clazz, String columns, String condition, String orderBy);


    /**
     * get entity list by ids
     *
     * @param <T>
     * @param <I>
     * @param clazz
     * @param ids
     * @return
     */
    <T, I> List<T> getListByIDS(Class<T> clazz, I[] ids);

    /**
     * get entity list by custom
     *
     * @param clazz
     * @param columns
     * @param condition
     * @param orderBy
     * @return
     */
    List getListByCustom(Class<?> clazz, String columns, String condition, String orderBy);


    /**
     * get entity list by custom
     *
     * @param clazz
     * @param columns
     * @param condition
     * @param orderBy
     * @return
     */
    <T> List<T> getListByCustom(Class<T> clazz, String columns, Map<String, Object> condition, String orderBy);

    /**
     * get entity list by page
     *
     * @param clazz
     * @param condition
     * @param columns
     * @param start
     * @param pageSize
     * @param orderBy
     * @return
     */
    List getListByPage(Class<?> clazz, String condition, String columns, int start, int pageSize, String orderBy);

    /**
     * get entity list by page
     *
     * @param clazz
     * @param condition
     * @param columns
     * @param start
     * @param pageSize
     * @param orderBy
     * @return
     */
    <T> List<T> getListByPage(Class<T> clazz, Map<String, Object> condition, String columns, int start, int pageSize, String orderBy);

    /**
     * get record count
     *
     * @param clazz
     * @param condition
     * @return
     */
    int getCount(Class<?> clazz, String condition);

    /**
     * get record count
     *
     * @param clazz
     * @param condition
     * @return
     */
    int getCount(Class<?> clazz, Map<String, Object> condition);


    /**
     * delete records by custom
     *
     * @param clazz
     * @param condition
     * @param timeOut
     */
    void deleteByCustom(Class<?> clazz, Map<String, Object> condition, int timeOut);


    /**
     * get entity list by ids
     *
     * @param <T>
     * @param <I>
     * @param clazz
     * @param ids
     * @param timeOut
     * @return
     */
    <T, I> List<T> getListByIDS(Class<T> clazz, I[] ids, int timeOut);

    /**
     * get entity list by custom
     *
     * @param clazz
     * @param columns
     * @param condition
     * @param orderBy
     * @param timeOut
     * @return
     */
    List getListByCustom(Class<?> clazz, String columns, String condition, String orderBy, int timeOut);

    /**
     * get entity list by custom
     *
     * @param clazz
     * @param columns
     * @param condition
     * @param orderBy
     * @param timeOut
     * @return
     */
    <T> List<T> getListByCustom(Class<T> clazz, String columns, Map<String, Object> condition, String orderBy, int timeOut);

    /**
     * get entity list by page
     *
     * @param clazz
     * @param condition
     * @param columns
     * @param page
     * @param pageSize
     * @param orderBy
     * @param timeOut
     * @return
     */
    List getListByPage(Class<?> clazz, String condition, String columns, int page, int pageSize, String orderBy, int timeOut);

    /**
     * get entity list by page
     *
     * @param clazz
     * @param condition
     * @param columns
     * @param page
     * @param pageSize
     * @param orderBy
     * @param timeOut
     * @return
     */
    <T> List<T> getListByPage(Class<T> clazz, Map<String, Object> condition, String columns, int page, int pageSize, String orderBy, int timeOut);

    /**
     * get record count
     *
     * @param clazz
     * @param condition
     * @param timeOut
     * @return
     */
    int getCount(Class<?> clazz, String condition, int timeOut);

    /**
     * get record count
     *
     * @param clazz
     * @param condition
     * @param timeOut
     * @return
     */
    int getCount(Class<?> clazz, Map<String, Object> condition, int timeOut);

    <T> List<T> getListBySQL(Class<T> clazz, String sql, Object... param);

    List<Object> getListBySQL(Class<?>[] classes, String sql, Object... param);

    List<Object> getListBySQL(Class<?>[] classes, String sql, int timeOut, Object... param);

    <T> List<T> getListBySQL(Class<T> clazz, String sql, int timeOut, Object... param);

    int execBySQL(String sql, Object... param);

    int execBySQL(String sql, int timeOut, Object... param);

    int[] execBatchSql(String sql, List<Object[]> paramList, boolean useTransaction);

    int getCountBySQL(String sql, Object... param);

    int getCountBySQL(String sql, int timeOut, Object... param);

    <T> T getSingleRecord(Class<T> clazz, String sql, Object... param);

    <T> T getSingleRecord(Class<T> clazz, String sql, int timeOut, Object... param);

    @Deprecated
    List<Object[]> customSql(String sql, int columnCount);

    @Deprecated
    void customSqlNoReturn(String sql);

    @Deprecated
    List<Object[]> customSql(String sql, int columnCount, int timeOut);

    @Deprecated
    void customSqlNoReturn(String sql, int timeOut);
}
