package xyz.dongxiaoxia.commons.persistence.uya;

import xyz.dongxiaoxia.commons.utils.IDGen;

import java.util.Date;

/**
 * 公共实体类
 *
 * @author dongxiaoxia
 * @create 2016-07-09 0:11
 */
public abstract class DataEntity<T> extends BaseEntity<T> {
    private static final long serialVersionUID = -7663363397490170789L;

    protected String remarks;//备注
//    protected User createBy;//创建者
    protected Date createDate;//创建日期
//    protected User updateBy;//更新者
    protected Date updateDate;//更新日期
    protected String delFlag;//删除标识（0：正常 1：删除 2：审核）

    public DataEntity(){
        super();
        this.delFlag = DEL_FLAG_NORMAL;
    }

    public DataEntity(String id){
        super(id);
    }

    /**
     * 插入之前执行方法，需要手动调用
     */
    @Override
    public void preInsert() {
        //不限制ID为UUID，调用setIsNewRecord()使用自定义ID
        if (!this.isNewRecord){
            setId(IDGen.uuid());
        }
//        User user = UserUtils.getUser();
//        if (StringUtils.isNotBlank(user.getId())){
//            this.updateBy = user;
//            this.createBy = user;
//        }
        this.updateDate = new Date();
        this.createDate = this.updateDate;
    }

    /**
     * 更新之前执行方法，需要手动调用
     */
    @Override
    public void preUpdate() {
//        User user = UserUtils.getUser();
//        if (StringUtils.isNotBlank(user.getId())){
//            this.updateBy = user;
//        }
        this.updateDate = new Date();
    }

//    @Length(min=0, max=255)
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

//    @JsonIgnore
//    public User getCreateBy() {
//        return createBy;
//    }
//
//    public void setCreateBy(User createBy) {
//        this.createBy = createBy;
//    }

//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

//    @JsonIgnore
//    public User getUpdateBy() {
//        return updateBy;
//    }
//
//    public void setUpdateBy(User updateBy) {
//        this.updateBy = updateBy;
//    }

//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

//    @JsonIgnore
//    @Length(min=1, max=1)
    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

}
