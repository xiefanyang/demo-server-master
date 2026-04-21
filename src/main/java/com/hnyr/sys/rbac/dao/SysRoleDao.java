package com.hnyr.sys.rbac.dao;

import com.hnyr.sys.data.Conditions;
import com.hnyr.sys.rbac.dao.repository.SysRoleRepository;
import com.hnyr.sys.rbac.entity.dto.SysRoleDto;
import com.hnyr.sys.rbac.entity.po.SysRole;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class SysRoleDao {
    @Resource
    private SysRoleRepository sysRoleRepository;
    @Resource
    private JdbcTemplate jdbcTemplate;
    public List<SysRole> getAllRoles() {
        return sysRoleRepository.findAll(Conditions.empty().and("deleted").is(false),
                Sort.by(Sort.Order.asc("sort"), Sort.Order.asc("id")));
    }

    public List<SysRole> getInIds(List<Long> ids, Boolean enable) {
        Conditions conditions = Conditions.empty().and("id").in(ids).and("deleted").is(false);
        if (enable != null) {
            conditions.and("enable").is(enable);
        }
        return sysRoleRepository.findAll(conditions, Sort.by(Sort.Order.asc("parentId"), Sort.Order.asc("sort"), Sort.Order.asc("id")));
    }

    public SysRole getById(Long id) {
        return sysRoleRepository.findOne(id);
    }

    public SysRole getByCode(String code, Boolean enable) {
        Conditions conditions = Conditions.empty().and("code").is(code).and("deleted").is(false);
        if (enable != null) {
            conditions.and("enable").is(enable);
        }
        return sysRoleRepository.findOne(conditions);
    }

    public List<SysRole> getByParentId(Long id) {
        return sysRoleRepository.findAll(Conditions.empty()
                .and("parentId").is(id).and("deleted").is(false).and("enable").is(true));
    }

    public void save(SysRole po) {
        if (po.getDeleted() == null) {
            po.setDeleted(false);
        }
        sysRoleRepository.save(po);
    }

    public SysRole findByCodeEnable(String roleCode) {
        return sysRoleRepository.findOne(Conditions.empty().and("code").is(roleCode).and("deleted").is(false).and("enable").is(true));
    }

    public SysRole findByIdEnable(Long id) {
        return sysRoleRepository.findOne(Conditions.empty().and("id").is(id).and("deleted").is(false).and("enable").is(true));
    }


    public List<SysRoleDto> getRoleListByRoleCodes(Set<String> roleCodes) {
        String sql = " select b.* from t_sys_role as b where b.code in(:roleCode) and b.enable = 1 and b.is_deleted = 0 ";
        Map<String, Object> searchMap = new HashMap<>();
        searchMap.put("roleCode", roleCodes);
        NamedParameterJdbcTemplate n = new NamedParameterJdbcTemplate(jdbcTemplate);
        return n.query(sql, searchMap, new BeanPropertyRowMapper<>(SysRoleDto.class));
    }

}
