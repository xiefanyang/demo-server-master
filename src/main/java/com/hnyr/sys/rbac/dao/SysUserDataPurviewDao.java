package com.hnyr.sys.rbac.dao;

import com.hnyr.sys.data.Conditions;
import com.hnyr.sys.rbac.dao.repository.SysUserDataPurviewRepository;
import com.hnyr.sys.rbac.entity.po.SysUserDataPurview;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class SysUserDataPurviewDao {

    @Resource
    private SysUserDataPurviewRepository sysUserDataPurviewRepository;

    public SysUserDataPurview getByUserIdAndResourceId(Long userId, Long resourceId, Boolean enable) {
        Conditions conditions = Conditions.empty()
                .and("userId").is(userId)
                .and("resourceId").is(resourceId)
                .and("deleted").is(false);
        if (enable != null) {
            conditions.and("enable").is(enable);
        }
        return sysUserDataPurviewRepository.findOne(conditions);
    }

    public SysUserDataPurview getById(Long id) {
        return sysUserDataPurviewRepository.findOne(id);
    }

    public void save(SysUserDataPurview po) {
        if (po.getDeleted() == null) {
            po.setDeleted(false);
        }
        sysUserDataPurviewRepository.save(po);
    }
}
