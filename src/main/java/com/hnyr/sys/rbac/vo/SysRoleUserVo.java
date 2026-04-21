package com.hnyr.sys.rbac.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysRoleUserVo implements Serializable {
    private Long userId;
    private Long roleId;
    private Boolean enable;
    private Boolean sys;
    private String roleCode;
    private String username;
    private String name;
    private String department;
    private String idNumber;
    private String gender;
    private String mobile;
    private String email;
    /**
     * 是否有需要指定权限的资源
     */
    private Boolean hasPurview = Boolean.FALSE;
    /**
     * 是否有未设置指定权限的资源（有需要指定的时候获取）
     */
    private Boolean notSetPurview = Boolean.FALSE;
}
