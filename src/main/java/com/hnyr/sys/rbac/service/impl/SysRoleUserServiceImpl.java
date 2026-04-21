package com.hnyr.sys.rbac.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import com.google.common.collect.Lists;
import com.hnyr.sys.config.BusinessException;
import com.hnyr.sys.rbac.dao.SysDataPurviewDefineDao;
import com.hnyr.sys.rbac.dao.SysRoleDao;
import com.hnyr.sys.rbac.dao.SysRoleUserDao;
import com.hnyr.sys.rbac.entity.dto.SysRoleDto;
import com.hnyr.sys.rbac.entity.dto.SysRoleUserDto;
import com.hnyr.sys.rbac.entity.dto.SysUserDataPurviewDto;
import com.hnyr.sys.rbac.entity.po.SysDataPurviewDefine;
import com.hnyr.sys.rbac.entity.po.SysRole;
import com.hnyr.sys.rbac.entity.po.SysRoleUser;
import com.hnyr.sys.rbac.service.SysRoleUserService;
import com.hnyr.sys.rbac.service.SysUserDataPurviewService;
import com.hnyr.sys.rbac.vo.SysRoleUserVo;
import com.hnyr.sys.utils.AssertUtil;
import com.hnyr.sys.utils.DataConvertor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class SysRoleUserServiceImpl implements SysRoleUserService {
    @Resource
    JdbcTemplate jdbcTemplate;
    @Resource
    SysRoleUserDao sysRoleUserDao;
    @Resource
    SysDataPurviewDefineDao sysDataPurviewDefineDao;
    @Resource
    SysUserDataPurviewService sysUserDataPurviewService;
    @Resource
    SysRoleDao sysRoleDao;

    private SysRoleUserDto bindRoleUser(Long userId, Long roleId, Boolean enable) {
        SysRoleUser po = get(userId, roleId);
        if (po != null) {
            po.setEnable(enable);
        } else {
            if (enable) {
                po = new SysRoleUser();
                po.setEnable(Boolean.TRUE);
                po.setRoleId(roleId);
                po.setUserId(userId);
                sysRoleUserDao.save(po);
            }

        }
        return BeanUtil.copyProperties(po, SysRoleUserDto.class);
    }


    @Override
    public void bindRoleUserRelationByRoleCode(List<Long> userIds, String roleCode, Boolean enable) {
        SysRole role = sysRoleDao.findByCodeEnable(roleCode);
        AssertUtil.isTrue(role != null, "未找到指定角色");
        for (Long userId : userIds) {
            bindRoleUser(userId, role.getId(), enable);
        }
    }

    @Override
    public void bindRoleUserRelationByRoleId(List<Long> userIds, Long roleId, Boolean enable) {
        SysRole role = sysRoleDao.findByIdEnable(roleId);
        AssertUtil.isTrue(role != null, "未找到指定角色");
        for (Long userId : userIds) {
            bindRoleUser(userId, role.getId(), enable);
        }
    }

    @Override
    public List<String> getRoleCodeByUserId(Long userId) {
        return sysRoleUserDao.getRoleCodeByUserId(userId);
    }

    @Override
    public List<SysRoleDto> getRoleListByRoleCodes(Set<String> roleCodes) {
        return sysRoleDao.getRoleListByRoleCodes(roleCodes);
    }


    @Override
    public Boolean changeGrant(Long userId, Long roleId, Boolean enable) {
        SysRoleUser roleUser = sysRoleUserDao.findRoleUserId(roleId, userId);
        if (roleUser != null) {
            roleUser.setEnable(enable);
            sysRoleUserDao.save(roleUser);
        } else {
            if (enable == true) {
                roleUser = new SysRoleUser();
                roleUser.setRoleId(roleId);
                roleUser.setUserId(userId);
                roleUser.setEnable(true);
                sysRoleUserDao.save(roleUser);
            }
        }
        return true;
    }

    @Override
    public List<Long> getUserIdsByRoleId(Long roleId) {
        return sysRoleUserDao.getUserIdsByRoleId(roleId);
    }

    @Override
    public List<Long> getRoleIdsByUserId(Long userId) {
        return sysRoleUserDao.getRoleIdsByUserId(userId);
    }


    @Override
    public SysRoleUser get(Long userId, Long roleId) {
        return sysRoleUserDao.get(userId, roleId);
    }

    @Override
    public SysRoleUser bindByUserAndRoleId(Long userId, Long roleId, Boolean enable) {
        SysRoleUser roleUser = sysRoleUserDao.bindByUserLongAndRoleId(userId, roleId, enable);
        return roleUser;
    }

    @Override
    public Map<String, Object> batchBindRoleUser(Long uid, List userIds, Long roleId, Boolean enable) {

        // 检查是否有权限
        // Boolean hasRight = sysRoleUserDao.hasRight(uid, roleId);
        // AssertUtil.isTrue(hasRight, "您不具有授权该角色的权限");
        Map<String, Object> map = new HashMap<>();
        map.put("ok", 0);
        map.put("fail", 0);
        map.put("failIds", new ArrayList<>());
        if (!CollectionUtils.isEmpty(userIds)) {
            Boolean flag = true;
            for (Object userId : userIds) {
                try {
                    bindByUserAndRoleId(Long.valueOf(userId.toString()), roleId, enable);
                    map.put("ok", (Integer) map.get("ok") + 1);
                } catch (Exception e) {
//                    log.error("{}", e.getMessage());
                    flag = false;
                    map.put("fail", (Integer) map.get("fail") + 1);
                    ((List) map.get("failIds")).add(userId);
                }
            }
        }

        return map;
    }

    @Override
    public List<SysRoleDto> getRolesByUserIdForGrant(Long userId) {
        if (userId == 1) {
            return getRolesByUserId(userId);
        }
        //获取管理的可授权角色  权限范围定义id为 3（固定）
        SysUserDataPurviewDto sdp = sysUserDataPurviewService.getByUserIdAndResourceCode(userId, "sys.rbac.grant");
        if (sdp == null || CollectionUtils.isEmpty(sdp.getIdsList())) {
            throw new BusinessException("未设置维护的角色");
        }

        List<SysRole> roles = sysRoleDao.getInIds(sdp.getIdsList().stream().map(Long::parseLong).collect(Collectors.toList()), Boolean.TRUE);
        if (CollectionUtils.isEmpty(roles)) {
            throw new BusinessException("未设置维护的角色");
        }
        return DataConvertor.listConvert(roles, SysRoleDto.class);
    }

    @Override
    public List<SysRoleDto> getRolesByUserId(Long userId) {
        // 取所有有权限角色
        List<String> userRoleCodes = sysRoleUserDao.getRoleCodesByUserId(userId);
        // 是否为超管，超管可维护全部。
        Boolean isAdmin = userId == 1;

        // 按code长度排序
        userRoleCodes.sort(Comparator.comparingInt(String::length));
        // 清除子code（因是嵌套，不必重复）
        List<String> filterCodes = Lists.newArrayList(userRoleCodes);
        Set<String> repeat = new HashSet<>();
        for (String code : userRoleCodes) {
            for (String c1 : filterCodes) {
                if (c1.startsWith(code + ".")) {
                    repeat.add(c1);
                }
            }
        }
        filterCodes.removeAll(repeat);
        if (!CollectionUtils.isEmpty(filterCodes)) {
            List<SysRoleDto> roles = DataConvertor
                    .listConvert(sysRoleUserDao.getRoleListLikeRoleCodes(filterCodes), SysRoleDto.class);
            List<SysRoleDto> result = new ArrayList<>();
            Map<String, SysRoleDto> m = new HashMap<>();
            roles.forEach(role -> {
                //如果非超管，角色code在绑定角色里（即：非子角色）
                if (!isAdmin && userRoleCodes.contains(role.getCode())) {
                    role.setManageSelf(Boolean.FALSE);
                }
                m.put(role.getCode(), role);
            });
            if (!CollectionUtils.isEmpty(roles)) {
                for (String code : filterCodes) {
                    SysRoleDto root = m.get(code);
                    setChildren(root, roles);
                    result.add(root);
                }
                result.sort(Comparator.comparing(SysRoleDto::getSort));
                return result;
            }
        }
        return new ArrayList<>();
    }

    private void setChildren(SysRoleDto current, List<SysRoleDto> list) {
        List<SysRoleDto> children = new ArrayList<>();
        for (SysRoleDto r : list) {
            if (current.getId().longValue() == r.getParentId().longValue()) {
                children.add(r);
            }
        }
        if (!CollectionUtils.isEmpty(children)) {
            children.sort(new Comparator<SysRoleDto>() {
                @Override
                public int compare(SysRoleDto o1, SysRoleDto o2) {
                    return o1.getSort().compareTo(o2.getSort());
                }
            });
            current.setChildren(children);
            current.getChildren().forEach(s -> {
                setChildren(s, list);
            });
        }
    }

    @Override
    public Page<SysRoleUserVo> getRoleUsersByRoleId(Pageable pageable, Long userId, Map<String, Object> searchMap) {
        //数据权限过滤可管理授权用户范围
        String filterSql = "";
        //获取用户授权的数据范围配置
        SysUserDataPurviewDto userDataPurview = sysUserDataPurviewService.getByUserIdAndResourceCode(userId, "sys.rbac.grant.users");
        if (userDataPurview != null) {
            SysDataPurviewDefine define = sysDataPurviewDefineDao.getById(userDataPurview.getPurviewId());
            AssertUtil.notNull(define, "未找到数据范围限定定义");
            //自定义权限过滤
            if (define.getType() == 1) {
                AssertUtil.notEmpty(userDataPurview.getIdsList(), "未设置数据权限范围");
                AssertUtil.notBlank(define.getFilter(), "未设置数据权限过滤SQL");
                filterSql = define.getFilter();
                List<String> idsListStr = userDataPurview.getIdsList();
                List idsList = new ArrayList<>();
                if ("Long".equals(define.getType())) {
                    idsList = idsListStr.stream().map(Long::parseLong).collect(Collectors.toList());
                } else if ("Integer".equals(define.getType())) {
                    idsList = idsListStr.stream().map(Integer::parseInt).collect(Collectors.toList());
                } else {
                    idsList = idsListStr;
                }
                searchMap.put("ids", idsList);
            }
        }
        Page<SysRoleUserVo> page = sysRoleUserDao.getRoleUserPage(pageable, searchMap, filterSql);
        Set<Long> userIds = new HashSet<>();
        Long roleId = MapUtil.getLong(searchMap, "roleId");
        if (!CollectionUtils.isEmpty(page.getContent())) {
            page.forEach(s -> {
                userIds.add(s.getUserId());
            });
        }
        if (!CollectionUtils.isEmpty(userIds)) {
            //获取用户的是否有需要指定设定的资源
            Long hasPurviewCount = sysRoleUserDao.countRoleResourceWithPurview1(roleId);
            if (hasPurviewCount > 0) {
                List<Map<String, Object>> setPurviewCount = sysRoleUserDao.getRoleUsersResourceWithPurview1(roleId, userIds);
                Set<Long> notSetPurviewUsers = new HashSet<>();
                if (!CollectionUtils.isEmpty(setPurviewCount)) {
                    setPurviewCount.forEach(s -> {
                        if (((Long) s.get("c")) < hasPurviewCount) {
                            notSetPurviewUsers.add((Long) s.get("user_id"));
                        }
                    });
                }
                page.getContent().forEach(s -> {
                    s.setHasPurview(true);
                    if (notSetPurviewUsers.contains(s.getUserId())) {
                        s.setNotSetPurview(true);
                    }
                });
            }

        }
        return page;
    }
}
