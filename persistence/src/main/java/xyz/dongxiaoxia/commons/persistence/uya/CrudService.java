package xyz.dongxiaoxia.commons.persistence.uya;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * service基类
 *
 * @author dongxiaoxia
 * @create 2016-07-09 23:36
 */
public abstract class CrudService<D extends CrudDao<T>,T extends DataEntity<T>> {

    /**
     * 持久层对象
     */
    @Autowired
    protected D dao;

    /**
     * 获取单条数据
     * @param id
     * @return
     */
    public T get(String id){
        return dao.get(id);
    }

    /**
     * 获取单条数据
     * @param entity
     * @return
     */
    public T get(T entity){
        return dao.get(entity);
    }

    /**
     * 查询列表数据
     * @param entity
     * @return
     */
    public List<T> findList(T entity){
        return dao.findList(entity);
    }

    /**
     * 查询分页数据
     * @param page
     * @param entity
     * @return
     */
    public Page<T> findPage(Page<T> page,T entity){
        entity.setPage(page);
        page.setList(dao.findList(entity));
        return page;
    }

    /**
     * 查询所有分页数据
     * @param page
     * @param entity
     * @return
     */
    public Page<T> findAllPage(Page<T> page,T entity){
        entity.setPage(page);
        page.setList(dao.findAllList(entity));
        return page;
    }

    /**
     * 保存数据（插入或更新）
     * @param entity
     */
    public void save(T entity){
        if (entity.getIsNewRecord()){
            entity.preInsert();
            dao.insert(entity);
        }else{
            entity.preUpdate();
            dao.update(entity);
        }
    }

    /**
     * 删除数据
     * @param entity
     */
    public void delete(T entity){
        dao.delete(entity);
    }

    /**
     * 更新数据
     * @param entity
     */
    public void update(T entity) {
        entity.preUpdate();
        dao.update(entity);
    }
}