package com.hnyr.sys.data.entity;

import com.hnyr.sys.data.EnumStatus;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName: BaseDoc
 * @Description: mongodb 数据对象 基类
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
@MappedSuperclass
@Data
public class BaseDoc implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String id;

    /**
     * 删除标志
     */
    private Boolean deleted = Boolean.FALSE;
    /**
     * 状态 0草稿 1正常 2冻结
     */
    private Integer status = EnumStatus.STATUS_ENABLE.getStatus();
    /**
     * 创建人
     */
    private Long creator = 0L;
    /**
     * 最后修改人
     */
    private Long modifier = 0L;
    /**
     * 创建时间
     */
    private Long createTime = 0L;
    /**
     * 最后修改时间
     */
    private Long updateTime = 0L;

    @Transient
    private String createTimeStr;
    @Transient
    private String updateTimeStr;
    @Transient
    private String statusStr;

    public String getStatusStr() {
        return EnumStatus.getStatusDesc(status);
    }

    public String getCreateTimeStr() {
        if (this.createTime != null && this.createTime > 0) {
            int length = String.valueOf(this.createTime).length();
            return length >= 13 ? (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date(this.createTime)) : (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date(this.createTime * 1000L));
        } else {
            return this.createTimeStr;
        }
    }

    public String getUpdateTimeStr() {
        if (this.updateTime != null && this.updateTime > 0) {
            int length = String.valueOf(this.updateTime).length();
            return length >= 13 ? (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date(this.updateTime)) : (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date(this.updateTime * 1000L));
        } else {
            return this.updateTimeStr;
        }
    }
}
