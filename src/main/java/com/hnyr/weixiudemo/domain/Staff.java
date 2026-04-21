package com.hnyr.weixiudemo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 工作人员表
 * @TableName staff
 */
@TableName(value ="staff")
@Data
public class Staff implements Serializable {
    /**
     * 工作人员主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 工号
     */
    @TableField(value = "staff_no")
    private String staffNo;

    /**
     * 姓名
     */
    @TableField(value = "staff_name")
    private String staffName;

    /**
     * 联系方式（手机号）
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 登录密码（加密存储）
     */
    @TableField(value = "password")
    private String password;

    /**
     * 年级（可空）
     */
    @TableField(value = "grade")
    private String grade;

    /**
     * 班级（可空）
     */
    @TableField(value = "class_name")
    private String className;

    /**
     * 辅导员ID（可空）
     */
    @TableField(value = "counselor_id")
    private Long counselorId;

    /**
     * 角色类型：系统管理员、指派教师、维修人员、普通人员
     */
    @TableField(value = "role_type")
    private Object roleType;

    /**
     * 状态
     */
    @TableField(value = "status")
    private Object status;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}