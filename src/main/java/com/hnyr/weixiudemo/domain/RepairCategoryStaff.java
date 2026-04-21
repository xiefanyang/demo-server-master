package com.hnyr.weixiudemo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 维修类别与工作人员关联表（多对多）
 * @TableName repair_category_staff
 */
@TableName(value ="repair_category_staff")
@Data
public class RepairCategoryStaff implements Serializable {
    /**
     * 关联主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 维修类别ID
     */
    @TableField(value = "repair_category_id")
    private Long repairCategoryId;

    /**
     * 工作人员ID（关联 staff 表）
     */
    @TableField(value = "staff_id")
    private Long staffId;

    /**
     * 创建时间
     */
    @TableField(value = "created_time")
    private Date createdTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}