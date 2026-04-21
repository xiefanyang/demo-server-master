package com.hnyr.sys.rbac.entity.po;

import com.hnyr.sys.data.entity.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "t_sys_user_data_purview")
@Accessors(chain = true)
public class SysUserDataPurview extends BaseEntity {
    @Column(columnDefinition = "bigint comment '授权级别ID'", nullable = false)
    private Long purviewId;

    @Column(columnDefinition = "text comment '授权数据对象范围id，以,分割'", nullable = false)
    private String ids;

    @Column(columnDefinition = "bigint COMMENT '账号'", nullable = false, updatable = false)
    private Long userId;
    @Column(columnDefinition = "bigint COMMENT '角色id'", nullable = false, updatable = false)
    private Long roleId;
    @Column(columnDefinition = "bigint COMMENT '资源id'", nullable = false, updatable = false)
    private Long resourceId;
    @Column(columnDefinition = "tinyint comment '状态（1正常0冻结）' default 1", nullable = false)
    private Boolean enable;
}
