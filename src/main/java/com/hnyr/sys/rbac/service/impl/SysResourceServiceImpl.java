package com.hnyr.sys.rbac.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.hnyr.sys.rbac.dao.SysResourceDao;
import com.hnyr.sys.rbac.entity.po.SysResource;
import com.hnyr.sys.rbac.entity.vo.SysResourceVo;
import com.hnyr.sys.rbac.service.SysResourceService;
import com.hnyr.sys.utils.AssertUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class SysResourceServiceImpl implements SysResourceService {
    @Resource
    private SysResourceDao sysResourceDao;

    @Override
    public List<SysResourceVo> getTreeResourcesByUser(Long userId) {
        List<SysResource> resources = sysResourceDao.getTreeResourcesByUser(userId);
        SysResourceVo root = new SysResourceVo();
        root.setId(0L);
        setChildren(root, resources);
        return root.getChildren();
    }

    @Override
    public List<SysResource> getTreeResourcesByUserAndRoleId(Long userId, Long roleId) {
        return sysResourceDao.getTreeResourcesByUserAndRoleId(userId, roleId);
    }

    @Override
    public List<SysResourceVo> getTreeAllResources() {
        List<SysResource> resources = sysResourceDao.getTreeAllResources();
        SysResourceVo root = new SysResourceVo();
        root.setId(0L);
        setChildren(root, resources);
        return root.getChildren();
    }

    @Override
    public void save(SysResourceVo vo) {
        if (vo.getId() == null) {
            sysResourceDao.save(BeanUtil.copyProperties(vo, SysResource.class));
        } else {
            SysResource po = sysResourceDao.getById(vo.getId());
            AssertUtil.isTrue(null != po, "未找到指定数据对象");
            BeanUtil.copyProperties(vo, po, "id", "version", "createTime");
            sysResourceDao.save(po);
        }
    }


    private void setChildren(SysResourceVo resource, List<SysResource> resources) {
        List<SysResourceVo> children = new ArrayList<>();
        for (SysResource r : resources) {
            if (r.getParentId().longValue() == resource.getId().longValue()) {
                SysResourceVo vo = new SysResourceVo();
                BeanUtil.copyProperties(r, vo);
                children.add(vo);
            }
        }
        if (!CollectionUtils.isEmpty(children)) {
            resource.setChildren(children);
            resource.getChildren().forEach(s -> {
                setChildren(s, resources);
            });
        }
    }

}
