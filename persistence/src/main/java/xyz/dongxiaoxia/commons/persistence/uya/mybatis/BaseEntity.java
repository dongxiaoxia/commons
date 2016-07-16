package xyz.dongxiaoxia.commons.persistence.uya.mybatis;

import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 基础实体类
 *
 * @author dongxiaoxia
 * @create 2016-07-08 23:52
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

    /**
     * 当前实体分页对象
     */
    protected Page<T> page;

    /**
     * 自定义SQL（SQL标识，SQL内容）
     */
    protected Map<String, String> sqlMap;

    /**
     * 是否为新纪录（默认：false），调用setIsNewRecord()设置新纪录，使用自定义ID。
     * 设置为true后强制执行插入语句，ID不会自动生成，需要从手动传入。
     */
    protected boolean isNewRecord = false;

    /**
     * 用于前端表单提供搜索多字段
     */
    protected String searchStr;

    public BaseEntity() {
    }

    public BaseEntity(String id) {
        this();
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

//    @JsonIgnore
//    @XmlTransient
//    public User getCurrentUser() {
//        if(currentUser == null){
//            currentUser = UserUtils.getUser();
//        }
//        return currentUser;
//    }
//
//    public void setCurrentUser(User currentUser) {
//        this.currentUser = currentUser;
//    }

//    @JsonIgnore
//    @XmlTransient
    public Page<T> getPage() {
        if (page == null){
            page = new Page<T>();
        }
        return page;
    }

    public Page<T> setPage(Page<T> page) {
        this.page = page;
        return page;
    }

//    @JsonIgnore
//    @XmlTransient
    public Map<String, String> getSqlMap() {
        if (sqlMap == null){
            sqlMap = new HashMap<String, String>();
        }
        return sqlMap;
    }

    public void setSqlMap(Map<String, String> sqlMap) {
        this.sqlMap = sqlMap;
    }

    /**
     * 插入之前执行方法，子类实现
     */
    public abstract void preInsert();

    /**
     * 更新之前执行方法，子类实现
     */
    public abstract void preUpdate();

    /**
     * 是否是新记录（默认：false），调用setIsNewRecord()设置新记录，使用自定义ID。
     * 设置为true后强制执行插入语句，ID不会自动生成，需从手动传入。
     * @return
     */
    public boolean getIsNewRecord() {
        return isNewRecord || StringUtils.isEmpty(getId());
    }

    /**
     * 是否是新记录（默认：false），调用setIsNewRecord()设置新记录，使用自定义ID。
     * 设置为true后强制执行插入语句，ID不会自动生成，需从手动传入。
     */
    public void setIsNewRecord(boolean isNewRecord) {
        this.isNewRecord = isNewRecord;
    }

    /**
     * 全局变量对象
     */
//    @JsonIgnore
//    public Global getGlobal() {
//        return Global.getInstance();
//    }

    /**
     * 获取数据库名称
     */
//    @JsonIgnore
//    public String getDbName(){
//        return Global.getConfig("jdbc.type");
//    }

//    @JsonIgnore
    public String getSearchStr() {
        return searchStr;
    }

    public void setSearchStr(String searchStr) {
        this.searchStr = searchStr;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!getClass().equals(obj.getClass())) {
            return false;
        }
        BaseEntity<?> that = (BaseEntity<?>) obj;
        return null == this.getId() ? false : this.getId().equals(that.getId());
    }
//
//    @Override
//    public String toString() {
//        return ReflectionToStringBuilder.toString(this);
//    }

    /**
     * 删除标记（0：正常；1：删除；2：审核；）
     */
    public static final String DEL_FLAG_NORMAL = "0";
    public static final String DEL_FLAG_DELETE = "1";
    public static final String DEL_FLAG_AUDIT = "2";

}
