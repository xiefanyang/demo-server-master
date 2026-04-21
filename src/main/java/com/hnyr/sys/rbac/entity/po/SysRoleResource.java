package com.hnyr.sys.rbac.entity.po;

import com.hnyr.sys.data.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "t_sys_role_resource")
public class SysRoleResource extends BaseEntity {
    @Column(columnDefinition = "bigint COMMENT '角色Id'", nullable = false)
    private Long roleId;
    @Column(columnDefinition = "bigint COMMENT '资源菜单Id'", nullable = false)
    private Long resourceId;
    @Column(columnDefinition = "bit COMMENT '是否有效' default 1", nullable = false)
    private Boolean enable;
}
