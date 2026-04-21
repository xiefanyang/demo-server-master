package com.hnyr.sys.rbac.entity.po;

import com.hnyr.sys.data.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * @ClassName: SysUser
 * @Description: 系统用户基础账号表（业务系统自登录可手动创建、登录页面登录验证；统一身份 sso 可自动创建账号）
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
@Entity
@Table(name = "t_sys_user", uniqueConstraints = {@UniqueConstraint(columnNames = "username")})
@Data
public class SysUser extends BaseEntity {
    @Column(columnDefinition = "bigint COMMENT '创建人'")
    private Long creator;
    @Column(columnDefinition = "bigint COMMENT '最后更新人' default -1")
    private Long modifier;
    @Column(columnDefinition = "varchar(64) COMMENT '用户名'", nullable = false, unique = true)
    private String username;
    @Column(columnDefinition = "varchar(128) COMMENT '密码'", nullable = false)
    private String password;
    @Column(columnDefinition = "varchar(100) COMMENT '姓名' default ''", nullable = false)
    private String name;
    @Column(columnDefinition = "varchar(255) COMMENT '头像' default ''", nullable = false)
    private String avatar;
    @Column(columnDefinition = "tinyint COMMENT '用户状态 0正常1冻结' default 0", nullable = false)
    private Integer state;
    @Column(columnDefinition = "bigint COMMENT '密码更新时间' default 0 ", nullable = false)
    private Long updatePasswordTime;
    @Column(columnDefinition = "tinyint COMMENT '用户类型 1业务用户 9管理账号' default 1 ", nullable = false)
    private Integer type;
}
