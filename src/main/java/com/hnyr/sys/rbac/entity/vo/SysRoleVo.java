package com.hnyr.sys.rbac.entity.vo;

import lombok.Data;

@Data
public class SysRoleVo {
    private Long id;
    private Long parentId;
    private String name;
    private String code;
    private String pCode;
    private String remark = "";
    private String rootName;
    private String rootCode;
    private Integer sort;
    private Long rootId;
    private Integer sys;
    private Boolean enable;
    private Long parentRootId;
    private Boolean bisShow = Boolean.FALSE;
    private String key;
    private String text;
    private String title;
    private Long bid;
    private Boolean disableCheckbox;
    private Boolean selectable;

    private Boolean open = Boolean.TRUE;

    public String getKey() {
        return code;
    }

    public String getText() {
        return name;
    }

    public String getTitle() {
        return name;
    }

    public Long getBid() {
        return id;
    }
}
