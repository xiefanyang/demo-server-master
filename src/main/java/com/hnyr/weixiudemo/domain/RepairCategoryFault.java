package com.hnyr.weixiudemo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 维修类别与故障类别关联表
 * @TableName repair_category_fault
 */
@TableName(value ="repair_category_fault")
@Data
public class RepairCategoryFault implements Serializable {
    /**
     * 关联关系主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 维修类别ID
     */
    @TableField(value = "repair_category_id")
    private Long repairCategoryId;

    /**
     * 故障类别ID
     */
    @TableField(value = "fault_category_id")
    private Long faultCategoryId;

    /**
     * 创建时间
     */
    @TableField(value = "created_time")
    private Date createdTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}