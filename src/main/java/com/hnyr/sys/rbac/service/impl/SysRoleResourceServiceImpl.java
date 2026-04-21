package com.hnyr.sys.rbac.service.impl;

import com.hnyr.sys.rbac.dao.SysRoleDao;
import com.hnyr.sys.rbac.dao.SysRoleResourceDao;
import com.hnyr.sys.rbac.entity.po.SysRole;
import com.hnyr.sys.rbac.entity.po.SysRoleResource;
import com.hnyr.sys.rbac.service.SysRoleResourceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class SysRoleResourceServiceImpl implements SysRoleResourceService {
    @Resource
    SysRoleResourceDao sysRoleResourceDao;
    @Resource
    SysRoleDao sysRoleDao;

    @Override
    public Boolean changeGrant(Long resourceId, Long roleId, Boolean enable) {
        SysRoleResource roleResource = sysRoleResourceDao.findRoleResourceId(roleId, resourceId);
        if (roleResource != null) {
            roleResource.setEnable(enable);
            sysRoleResourceDao.save(roleResource);
        } else {
            if (enable == true) {
                SysRole role = sysRoleDao.getById(roleId);
                roleResource = new SysRoleResource();
                roleResource.setRoleId(roleId);
                roleResource.setResourceId(resourceId);
                roleResource.setEnable(true);
                sysRoleResourceDao.save(roleResource);
            }
        }
        return true;
    }

    @Override
    public List<Long> getResourceIdsByUserId(Long uid) {
        return sysRoleResourceDao.getResourceIdsByUserId(uid);
    }

    @Override
    public List<Long> getResourceIdsByRoleId(Long roleId) {
        return sysRoleResourceDao.getResourceIdsByRoleId(roleId);
    }


}
