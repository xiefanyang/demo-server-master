package com.hnyr.sys.rbac.service;

import com.hnyr.sys.rbac.entity.po.SysResource;
import com.hnyr.sys.rbac.entity.vo.SysResourceVo;

import java.util.List;

public interface SysResourceService {

    List<SysResourceVo> getTreeResourcesByUser(Long userId);

    List<SysResource> getTreeResourcesByUserAndRoleId(Long userId, Long roleId);

    List<SysResourceVo> getTreeAllResources();

    void save(SysResourceVo vo);

}
