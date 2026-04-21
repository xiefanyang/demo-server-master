package com.hnyr.sys.rbac.entity.dto;

import com.hnyr.sys.data.entity.BaseDto;
import lombok.Data;

import java.util.List;

@Data
public class SysRoleDto extends BaseDto {
    private Long parentId;
    private String code;
    private String name;
    private Integer sort;
    private String remark = "";
    private Integer sys;
    private Boolean enable;
    private Boolean disabled;
    private Boolean bisShow = Boolean.FALSE;

    private Boolean open = Boolean.TRUE;
    private Boolean selected;
    private List<Long> resourcesIds;
    private List<SysRoleDto> children;

    private Boolean manageSelf = Boolean.TRUE;
}
