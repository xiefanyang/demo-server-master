package com.hnyr.sys.rbac.dao;

import cn.hutool.core.map.MapUtil;
import com.hnyr.sys.config.BusinessException;
import com.hnyr.sys.data.Conditions;
import com.hnyr.sys.rbac.dao.repository.SysRoleRepository;
import com.hnyr.sys.rbac.dao.repository.SysRoleUserRepository;
import com.hnyr.sys.rbac.entity.po.SysRole;
import com.hnyr.sys.rbac.entity.po.SysRoleUser;
import com.hnyr.sys.rbac.vo.SysRoleUserVo;
import com.hnyr.sys.utils.AssertUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

@Component
public class SysRoleUserDao {
    @Resource
    private SysRoleUserRepository sysRoleUserRepository;
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    SysRoleRepository roleRepository;

    private SysRoleUser getRoleUser(Long userId, Long roleId) {
        return sysRoleUserRepository.findOne(Conditions.empty()
                .and("deleted").is(false)
                .and("userId").is(userId)
                .and("roleId").is(roleId));
    }

    public void save(SysRoleUser roleUser) {
        sysRoleUserRepository.save(roleUser);
    }

    public Long countUserByRoleId(Long roleId) {
        return sysRoleUserRepository.count(Conditions.empty().and("roleId").is(roleId).and("enable").is(true).and("deleted").is(false));
    }

    public List<Long> getUserIdsByRoleId(Long roleId) {
        String sql = "select distinct user_id from t_sys_role_user where role_id = :roleId and enable = 1 and is_deleted = 0";
        Map<String, Object> params = new HashMap<>(1);
        params.put("roleId", roleId);
        return new NamedParameterJdbcTemplate(jdbcTemplate).queryForList(sql, params, Long.class);
    }

    public List<Long> getRoleIdsByUserId(Long userId) {
        String sql = "select distinct role_id from t_sys_role_user where user_id = :userId and enable = 1 and is_deleted = 0";
        Map<String, Object> params = new HashMap<>(1);
        params.put("userId", userId);
        return new NamedParameterJdbcTemplate(jdbcTemplate).queryForList(sql, params, Long.class);
    }

    public List<SysRoleUser> getAllRoleUsersByRoleId(Long roleId) {
        return sysRoleUserRepository.findAll(Conditions.empty().and("roleId").is(roleId).and("enable").is(true));
    }

    public List<SysRoleUser> getAllRoleUsersByUserId(Long userId) {
        return sysRoleUserRepository.findAll(Conditions.empty().and("userId").is(userId).and("enable").is(true));
    }

    public SysRoleUser findRoleUserId(Long roleId, Long userId) {
        Conditions conditions = Conditions.empty();
        conditions.and("roleId").is(roleId);
        conditions.and("userId").is(userId);
        conditions.and("deleted").is(false);
        return sysRoleUserRepository.findOne(conditions);
    }

    public List<String> getRoleCodeByUserId(Long userId) {
        String sql = "SELECT" +
                " b.`code` " +
                " FROM" +
                " t_sys_role_user AS a" +
                " INNER JOIN t_sys_role AS b ON b.id = a.role_id " +
                " AND b.ENABLE = 1 " +
                " AND b.is_deleted = 0 " +
                " WHERE" +
                " a.enable = 1 " +
                " AND a.is_deleted = 0 " +
                " AND a.user_id = :userId ";
        Map<String, Object> params = new HashMap<>(1);
        params.put("userId", userId);
        return new NamedParameterJdbcTemplate(jdbcTemplate).queryForList(sql, params, String.class);
    }

    public List<SysRoleUser> getUserRoles(Long userId) {
        return sysRoleUserRepository.findAll(Conditions.empty().and("userId").is(userId).and("deleted").is(false));
    }

    public SysRoleUser get(Long userId, Long roleId) {
        return sysRoleUserRepository
                .findOne(Conditions.empty().and("userId").is(userId).and("roleId").is(roleId).and("deleted").is(false));
    }

    public SysRoleUser bindByUserLongAndRoleId(Long userId, Long roleId, Boolean enable) {
        SysRole role = roleRepository.findOne(Conditions.empty().and("deleted").is(false).and("id").is(roleId));
        if (role == null) {
            throw new BusinessException("角色" + roleId + "不存在");
        }
        return bindByUserLongAndRoleCode(userId, role, enable);
    }

    public SysRoleUser bindByUserLongAndRoleCode(Long userId, SysRole role, Boolean enable) {
        SysRoleUser po = get(userId, role.getId());
        if (po != null) {
            po.setEnable(enable);
        } else {
            po = new SysRoleUser();
            po.setEnable(enable);
            po.setRoleId(role.getId());
            po.setUserId(userId);
        }
        return sysRoleUserRepository.save(po);

    }

    public SysRoleUser bindByUserLongAndRoleCode(Long userId, String roleCode, Boolean enable) {
        SysRole role = roleRepository.findOne(Conditions.empty().and("deleted").is(false).and("code").is(roleCode));
        if (role == null) {
            throw new BusinessException("角色" + roleCode + "不存在");
        }
        return bindByUserLongAndRoleCode(userId, role, enable);

    }

    public List<String> getRoleCodesByUserId(Long userId) {
        String sql;
        if (userId == 1) {
            sql = " select distinct a.code from t_sys_role as a where  a.enable = 1 and a.is_deleted = 0 ";
            return jdbcTemplate.queryForList(sql, String.class);
        } else {
            sql = " select distinct a.code from t_sys_role as a inner join  t_sys_role_user as b on b.user_id = :userId  and b.role_id = a.id and b.enable = 1 and b.is_deleted = 0  where  a.enable = 1 and a.is_deleted = 0 ";
            Map<String, Object> params = new HashMap<>(1);
            params.put("userId", userId);
            return new NamedParameterJdbcTemplate(jdbcTemplate).queryForList(sql, params, String.class);
        }

    }

    public List<String> getRoleCodesByUserIdWithoutLevel(Long userId, Integer sys, Boolean hideBis) {
        String sql;
        Map<String, Object> params = new HashMap<>();
        if (userId == 1) {
            sql = " select distinct a.code from t_sys_role as a where  a.enable = 1 and a.is_deleted = 0 ";
        } else {
            sql = " select distinct a.code from t_sys_role as a inner join  t_sys_role_user as b on b.user_id = :userId and b.role_id = a.id and b.enable = 1 and b.is_deleted = 0 "
                    + " where  a.enable = 1 and a.is_deleted = 0 ";
            params.put("userId", userId);
        }

        if (sys != null) {
            sql += " and a.sys = :sys ";
            params.put("sys", sys);
        }

        if (hideBis != null) {
            sql += hideBis ? " and a.bis_show = 0 " : " and a.bis_show = 1 ";
        }
        return new NamedParameterJdbcTemplate(jdbcTemplate).queryForList(sql, params, String.class);
    }

    public Boolean hasRight(Long userId, Long roleId) {
        SysRole role = roleRepository.findOne(roleId);
        AssertUtil.isTrue(role != null, "授权的角色不存在");
        List<String> userRoles = getRoleCodesByUserId(userId);
        Boolean flag = false;
        if (!CollectionUtils.isEmpty(userRoles)) {
            flag = userRoles.stream().anyMatch(item -> role.getCode().startsWith(item + "."));
        }
        return flag;
    }

    /**
     * 根据code前缀获取
     *
     * @param roleCodes
     * @return
     */
    public List<SysRole> getRoleListLikeRoleCodes(List<String> roleCodes) {
        if (CollectionUtils.isEmpty(roleCodes)) {
            return new ArrayList<>();
        }
        Conditions conditions = Conditions.empty().and("enable").is(true).and("deleted").is(false);
        Conditions c1 = Conditions.empty();
        roleCodes.forEach(role -> {
            c1.or("code").like(role + ".%");
            c1.or("code").is(role);
        });
        conditions.and(c1);
        return roleRepository.findAll(conditions);
    }

    public Long countRoleResourceWithPurview1(Long roleId) {
        NamedParameterJdbcTemplate n = new NamedParameterJdbcTemplate(jdbcTemplate);
        String sql = "SELECT count(a.id) FROM t_sys_resource as a inner join t_sys_role_resource as b on b.resource_id = a.id and b.role_id = :roleId and " +
                " b.is_deleted = 0 and b.enable = 1 WHERE a.purview = 1 and a.is_deleted = 0  ";
        Map<String, Object> params = new HashMap<>();
        params.put("roleId", roleId);
        return new NamedParameterJdbcTemplate(jdbcTemplate).queryForObject(sql, params, Long.class);
    }

    public List<Map<String, Object>> getRoleUsersResourceWithPurview1(Long roleId, Set<Long> userIds) {
        NamedParameterJdbcTemplate n = new NamedParameterJdbcTemplate(jdbcTemplate);
        String sql = "SELECT c.user_id ,count(c.purview_id) as c FROM t_sys_role_resource as a inner join t_sys_resource as b on a.resource_id = b.id and " +
                " b.is_deleted = 0 and b.purview = 1 " +
                " left join t_sys_user_data_purview as c on c.role_id = a.role_id and c.resource_id = a.resource_id and c.enable = 1 and  c.user_id in (:userIds)  and c.is_deleted = 0 " +
                " WHERE a.role_id = :roleId and a.enable = 1 and a.is_deleted = 0  " +
                " group by c.user_id ";
        Map<String, Object> params = new HashMap<>();
        params.put("roleId", roleId);
        params.put("userIds", userIds);
        return n.queryForList(sql, params);
    }

    public Page<SysRoleUserVo> getRoleUserPage(Pageable pageable, Map<String, Object> searchMap, String purviewSql) {
        Long roleId = MapUtil.getLong(searchMap, "roleId");
        AssertUtil.isTrue(roleId != null, "必须有角色ID");

        NamedParameterJdbcTemplate n = new NamedParameterJdbcTemplate(jdbcTemplate);


        String sql = " from t_sys_role_user as a "
                + " inner join t_sys_user as b on b.id = a.user_id and b.state = 0 and b.is_deleted = 0 "
                + " inner join t_sys_role as r on a.role_id = r.id "
                + purviewSql
                + " where a.role_id = " + roleId + " and a.is_deleted = 0 ";
        Boolean enable = MapUtil.getBool(searchMap, "enable");
        if (enable != null) {
            sql += " and a.enable = :enable ";
        }
        String idNumber = MapUtil.getStr(searchMap, "idNumber");
        String name = MapUtil.getStr(searchMap, "name");
        if (StringUtils.hasText(idNumber)) {
            sql += " and b.username like :idNumber ";
            searchMap.put("username", "%" + idNumber + "%");
        }
        if (StringUtils.hasText(name)) {
            sql += " and b.name like :name ";
            searchMap.put("name", "%" + name + "%");
        }
        long total = n.queryForObject("select count(a.user_id) " + sql, searchMap, Long.class);

        String selectSql = "select a.*,b.name,b.username  " + sql + " limit "
                + pageable.getPageSize() * pageable.getPageNumber() + " ," + pageable.getPageSize();
        List<SysRoleUserVo> roles = n.query(selectSql, searchMap, new BeanPropertyRowMapper<>(SysRoleUserVo.class));
        return new PageImpl<>(roles, pageable, total);
    }
}
