package xyz.dongxiaoxia.commons.persistence.dialect;

import java.io.Serializable;

/**
 * 基础实体类
 *
 * @author dongxiaoxia
 * @create 2016-07-07 18:28
 */
public abstract class BaseEntity<T> implements Serializable {

    private static final long serialVersionUID = 1282412827684784210L;
    /**
     * 实体编号（唯一标识）
     */
    protected String id;

    /**
     * 当前用户
     */
//    protected User currentUser;



}
