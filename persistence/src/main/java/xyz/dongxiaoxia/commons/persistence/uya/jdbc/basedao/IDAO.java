package xyz.dongxiaoxia.commons.persistence.uya.jdbc.basedao;

import java.util.List;
import java.util.Map;

/**
 * @author dongxiaoxia
 * @create 2016-07-11 14:32
 */
interface IDAO {

    /**
     * insert a entity
     * @param t
     * @return
     * @throws Exception
     */
    Object insert(Object t) throws Exception ;

    Object[] insert(String sql, Object... param) throws Exception;

    /**
     * update a entity
     * @param t
     * @throws Exception
     */
    void upateEntity(Object t) throws Exception ;

    /**
     * update a entity
     * @param id
     * @throws Exception
     */
    <I> void upateByID(Class<?> clazz, String updateStatement, I id) throws Exception ;

    /**
     * update column by custom
     * @param clazz
     * @param updateStatement
     * @param condition
     * @throws Exception
     */
    void updateByCustom(Class<?> clazz, String updateStatement, String condition) throws Exception;

    /**
     * update column by custom
     * @param clazz
     * @param kv
     * @param condition
     * @throws Exception
     */
    void updateByCustom(Class<?> clazz, Map<String, Object> kv, Map<String, Object> condition) throws Exception;

    /**
     * delete record by id
     * @param <I>
     * @param clazz
     * @param id
     * @throws Exception
     */
    <I> void deleteByID(Class<?> clazz, I id) throws Exception;

    /**
     * delete record by id array
     * @param <I>
     * @param clazz
     * @param ids
     * @throws Exception
     */
    <I> void deleteByIDS(Class<?> clazz, I[] ids) throws Exception;

    /**
     * delete by custom
     * @param clazz
     * @param condition
     * @throws Exception
     */
    void deleteByCustom(Class<?> clazz, String condition) throws Exception;

    /**
     * delete by custom
     * @param clazz
     * @param condition
     * @throws Exception
     */
    void deleteByCustom(Class<?> clazz, Map<String, Object> condition) throws Exception;

    /**
     * 根据主键获得实体，需注意的是不论传入的是int还是string，都会转换成id=''的形式
     * 因为如果传的的是String，如果不是主键则有可能有多则数据，这个时间最好的是用getListByCustom方法
     * @param <I>
     * @param clazz
     * @param id
     * @return
     * @throws Exception
     */
    <I> Object get(Class<?> clazz, I id) throws Exception ;

    Object getByCustom(Class<?> clazz, String columns, String condition,String orderBy) throws Exception ;


    /**
     * get entity list by ids
     * @param <T>
     * @param <I>
     * @param clazz
     * @param ids
     * @return
     * @throws Exception
     */
    <T,I> List<T> getListByIDS(Class<T> clazz, I[] ids) throws Exception;

    /**
     * get entity list by custom
     * @param clazz
     * @param columns
     * @param condition
     * @param orderBy
     * @return
     * @throws Exception
     */
    List getListByCustom(Class<?> clazz, String columns, String condition, String orderBy) throws Exception;


    /**
     * get entity list by custom
     * @param clazz
     * @param columns
     * @param condition
     * @param orderBy
     * @return
     * @throws Exception
     */
    <T> List<T> getListByCustom(Class<T> clazz, String columns, Map<String, Object> condition, String orderBy) throws Exception;

    /**
     * get entity list by page
     * @param clazz
     * @param condition
     * @param columns
     * @param start
     * @param pageSize
     * @param orderBy
     * @return
     * @throws Exception
     */
    List getListByPage(Class<?> clazz, String condition, String columns, int start, int pageSize, String orderBy) throws Exception;

    /**
     * get entity list by page
     * @param clazz
     * @param condition
     * @param columns
     * @param start
     * @param pageSize
     * @param orderBy
     * @return
     * @throws Exception
     */
    <T> List<T> getListByPage(Class<T> clazz, Map<String, Object> condition, String columns, int start, int pageSize, String orderBy) throws Exception;

    /**
     * get record count
     * @param clazz
     * @param condition
     * @return
     * @throws Exception
     */
    int getCount(Class<?> clazz, String condition) throws Exception;

    /**
     * get record count
     * @param clazz
     * @param condition
     * @return
     * @throws Exception
     */
    int getCount(Class<?> clazz, Map<String, Object> condition) throws Exception;


    /**
     * insert a entity
     * @param t
     * @param timeOut
     * @return
     * @throws Exception
     */
    Object insert(Object t, int timeOut) throws Exception ;

    /**
     * update a entity
     * @param t
     * @param timeOut
     * @throws Exception
     */
    void upateEntity(Object t, int timeOut) throws Exception ;

    /**
     * update a entity
     * @param t
     * @param timeOut
     * @throws Exception
     */
    <I> void upateByID(Class<?> clazz, String updateStatement, I id, int timeOut) throws Exception ;

    /**
     * update by custom
     * @param clazz
     * @param updateStatement
     * @param condition
     * @param timeOut
     * @throws Exception
     */
    void updateByCustom(Class<?> clazz, String updateStatement, String condition, int timeOut) throws Exception;

    /**
     * update by custom
     * @param clazz
     * @param updateStatement
     * @param condition
     * @param timeOut
     * @throws Exception
     */
    void updateByCustom(Class<?> clazz, Map<String, Object> kv, Map<String, Object> condition, int timeOut) throws Exception;

    /**
     * delete record by id
     * @param <I>
     * @param clazz
     * @param id
     * @param timeOut
     * @throws Exception
     */
    <I> void deleteByID(Class<?> clazz, I id, int timeOut) throws Exception;

    /**
     * delete record by id array
     * @param <I>
     * @param clazz
     * @param id
     * @param timeOut
     * @throws Exception
     */
    <I> void deleteByIDS(Class<?> clazz, I[] ids, int timeOut) throws Exception;

    /**
     * delete records by custom
     * @param clazz
     * @param condition
     * @param timeOut
     * @throws Exception
     */
    void deleteByCustom(Class<?> clazz, String condition, int timeOut) throws Exception;

    /**
     * delete records by custom
     * @param clazz
     * @param condition
     * @param timeOut
     * @throws Exception
     */
    void deleteByCustom(Class<?> clazz, Map<String, Object> condition, int timeOut) throws Exception;

    /**
     * get entity
     * @param <I>
     * @param clazz
     * @param id
     * @param timeOut
     * @return
     * @throws Exception
     */
    <I> Object get(Class<?> clazz, I id, int timeOut) throws Exception ;

    /**
     * get entity list by ids
     * @param <T>
     * @param <I>
     * @param clazz
     * @param ids
     * @param timeOut
     * @return
     * @throws Exception
     */
    <T,I> List<T> getListByIDS(Class<T> clazz, I[] ids, int timeOut) throws Exception;

    /**
     * get entity list by custom
     * @param clazz
     * @param columns
     * @param condition
     * @param orderBy
     * @param timeOut
     * @return
     * @throws Exception
     */
    List getListByCustom(Class<?> clazz, String columns, String condition,String orderBy, int timeOut) throws Exception;

    /**
     * get entity list by custom
     * @param clazz
     * @param columns
     * @param condition
     * @param orderBy
     * @param timeOut
     * @return
     * @throws Exception
     */
    <T> List<T> getListByCustom(Class<T> clazz, String columns, Map<String, Object> condition,String orderBy, int timeOut) throws Exception;

    /**
     * get entity list by page
     * @param clazz
     * @param condition
     * @param columns
     * @param page
     * @param pageSize
     * @param orderBy
     * @param timeOut
     * @return
     * @throws Exception
     */
    List getListByPage(Class<?> clazz, String condition, String columns, int page, int pageSize, String orderBy, int timeOut) throws Exception;

    /**
     * get entity list by page
     * @param clazz
     * @param condition
     * @param columns
     * @param page
     * @param pageSize
     * @param orderBy
     * @param timeOut
     * @return
     * @throws Exception
     */
    <T> List<T> getListByPage(Class<T> clazz, Map<String, Object> condition, String columns, int page, int pageSize, String orderBy, int timeOut) throws Exception;

    /**
     * get record count
     * @param clazz
     * @param condition
     * @param timeOut
     * @return
     * @throws Exception
     */
    int getCount(Class<?> clazz, String condition, int timeOut) throws Exception;

    /**
     * get record count
     * @param clazz
     * @param condition
     * @param timeOut
     * @return
     * @throws Exception
     */
    int getCount(Class<?> clazz, Map<String, Object> condition, int timeOut) throws Exception;

    <T> List<T> getListBySQL(Class<T> clazz,String sql, Object... param) throws Exception;

    List<Object> getListBySQL(Class<?>[] classes,String sql, Object... param) throws Exception;

    List<Object> getListBySQL(Class<?>[] classes,String sql, int timeOut, Object... param) throws Exception;

    <T> List<T> getListBySQL(Class<T> clazz,String sql, int timeOut, Object... param) throws Exception;

    int execBySQL(String sql, Object... param) throws Exception;

    int execBySQL(String sql, int timeOut, Object... param) throws Exception;

    int[] execBatchSql(String sql, List<Object[]> paramList, boolean useTransaction) throws Exception;

    int getCountBySQL(String sql, Object... param) throws Exception;

    int getCountBySQL(String sql, int timeOut, Object... param) throws Exception;

    <T> T getSingleRecord(Class<T> clazz, String sql, Object... param) throws Exception;

    <T> T getSingleRecord(Class<T> clazz, String sql,int timeOut, Object...param) throws Exception;

    @Deprecated
    List<Object[]> customSql(String sql, int columnCount) throws Exception;

    @Deprecated
    void customSqlNoReturn(String sql) throws Exception;

    @Deprecated
    List<Object[]> customSql(String sql, int columnCount, int timeOut) throws Exception;

    @Deprecated
    void customSqlNoReturn(String sql, int timeOut) throws Exception;
}
