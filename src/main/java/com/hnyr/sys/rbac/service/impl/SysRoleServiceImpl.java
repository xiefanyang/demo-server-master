package com.hnyr.sys.rbac.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.hnyr.sys.config.BusinessException;
import com.hnyr.sys.rbac.dao.SysRoleDao;
import com.hnyr.sys.rbac.dao.SysRoleUserDao;
import com.hnyr.sys.rbac.entity.po.SysRole;
import com.hnyr.sys.rbac.entity.vo.SysRoleVo;
import com.hnyr.sys.rbac.service.SysRoleService;
import com.hnyr.sys.utils.AssertUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class SysRoleServiceImpl implements SysRoleService {
    @Resource
    private SysRoleDao roleDao;
    @Resource
    private SysRoleUserDao sysRoleUserDao;
//
//    private void setChildren(SysRole role, Collection<SysRole> roles) {
//        List<SysRole> children = new ArrayList<>();
//        for (SysRole r : roles) {
//            if (r.getParentId().longValue() == role.getId().longValue()) {
//                children.add(r);
//            }
//        }
//        if (!CollectionUtils.isEmpty(children)) {
//            role.setChildren(children);
//            role.getChildren().forEach(s -> {
//                setChildren(s, roles);
//            });
//        }
//    }
//
//    @Override
//    public List<SysRole> getTreeAllRoles() {
//        List<SysRole> roles = roleDao.getAllRoles();
//        SysRole root = new SysRole();
//        root.setId(0L);
//        setChildren(root, roles);
//        return root.getChildren();
//    }
//
//    @Override
//    public List<SysRole> getTreeRolesInIds(List<Long> roleIds) {
//        List<SysRole> allRoles = roleDao.getAllRoles();
//        if (CollectionUtils.isEmpty(allRoles)) {
//            throw new BusinessException("系统未初始化角色，请检查。");
//        }
//        List<SysRole> roles = roleDao.getInIds(roleIds);
//        Set<String> codes = new HashSet<>();
//        if (!CollectionUtils.isEmpty(roles)) {
//            roles.forEach(s -> {
//                codes.add(s.getCode());
//            });
//        }
//
//        LinkedHashSet<SysRole> userRoles = new LinkedHashSet<>();
//        allRoles.forEach(s -> {
//            for (String c : codes) {
//                if (s.getCode().equals(c) || s.getCode().startsWith(c + ".")) {
//                    userRoles.add(s);
//                }
//            }
//        });
//        if (CollectionUtils.isEmpty(userRoles)) {
//            throw new BusinessException("当前用户未配置角色");
//        }
//        SysRole root = new SysRole();
//        root.setId(0L);
//        setChildren(root, roles);
//        return root.getChildren();
//    }

    @Override
    public SysRole getRole(Long id) {
        return roleDao.getById(id);
    }

    @Override
    public void save(SysRoleVo vo) {
        if (vo.getParentId() > 0) {
            SysRole parent = roleDao.getById(vo.getParentId());
            if (!vo.getCode().startsWith(parent.getCode() + ".")) {
                throw new BusinessException("子角色code必须以父角色code 开头并以.间隔");
            }
        }
        if (vo.getId() == null) {
            roleDao.save(BeanUtil.copyProperties(vo, SysRole.class));
        } else {
            SysRole po = roleDao.getById(vo.getId());
            AssertUtil.isTrue(null != po, "未找到指定数据对象");
            BeanUtil.copyProperties(vo, po, "id", "version", "createTime");
            roleDao.save(po);
        }
    }

    @Override
    public void remove(Long id) {
        SysRole role = roleDao.getById(id);
        AssertUtil.notNull(role, "未找到指定角色");
        List<SysRole> children = roleDao.getByParentId(id);
        if (!CollectionUtils.isEmpty(children)) {
            throw new BusinessException("存在子角色，请先移除子角色");
        }
        Long roleUserCount = sysRoleUserDao.countUserByRoleId(id);
        AssertUtil.isTrue(roleUserCount == 0, "请先移除角色已授权用户");
        role.setCode(role.getCode() + "_del");
        role.setDeleted(true);
        roleDao.save(role);
    }
}
