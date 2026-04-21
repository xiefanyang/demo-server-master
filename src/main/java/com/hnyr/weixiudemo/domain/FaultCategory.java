package com.hnyr.weixiudemo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 故障类别表
 * @TableName fault_category
 */
@TableName(value ="fault_category")
@Data
public class FaultCategory implements Serializable {
    /**
     * 故障类别主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 故障类别名称
     */
    @TableField(value = "fault_category_name")
    private String faultCategoryName;

    /**
     * 故障类别描述
     */
    @TableField(value = "fault_category_description")
    private String faultCategoryDescription;

    /**
     * 启用状态
     */
    @TableField(value = "status")
    private Object status;

    /**
     * 评分期限，单位小时
     */
    @TableField(value = "default_score_deadline_hours")
    private Integer defaultScoreDeadlineHours;

    /**
     * 创建人用户ID
     */
    @TableField(value = "created_by")
    private String createdBy;

    /**
     * 创建时间
     */
    @TableField(value = "created_time")
    private Date createdTime;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time")
    private Date updatedTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}