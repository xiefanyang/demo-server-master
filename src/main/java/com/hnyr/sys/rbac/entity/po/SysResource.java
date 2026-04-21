package com.hnyr.sys.rbac.entity.po;

import com.hnyr.sys.data.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "t_sys_resource")
public class SysResource extends BaseEntity {
    @Column(columnDefinition = "bigint COMMENT '父资源Id' default 0", nullable = false)
    private Long parentId;
    @Column(columnDefinition = "varchar(100) COMMENT '资源code（全局唯一）'", unique = true, nullable = false)
    private String code;
    @Column(columnDefinition = "varchar(30) COMMENT '资源名称'", nullable = false)
    private String name;
    @Column(columnDefinition = "varchar(255) COMMENT '图标' default ''", nullable = false)
    private String icon;
    @Column(columnDefinition = "varchar(20) COMMENT '资源类型' default '' ", nullable = false)
    private String type;
    @Column(columnDefinition = "bit COMMENT '是否为外链菜单' default 0", nullable = false)
    private Boolean outLink;
    @Column(columnDefinition = "bit COMMENT '是否可见' default 1", nullable = false)
    private Boolean visible;
    @Column(columnDefinition = "bit COMMENT '是否默认可用' default 1", nullable = false)
    private Boolean openUse;
    @Column(columnDefinition = "smallint COMMENT '排序' default 999", nullable = false)
    private Integer sort;
    @Column(columnDefinition = "varchar(255) COMMENT '描述' default ''", nullable = false)
    private String remark;
    @Column(columnDefinition = "smallint COMMENT '是否指定数据权限 0 不指定 1 指定' default 0", nullable = false)
    private Integer purview;
}
