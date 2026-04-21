package com.hnyr.sys.rbac.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.hnyr.sys.rbac.dao.SysDataPurviewDefineDao;
import com.hnyr.sys.rbac.dao.SysResourceDao;
import com.hnyr.sys.rbac.dao.SysUserDataPurviewDao;
import com.hnyr.sys.rbac.entity.dto.SysUserDataPurviewDto;
import com.hnyr.sys.rbac.entity.po.SysDataPurviewDefine;
import com.hnyr.sys.rbac.entity.po.SysResource;
import com.hnyr.sys.rbac.entity.po.SysUserDataPurview;
import com.hnyr.sys.rbac.service.SysUserDataPurviewService;
import com.hnyr.sys.utils.AssertUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class SysUserDataPurviewServiceImpl implements SysUserDataPurviewService {

    @Resource
    private SysUserDataPurviewDao sysUserDataPurviewDao;
    @Resource
    private SysResourceDao sysResourceDao;
    @Resource
    private SysDataPurviewDefineDao defineDao;

    @Override
    public SysUserDataPurviewDto getByUserIdAndResourceId(Long userId, Long resourceId) {
        SysUserDataPurview po = sysUserDataPurviewDao.getByUserIdAndResourceId(userId, resourceId, Boolean.TRUE);
        return BeanUtil.copyProperties(po, SysUserDataPurviewDto.class);
    }

    @Override
    public SysUserDataPurviewDto getByUserIdAndResourceCode(Long userId, String resourceCode) {
        SysResource resource = sysResourceDao.getByCode(resourceCode, Boolean.TRUE);
        AssertUtil.notNull(resource, "无权限访问该资源");
        return getByUserIdAndResourceId(userId, resource.getId());
    }

    @Override
    public void save(SysUserDataPurviewDto dto) {
        SysUserDataPurview po = sysUserDataPurviewDao.getByUserIdAndResourceId(dto.getUserId(), dto.getResourceId(), null);
        if (po == null) {
            sysUserDataPurviewDao.save(BeanUtil.copyProperties(dto, SysUserDataPurview.class));
        } else {
            dto.setEnable(Boolean.TRUE);
            BeanUtil.copyProperties(dto, po, "id", "version", "createTime");
            sysUserDataPurviewDao.save(po);
        }
    }

    @Override
    public List getUserDataPurviewIds(Long userId, String resourceCode) {
        SysUserDataPurviewDto dto = getByUserIdAndResourceCode(userId, resourceCode);
        if (dto != null) {
            //获取定义
            SysDataPurviewDefine define = defineDao.getById(dto.getPurviewId());
            //自定义，必须配置数据权限，未配置则为
            if (define.getType() == 1) {
                AssertUtil.notEmpty(dto.getIdsList(), "");
                if ("Long".equals(define.getType())) {
                    return dto.getIdsList().stream().map(Long::parseLong).collect(Collectors.toList());
                } else if ("Integer".equals(define.getType())) {
                    return dto.getIdsList().stream().map(Integer::parseInt).collect(Collectors.toList());
                }
                return dto.getIdsList();
            }
        }
        return new ArrayList<>();
    }
}
