package com.hnyr.weixiudemo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 院系表
 * @TableName department
 */
@TableName(value ="department")
@Data
public class Department implements Serializable {
    /**
     * 主键ID（自增）
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 院系名称
     */
    @TableField(value = "dept_name")
    private String deptName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}