package com.hnyr.sys.rbac.entity.po;

import com.hnyr.sys.data.entity.BaseEntity;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "t_sys_role_user", uniqueConstraints = {@UniqueConstraint(columnNames = {"userId", "roleId"})}, indexes = {@Index(columnList = "userId"),})
public class SysRoleUser extends BaseEntity {
    @Column(columnDefinition = "bigint COMMENT '用户Id'", nullable = false)
    private Long userId;
    @Column(columnDefinition = "bigint COMMENT '角色Id'", nullable = false)
    private Long roleId;
    @Column(columnDefinition = "bit COMMENT '是否有效 1有效 0无效' default 1", nullable = false)
    private Boolean enable;
}
