package com.hnyr.sys.data.entity;

import com.hnyr.sys.config.Constant;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName: BaseDto
 * @Description: dto 基类 （对应 BaseEntity）
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
@MappedSuperclass
public class BaseDto implements Serializable {
    private static final long serialVersionUID = 1L;
    //主键
    private Long id;
    //创建时间
    private Long createTime;
    //更新时间
    private Long updateTime = -1L;
    private Boolean deleted = false;
    private String createTimeStr;
    private String updateTimeStr;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public void setUpdateTimeStr(String updateTimeStr) {
        this.updateTimeStr = updateTimeStr;
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

    public String getUpdateTimeStr() {
        if (this.updateTime != null) {
            int length = String.valueOf(this.updateTime).length();
            if (length >= Constant.TIME_13) {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(this.updateTime));
            } else {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(this.updateTime * 1000));
            }
        }
        return updateTimeStr;
    }
}
