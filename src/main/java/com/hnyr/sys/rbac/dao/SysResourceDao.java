package com.hnyr.sys.rbac.dao;

import com.hnyr.sys.data.Conditions;
import com.hnyr.sys.rbac.dao.repository.SysResourceRepository;
import com.hnyr.sys.rbac.entity.po.SysResource;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SysResourceDao {
    @Resource
    private SysResourceRepository repository;
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private SysRoleUserDao sysRoleUserDao;

    public List<SysResource> getTreeResourcesByUser(Long userId) {

        List<Long> roleIds = sysRoleUserDao.getRoleIdsByUserId(userId);
        String sql = "SELECT a.* FROM t_sys_resource AS a \n" +
                "where a.is_deleted = 0 and a.id in (select distinct resource_id from t_sys_role_resource where role_id in (:ids) and enable = 1 and a.is_deleted = 0)";
        Map<String, Object> params = new HashMap<>(1);
        params.put("ids", roleIds);
        return new NamedParameterJdbcTemplate(jdbcTemplate).query(sql, params, new BeanPropertyRowMapper<>(SysResource.class));
    }

    /**
     * 获取role user的需要设置数据权限的资源
     *
     * @param userId
     * @param roleId
     * @return
     */
    public List<SysResource> getTreeResourcesByUserAndRoleId(Long userId, Long roleId) {
        String sql = "SELECT a.* FROM t_sys_resource AS a \n" +
                "INNER JOIN t_sys_role_resource as b on b.resource_id = a.id and b.role_id = :roleId and b.is_deleted = 0 and b.`enable` = 1\n" +
                "INNER JOIN t_sys_role_user as c on c.role_id = b.role_id and c.user_id = :userId and c.`enable` = 1 and c.is_deleted = 0\n" +
                "where a.purview = 1 and a.is_deleted = 0 ";
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("roleId", roleId);
        return new NamedParameterJdbcTemplate(jdbcTemplate).query(sql, params, new BeanPropertyRowMapper<>(SysResource.class));
    }

    public List<SysResource> getTreeAllResources() {
        return repository.findAll(Conditions.empty().and("deleted").is(false),
                Sort.by(Sort.Order.asc("sort"), Sort.Order.asc("id")));
    }

    public SysResource getById(Long id) {
        return repository.findOne(id);
    }

    public void save(SysResource po) {
        if (po.getDeleted() == null) {
            po.setDeleted(false);
        }
        repository.save(po);
    }

    public List<SysResource> getAllList() {
        return repository.findAll(Conditions.empty().and("deleted").is(false),
                Sort.by(Sort.Order.asc("sort"), Sort.Order.asc("id")));
    }

    public SysResource getByCode(String code, Boolean enable) {
        Conditions conditions = Conditions.empty().and("code").is(code).and("deleted").is(false);
        return repository.findOne(conditions);
    }
}
