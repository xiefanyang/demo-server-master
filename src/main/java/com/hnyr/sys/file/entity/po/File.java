package com.hnyr.sys.file.entity.po;

import com.hnyr.sys.data.entity.BaseBisEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "t_sys_file")
public class File extends BaseBisEntity {
    @Column(columnDefinition = "varchar(128) COMMENT '文件分类' default ''", nullable = false)
    private String category;
    @Column(columnDefinition = "varchar(128) COMMENT '原始文件名' default ''", nullable = false)
    private String originalName;
    @Column(columnDefinition = "varchar(128) COMMENT 'MIME文件类型' default ''", nullable = false)
    private String type;
    @Column(columnDefinition = "varchar(10) COMMENT '文件结尾' default ''", nullable = false)
    private String suffix;
    @Column(columnDefinition = "varchar(64) COMMENT '文件类型名称' default ''", nullable = false)
    private String typeName;
    @Column(columnDefinition = "bigint COMMENT '文件大小，存储默认单位为byte'", nullable = false)
    private Long size;
    @Column(columnDefinition = "bigint COMMENT '音频视频文件时长'")
    private Long fileTime;
    @Column(columnDefinition = "bit COMMENT '使用状态 1正常 2移除(回收站，没有清理文件)' default 1", nullable = false)
    private Boolean enable;
    @Column(columnDefinition = "bigint COMMENT '上传人'", nullable = false)
    private Long createUser;
    @Column(columnDefinition = "bigint COMMENT '删除人'")
    private Long deleteUser;
    @Column(columnDefinition = "bigint COMMENT '删除时间'")
    private Long deleteTime;
    @Column(columnDefinition = "bigint COMMENT '彻底删除人'")
    private Long clearUser;
    @Column(columnDefinition = "bigint COMMENT '彻底删除时间戳'")
    private Long clearTime;
    @Column(columnDefinition = "bit COMMENT '是否为公共读 1是0否' default 0", nullable = false)
    private Boolean publicRead;
}
