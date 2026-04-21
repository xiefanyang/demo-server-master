package com.hnyr.sys.rbac.service.impl;

import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.hnyr.sys.rbac.security.SecurityConstant;
import com.hnyr.sys.rbac.security.UserPermissionVo;
import com.hnyr.sys.rbac.service.PermissionService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Transactional(rollbackFor = Exception.class)
public class PermissionServiceImpl implements PermissionService {

    @Resource
    RedisTemplate redisTemplate;
    @Resource
    JdbcTemplate jdbcTemplate;

    @Override
    public UserPermissionVo renewAccountPermissionByUserId(Long userId) {
        /**
         * 获取用户所有角色
         */
        String sql = "SELECT b.code,b.id FROM t_sys_role_user AS a " +
                " INNER JOIN t_sys_role as b ON a.role_id = b.id AND b.enable = 1 AND b.is_deleted = 0 " +
                " WHERE a.is_deleted = 0 and a.enable = 1 and a.user_id = :userId ";
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        List<Map<String, Object>> roleMap = new NamedParameterJdbcTemplate(jdbcTemplate).queryForList(sql, map);
        List<String> roleCodes = new ArrayList<>();
        List<Long> roleIds = new ArrayList<>();
        List<String> resources = new ArrayList<>();
        if (!CollectionUtils.isEmpty(roleMap)) {
            for (Map<String, Object> m : roleMap) {
                roleCodes.add(m.get("code").toString());
                roleIds.add(Long.valueOf(m.get("id").toString()));
            }
        }
        //根据用户角色获取所有资源
        if (!CollectionUtils.isEmpty(roleCodes)) {
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
            sql = "SELECT a.code,a.id,a.open_use FROM t_sys_resource as a WHERE a.is_deleted = 0 and a.visible = 1 " +
                    " AND a.id IN (SELECT b.resource_id FROM t_sys_role_resource as b WHERE b.is_deleted = 0 and b.enable = 1 " +
                    " AND b.role_id in (:roles))";
            map = new HashMap<>();
            map.put("roles", roleIds);
            List<Map<String, Object>> resourceList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(roleIds)) {
                resourceList = namedParameterJdbcTemplate.queryForList(sql, map);
            }
            for (Map<String, Object> m : resourceList) {
                resources.add(m.get("code").toString());
            }
        } else {
//            throw new BusinessException("抱歉，您未被授权访问任何资源");
            return null;
        }
        UserPermissionVo permission = new UserPermissionVo();
        permission.setUserId(userId);
        permission.setRoles(roleCodes);
        permission.setResources(resources);
        //重写权限
        saveCache(userId, permission);
        return permission;
    }

    @Override
    public UserPermissionVo getSuperAdminPermission() {
        UserPermissionVo permission = new UserPermissionVo();
        permission.setUserId(1L);
        permission.setRoles(Lists.newArrayList());
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        String sql = "SELECT a.code FROM t_sys_resource as a WHERE a.is_deleted = 0 and a.visible = 1 ";
        Map<String, Object> map = new HashMap<>();
        List<String> resourceList = namedParameterJdbcTemplate.queryForList(sql, map, String.class);
        permission.setResources(resourceList);
        //重写权限
        saveCache(1L, permission);
        return permission;
    }

    @Override
    public UserPermissionVo getPermissionCache(Long userId) {
        Object cache = redisTemplate.opsForValue().get(getAclKey(userId));
        if (cache == null) {
            return null;
        } else {
            UserPermissionVo permission = JSONUtil.toBean(cache.toString(), UserPermissionVo.class);
            if (permission != null && !CollectionUtils.isEmpty(permission.getRoles())) {
                return permission;
            }
            return null;
        }
    }

    /**
     * 创建7天缓存
     *
     * @param userId
     * @param permission
     */
    private void saveCache(Long userId, UserPermissionVo permission) {
        redisTemplate.opsForValue().set(getAclKey(userId), JSONUtil.toJsonStr(permission), 7, TimeUnit.DAYS);
    }

    private String getAclKey(Long userId) {
        return SecurityConstant.CACHE_RESOURCE_VALID + ":" + userId;
    }

    @Override
    public List<String> getCurrentUserRoles(Long userId) {
        Object cache = redisTemplate.opsForValue().get(getAclKey(userId));
        if (cache == null) {
            return new ArrayList<>();
        } else {
            UserPermissionVo permission = JSONUtil.toBean(cache.toString(), UserPermissionVo.class);
            if (permission != null && !CollectionUtils.isEmpty(permission.getRoles())) {
                return permission.getRoles();
            }
            return new ArrayList<>();
        }
    }
}
