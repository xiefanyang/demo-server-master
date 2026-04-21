package com.hnyr.sys.rbac.entity.dto;

import com.hnyr.sys.data.entity.BaseDto;
import lombok.Data;

/**
 * @ClassName: SysDataPurviewDefineDto
 * @Description: 数据权限范围级别定义
 * @Author: demo
 * @CreateDate: 2023/10/9 17:02
 * @Version: 1.0
 */
@Data
public class SysDataPurviewDefineDto extends BaseDto {
    private String name;
    /**
     * 权限范围的 sql 配置，返回范围表的结果集合，格式： [{rid:'',name:''}] 例如：
     * (1)非分级表，例如：新闻管理的模块（仅一级），select 模块ID（最好不用自增 id） as rid, 模块名称 as name from 新闻模块表 where xxx；
     * (2)分级表，便于读取选用，需要拼装 name，暂不采用多级联动数据的方式返回，自行做好排序。如：xx 院系 xx 科教中心
     */
    private String content = "";
    private String valid = "";
    private String filter = "";
    private String valueType = "";
    private Boolean enable = Boolean.TRUE;
    private Integer type = 0;
}
