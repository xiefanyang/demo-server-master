package com.hnyr.sys.rbac.entity.po;

import com.hnyr.sys.data.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @ClassName: SysDataPurviewDefine
 * @Description: 数据权限范围级别定义
 * @Author: demo
 * @CreateDate: 2023/10/9 17:02
 * @Version: 1.0
 */
@Entity
@Data
@Table(name = "t_sys_data_purview_define")
public class SysDataPurviewDefine extends BaseEntity {
    @Column(columnDefinition = "varchar(50) comment '权限范围名称'", nullable = false, unique = true)
    private String name;
    /**
     * 权限范围的 sql 配置，返回范围表的结果集合，格式： [{value:'',label:''}] 例如：
     * (1)非分级表，例如：新闻管理的模块（仅一级），select 模块ID as value, 模块名称 as label from 新闻模块表 where xxx；
     * (2)分级表，便于读取选用，需要拼装 name，暂不采用多级联动数据的方式返回，自行做好排序。如：xx 院系 xx 科教中心
     */
    @Column(columnDefinition = "text comment '权限范围SQL'", nullable = false)
    private String content;
    @Column(columnDefinition = "text comment '权限校验SQL'", nullable = false)
    private String valid;
    @Column(columnDefinition = "text comment '权限过滤限定SQL'", nullable = false)
    private String filter;
    @Column(columnDefinition = "varchar(30) comment 'value字段类型：Long String Integer'", nullable = false)
    private String valueType;
    @Column(columnDefinition = "bit COMMENT '是否有效 1有效 0无效' default 1", nullable = false)
    private Boolean enable;
    @Column(columnDefinition = "tinyint COMMENT '范围类型 0 默认 1 自定义' default  0", nullable = false)
    private Integer type;
}
