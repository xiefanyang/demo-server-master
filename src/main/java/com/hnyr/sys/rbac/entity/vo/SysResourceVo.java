package com.hnyr.sys.rbac.entity.vo;

import lombok.Data;

import java.util.List;


@Data
public class SysResourceVo {
    private Long id;
    private Long parentId;
    private String code;
    private String name;
    private String icon = "";
    private String type;
    private Boolean outLink;
    private Boolean visible;
    private Integer purview;
    private Boolean openUse;
    private Boolean deleted;
    private Integer sort = 0;
    private String remark = "";
    private List<SysResourceVo> children;

    private Boolean open = Boolean.TRUE;
}
