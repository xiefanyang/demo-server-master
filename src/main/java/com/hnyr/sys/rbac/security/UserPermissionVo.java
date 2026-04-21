package com.hnyr.sys.rbac.security;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserPermissionVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long userId;
    private List<String> roles;
    private List<String> resources;
    private Integer userStatus;
    private Integer userType;
    private Object f1;
    private Object f2;
    private Object f3;
    private Object f4;
    private Object f5;
}
