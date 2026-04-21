package com.hnyr.sys.rbac.service;

import java.util.List;

public interface SysRoleResourceService {
    Boolean changeGrant(Long resourceId, Long roleId, Boolean enable);

    List<Long> getResourceIdsByUserId(Long uid);

    List<Long> getResourceIdsByRoleId(Long roleId);
}
