package com.hnyr.weixiudemo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 一般教师信息表
 * @TableName teacher
 */
@TableName(value ="teacher")
@Data
public class Teacher implements Serializable {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 工号
     */
    @TableField(value = "teacher_no")
    private String teacherNo;

    /**
     * 教师姓名
     */
    @TableField(value = "teacher_name")
    private String teacherName;

    /**
     * 联系方式（手机号）
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 登录密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * 校区ID(关联校区表)
     */
    @TableField(value = "campus_id")
    private Integer campusId;

    /**
     * 院系ID(关联院系表)
     */
    @TableField(value = "department_id")
    private Integer departmentId;

    /**
     * 年级
     */
    @TableField(value = "grade")
    private String grade;

    /**
     * 班级
     */
    @TableField(value = "class_name")
    private String className;

    /**
     * 辅导员ID
     */
    @TableField(value = "counselor_id")
    private Long counselorId;

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