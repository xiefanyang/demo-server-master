package com.hnyr.sys.rbac.entity.dto;

import lombok.Data;

@Data
public class SysRoleUserDto {
    private Long userId;
    private Long roleId;
    private Boolean enable;
    private String roleCode;
    private String username;
    private String name;
    private String department;
}
