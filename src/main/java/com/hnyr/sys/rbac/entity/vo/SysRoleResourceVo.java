package com.hnyr.sys.rbac.entity.vo;

import lombok.Data;

@Data
public class SysRoleResourceVo {
    private Long roleId;
    private Long resourceId;
    private Boolean enable;
}
