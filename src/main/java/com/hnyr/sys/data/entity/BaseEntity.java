package com.hnyr.sys.data.entity;

import com.hnyr.sys.config.Constant;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName: BaseEntity
 * @Description: entity 基类 （字典、常态、内置非用户日常业务数据记录可使用，不需要 recordId，使用 id）
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    //主键
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    //乐观锁
    @Version
    protected int version;

    //创建时间
    @Column(updatable = false)
    protected Long createTime;

    //更新时间
    protected Long updateTime = -1L;

    @Column(name = "IS_DELETED", nullable = false)
    protected Boolean deleted = false;

    @Transient
    private String createTimeStr;
    @Transient
    private String updateTimeStr;

    @PreUpdate
    public void preUpdate() {
        setUpdateTime(System.currentTimeMillis());
    }

    @PrePersist
    public void preAdd() {
        setCreateTime(System.currentTimeMillis());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getCreateTimeStr() {
        if (this.createTime != null) {
            int length = String.valueOf(this.createTime).length();
            if (length >= Constant.TIME_13) {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(this.createTime));
            } else {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(this.createTime * 1000));
            }
        }
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public String getUpdateTimeStr() {
        if (this.updateTime != null) {
            int length = String.valueOf(this.createTime).length();
            if (length >= Constant.TIME_13) {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(this.updateTime));
            } else {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(this.updateTime * 1000));
            }
        }
        return updateTimeStr;
    }

    public void setUpdateTimeStr(String updateTimeStr) {
        this.updateTimeStr = updateTimeStr;
    }
}
