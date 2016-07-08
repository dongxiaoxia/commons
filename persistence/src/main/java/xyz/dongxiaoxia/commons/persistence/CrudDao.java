package xyz.dongxiaoxia.commons.persistence;

import java.util.List;

/**
 * @author dongxiaoxia
 * @create 2016-07-07 15:22
 */
public interface CrudDao<T> extends BaseDao {

    /**
     * 获取单挑记录
     *
     * @param id
     * @return
     */
    T get(String id);

    /**
     * 获取单挑记录
     *
     * @param entity
     * @return
     */
    T get(T entity);

    /**
     * 查询数据列表，如果需要分页，请设置分页对象，如entity.setPage(new Page<T>());
     *
     * @param entity
     * @return
     */
    List<T> findList(T entity);

    /**
     * 查询所有数据列表
     *
     * @param entity
     * @return
     */
    List<T> findAllList(T entity);

    /**
     * 查询所有数据列表
     *
     * @return
     * @see List<T> findAllList(T entity)
     */
    @Deprecated
    List<T> findAllList();

    /**
     * 插入数据
     *
     * @param entity
     * @return
     */
    int insert(T entity);

    /**
     * 更新数据
     *
     * @param entity
     * @return
     */
    int update(T entity);

    /**
     * 删除数据（一般为逻辑删除，更新del_flag字段为1）
     *
     * @param id
     * @return
     * @see int delete(T entity)
     */
    @Deprecated
    int delete(String id);

    int delete(T entity);
}
