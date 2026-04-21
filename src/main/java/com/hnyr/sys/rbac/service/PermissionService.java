package com.hnyr.sys.rbac.service;

import com.hnyr.sys.rbac.security.UserPermissionVo;

import java.util.List;

public interface PermissionService {

    /**
     * 重新获取用户权限
     *
     * @param userId 用户ID
     * @return
     */
    UserPermissionVo renewAccountPermissionByUserId(Long userId);

    UserPermissionVo getSuperAdminPermission();

    UserPermissionVo getPermissionCache(Long userId);

    /**
     * @param userId
     * @return
     */
    List<String> getCurrentUserRoles(Long userId);

}
