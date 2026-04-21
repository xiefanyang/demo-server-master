package com.hnyr.sys.rbac.dao;

import com.hnyr.sys.data.Conditions;
import com.hnyr.sys.rbac.dao.repository.SysRoleResourceRepository;
import com.hnyr.sys.rbac.entity.po.SysRoleResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SysRoleResourceDao {
    @Resource
    private SysRoleResourceRepository sysRoleResourceRepository;
    @Resource
    private JdbcTemplate jdbcTemplate;

    public void save(SysRoleResource roleResource) {
        sysRoleResourceRepository.save(roleResource);
    }

    public List<Long> getResourceIdsByRoleId(Long roleId) {
        String sql = "select distinct resource_id from t_sys_role_resource where role_id = :roleId and enable = 1 and is_deleted = 0";
        Map<String, Object> params = new HashMap<>();
        params.put("roleId", roleId);
        return new NamedParameterJdbcTemplate(jdbcTemplate).queryForList(sql, params, Long.class);
    }

    public List<Long> getResourceIdsByUserId(Long uid) {
        String sql = "select distinct a.id from t_sys_resource as a " +
                " inner join t_sys_role_resource as b on b.resource_id = a.id and b.enable = 1 and b.is_deleted = 0 " +
                " inner join t_sys_role_user as c on c.user_id = :userId and c.role_id = b.role_id and c.enable = 1 and c.is_deleted = 0 " +
                " where a.is_deleted = 0 ";
        Map<String, Object> params = new HashMap<>();
        params.put("userId", uid);
        return new NamedParameterJdbcTemplate(jdbcTemplate).queryForList(sql, params, Long.class);
    }

    public List<SysRoleResource> getAllRoleResourcesByRoleId(Long roleId) {
        return sysRoleResourceRepository.findAll(Conditions.empty().and("roleId").is(roleId).and("enable").is(true));
    }

    public List<SysRoleResource> getAllRoleResourcesByUserId(Long userId) {
        return sysRoleResourceRepository.findAll(Conditions.empty().and("userId").is(userId).and("enable").is(true));
    }

    public SysRoleResource findRoleResourceId(Long roleId, Long resourceId) {
        Conditions conditions = Conditions.empty();
        conditions.and("roleId").is(roleId);
        conditions.and("resourceId").is(resourceId);
        conditions.and("deleted").is(false);
        return sysRoleResourceRepository.findOne(conditions);
    }
}
