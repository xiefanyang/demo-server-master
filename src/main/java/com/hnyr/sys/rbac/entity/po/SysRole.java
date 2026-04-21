package com.hnyr.sys.rbac.entity.po;

import com.hnyr.sys.data.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "t_sys_role", indexes = {@Index(columnList = "code")})
public class SysRole extends BaseEntity {
    @Column(columnDefinition = "bigint COMMENT '父角色Id' default 0", nullable = false)
    private Long parentId;
    @Column(columnDefinition = "varchar(100) COMMENT '编号'", nullable = false, unique = true)
    private String code;
    @Column(columnDefinition = "varchar(30) COMMENT '角色名称'", nullable = false)
    private String name;
    @Column(columnDefinition = "smallint COMMENT '排序' default 999", nullable = false)
    private Integer sort;
    @Column(columnDefinition = "varchar(255) COMMENT '描述' default ''", nullable = false)
    private String remark;
    @Column(columnDefinition = "smallint COMMENT '角色类型 0 否 1 是' default  0 ", nullable = false)
    private Integer sys;
    @Column(columnDefinition = "bit COMMENT '状态：1正常 0冻结' default 1 ", nullable = false)
    private Boolean enable;
    @Column(columnDefinition = "bit COMMENT '是否可被非管理用户授权 1 是 0 否' default 1 ", nullable = false)
    private Boolean bisShow;
}
