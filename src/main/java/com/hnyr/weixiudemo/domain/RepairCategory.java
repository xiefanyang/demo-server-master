package com.hnyr.weixiudemo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 维修类别表
 * @TableName repair_category
 */
@TableName(value ="repair_category")
@Data
public class RepairCategory implements Serializable {
    /**
     * 维修类别主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 维修类别名称
     */
    @TableField(value = "repair_category_name")
    private String repairCategoryName;

    /**
     * 校区ID(关联校区表)
     */
    @TableField(value = "campus_id")
    private Integer campusId;

    /**
     * 维修类别描述
     */
    @TableField(value = "repair_category_description")
    private String repairCategoryDescription;

    /**
     * 启用状态
     */
    @TableField(value = "status")
    private Object status;

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