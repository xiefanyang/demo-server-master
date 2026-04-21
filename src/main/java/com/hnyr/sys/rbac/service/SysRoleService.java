package com.hnyr.sys.rbac.service;

import com.hnyr.sys.rbac.entity.po.SysRole;
import com.hnyr.sys.rbac.entity.vo.SysRoleVo;

public interface SysRoleService {
    /**
     * 获取全部角色
     *
     * @return
     */
//    List<SysRole> getTreeAllRoles();
//
//    List<SysRole> getTreeRolesInIds(List<Long> roleIds);

    SysRole getRole(Long id);

    void save(SysRoleVo vo);

    void remove(Long id);
}
