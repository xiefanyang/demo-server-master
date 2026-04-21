package com.hnyr.sys.rbac.dao;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SmUtil;
import com.hnyr.sys.config.BusinessException;
import com.hnyr.sys.data.Conditions;
import com.hnyr.sys.rbac.dao.repository.SysUserRepository;
import com.hnyr.sys.rbac.entity.dto.SysUserDto;
import com.hnyr.sys.rbac.entity.po.SysUser;
import com.hnyr.sys.utils.AssertUtil;
import com.hnyr.sys.utils.SmUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
public class SysUserDao {

    @Resource
    SysUserRepository repository;
    @Resource
    JdbcTemplate jdbcTemplate;


    public List<Long> getSysRoleIdForUserId(Long userId) {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);

        String sql = "select a.id as roleId from t_sys_role_user  as b" +
                " left join t_sys_role as a on b.role_id = a.id and a.enable = 1 " +
                " and b.user_id  = :userId and a.is_deleted = 0 " +
                " where b.enable = 1 and b.is_deleted = 0";
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        return namedParameterJdbcTemplate.queryForList(sql, params, Long.class);
    }

    public List<Map<String, Object>> getSysRoleUsersInUserIds(List<Long> userIds) {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);

        String sql = "select b.user_id as id,a.id as roleId from t_sys_role_user  as b" +
                " left join t_sys_role as a on b.role_id = a.id and a.enable = 1 " +
                " and b.user_id in (:ids) and a.is_deleted = 0 " +
                " where b.enable = 1 and b.is_deleted = 0";
        Map<String, Object> params = new HashMap<>();
        params.put("ids", userIds);
        return namedParameterJdbcTemplate.queryForList(sql, params);
    }

    public SysUser getByUserNameHasDeleted(String username) {
        return repository.findOne(Conditions.empty().and("username").is(username));
    }

    public SysUser saveWithDefaultPassword(SysUserDto dto) {
        AssertUtil.isTrue(dto.getUsername() != null, "缺少用户名");
        SysUser po = getByUserNameHasDeleted(dto.getUsername());
        if (po != null) {
            if (po.getDeleted()) {
                dto.setDeleted(true);
                dto.setModifier(dto.getModifier());
            }
            BeanUtil.copyProperties(dto, po, CopyOptions.create().ignoreNullValue().setIgnoreProperties("id", "password", "createTime", "version", "updatePasswordTime", "username"));
            po.setPassword(po.getPassword());
        } else {
            po = new SysUser();
            AssertUtil.isTrue(StringUtils.hasText(dto.getPassword()), "未设置默认密码");
            BeanUtil.copyProperties(dto, po, CopyOptions.create().ignoreNullValue().setIgnoreProperties("id", "password", "createTime", "version", "updatePasswordTime"));
            po.setPassword(SmUtils.sm3Enc(dto.getPassword()));
        }
        AssertUtil.isTrue(!(StringUtils.hasText(po.getUsername()) && getByUsername(po.getUsername(), po.getId()) != null), "用户名不能重复");
        return repository.save(po);
    }


    /**
     * 获取有效用户in ids
     *
     * @param ids
     * @return
     */
    public List<SysUser> getInIds(List<Long> ids, Boolean enable) {
        Conditions conditions = Conditions.empty().and("deleted").is(false).and("id").in(ids);
        if (enable != null) {
            conditions.and("state").is(enable ? 0 : 1);
        }
        return repository.findAll(conditions);
    }

    public List<Long> getUserIds(List<String> idNumbers) {
        Conditions conditions = Conditions.empty().and("deleted").is(false).and("state").is(0).and("idNumber").in(idNumbers);
        List<SysUser> list = repository.findAll(conditions);
        List<Long> userIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(accountPo -> {
                userIds.add(accountPo.getId());
            });
        }
        return userIds;
    }


    /**
     * 用身份证号默认添加用户
     *
     * @param dto
     * @return
     */
    public SysUser addDefaultByIdCardAndName(SysUserDto dto) {
        SysUser po = BeanUtil.copyProperties(dto, SysUser.class, "id", "version");

        po.setPassword(SmUtils.sm3Enc(po.getPassword()));

        if (StringUtils.hasText(po.getUsername()) && getByUsername(po.getUsername(), null) != null) {
            throw new BusinessException("用户名已存在" + po.getUsername());
        }
        return repository.save(po);
    }

    /**
     * 用学工号默认添加用户
     *
     * @param dto
     * @return
     */
    public SysUser addDefaultByIdNumberAndName(SysUserDto dto) {
        SysUser po = getByUserNameHasDeleted(dto.getUsername());
        if (po != null) {
            BeanUtil.copyProperties(dto, po, "id", "version");
            po.setDeleted(false);
        } else {
            po = BeanUtil.copyProperties(dto, SysUser.class, "id", "version");
        }
        po.setPassword(SmUtils.sm3Enc(po.getPassword()));
        if (StringUtils.hasText(po.getUsername()) && getByUsername(po.getUsername(), po.getId()) != null) {
            throw new BusinessException("用户名已存在" + po.getUsername());
        }
        return repository.save(po);
    }

    public SysUser add(SysUserDto dto) {
        SysUser po = new SysUser();
        if (!StringUtils.hasText(dto.getIdNumber())) {
            dto.setIdNumber(dto.getUsername());
        } else {
            // 如果对应idNumber 已存在则更新
            SysUser sysUserPo = getByIdNumber(dto.getIdNumber(), null);
            if (sysUserPo != null) {
                if (sysUserPo.getDeleted()) {
                    dto.setId(sysUserPo.getId());
                    dto.setDeleted(false);
                    return modify(dto);
                } else {
                    throw new BusinessException("存在相同工号/学号用户");
                }
            }
        }

        BeanUtils.copyProperties(dto, po);
        po.setPassword(SmUtil.sm3(dto.getPassword()));
        preSave(po);
        return repository.save(po);
    }

    private void preSave(SysUser po) {
        if (StringUtils.hasText(po.getUsername()) && getByUsername(po.getUsername(), null) != null) {
            throw new BusinessException("用户名不能重复");
        }
    }

    public SysUser getById(Long id) {
        return repository.findOne(id);
    }

    public SysUser getByIdNumber(String idNumber, Long id) {
        Conditions conditions = Conditions.empty().and("idNumber").is(idNumber);
        if (id != null) {
            conditions.and("id").ne(id);
        }
        return repository.findOne(conditions);
    }

    public SysUser modify(SysUserDto dto) {
        if (dto.getId() == null) {
            throw new BusinessException("缺少 ID");
        }
        SysUser po = getById(dto.getId());
        if (dto.getDeleted()) {
            po.setDeleted(true);
            po.setModifier(dto.getModifier());
            return repository.save(po);
        }
        // 密码不更新，提供专门的密码修改方法。管理端为重置（按照一定规则）、业务用户为用户名（学工号）后六位
        dto.setPassword(po.getPassword());
        if ("none".equals(dto.getPassword())) {
            po.setPassword(SmUtil.sm3(org.apache.commons.lang3.StringUtils.right(dto.getUsername(), 6)));
        }


        BeanUtils.copyProperties(dto, po);

        if (StringUtils.hasText(po.getUsername()) && getByUsername(po.getUsername(), po.getId()) != null) {
            throw new BusinessException("用户名不能重复");
        }
        return repository.save(po);
    }


    public SysUser modifyUser(SysUserDto dto) {
        if (dto.getId() == null) {
            throw new BusinessException("缺少 ID");
        }
        SysUser po = getById(dto.getId());
        if (dto.getDeleted()) {
            po.setDeleted(true);
            po.setModifier(dto.getModifier());
            return repository.save(po);
        }
        BeanUtils.copyProperties(dto, po);
        if (StringUtils.hasText(po.getUsername()) && getByUsername(po.getUsername(), po.getId()) != null) {
            throw new BusinessException("用户名不能重复");
        }
        return repository.save(po);
    }

    public SysUser getByUsername(String username, Long userId) {
        // 不排除已删除的
        Conditions condition = Conditions.empty();
        condition.and("username").is(username);
        condition.and("deleted").is(false);
        if (userId != null) {
            condition.and("id").ne(userId);
        }
        return repository.findOne(condition);
    }


    public Page<SysUser> page(Pageable pageable, Map<String, Object> searchMap) {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        Long id = MapUtil.getLong(searchMap, "id");
        Integer state = MapUtil.getInt(searchMap, "state");
        Long orgId = MapUtil.getLong(searchMap, "orgId");
        String keyword = MapUtil.getStr(searchMap, "keyword");
        Integer type = MapUtil.getInt(searchMap, "type");

        String sql = " from t_sys_user as a " + (orgId != null ? " inner join t_sys_org_user as b on b.user_id = a.id and b.is_deleted = 0  and b.org_id = :orgId " : "") + "where a.is_deleted = 0 and a.username <> 'sadmin'";
        if (id != null) {
            sql += " and a.id = :id ";
        }
        if (state != null) {
            sql += " and a.state = :state ";
        }
        if (type != null) {
            sql += " and a.type = :type ";
        }

        if (StringUtils.hasText(keyword)) {
            keyword = "%" + keyword + "%";
            searchMap.put("keyword", keyword);
            sql += " and ( a.username like :keyword or a.nickname like :keyword)";
        }

        long count = namedParameterJdbcTemplate.queryForObject("select count(a.id) " + sql, searchMap, Long.class);
        sql = "select a.* " + sql + " order by id desc limit " + pageable.getPageSize() * pageable.getPageNumber() + " , " + pageable.getPageSize();
        List<SysUser> us = namedParameterJdbcTemplate.query(sql, searchMap, new BeanPropertyRowMapper<>(SysUser.class));
        return new PageImpl<>(us, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()), count);
    }

    public void validPasswordStrong(String password) {
        //String reg = "^^(?![a-zA-z]+$)(?!\d+$)(?![!@#$%^&*_-]+$)(?![a-zA-z\d]+$)(?![a-zA-z!@#$%^&*_-]+$)(?![\d!@#$%^&*_-]+$)[a-zA-Z\d!@#$%^&*_-]{6,16}+$";
        String reg = "^^(?![a-zA-Z]+$)(?![A-Z0-9]+$)(?![A-Z,.:;，。？''\"\"；；‘’“”·、_~!@#$%^&*()<>{}\\[\\]【】|?\\/+=-]+$)(?![a-z0-9]+$)(?![a-z,.:;，。？''\"\"；；‘’“”_~!@#$%^&*()<>{}\\[\\]【】|?/+=-]+$)(?![0-9,.:;，。？''\"\"；；‘’“”_~!@#$%^&*()<>{}\\[\\]【】|?/+=-]+$)[a-zA-Z0-9,.:;，。？''\"\"；；‘’“”_~!@#$%^&*()<>{}\\[\\]【】|?/+=-]{6,16}+$";
        if (!password.matches(reg)) {
//            log.info("密码强度不足  {}", password);
            throw new BusinessException("密码强度不足，请同时包含数字，字母，特殊符号，且长度6-16位");
        }
    }

    /**
     * 不检查密码强度
     *
     * @param dto
     * @return
     */
    public SysUser addWithoutPasswordValid(SysUserDto dto) {
        SysUser po = new SysUser();
        BeanUtil.copyProperties(dto, po);
        //不检查
//        validPasswordStrong(dto.getPassword());

//        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        po.setPassword(passwordEncoder.encode(dto.getPassword()));
        po.setPassword(SmUtil.sm3(dto.getPassword()));
        if (StringUtils.hasText(po.getUsername()) && getByUsername(po.getUsername(), null) != null) {
            throw new BusinessException("学号/工号已存在");
        }
        return repository.save(po);
    }

    public SysUserDto getByUserNameOrEmailOrIdCardOrMobile(String key) {
        Conditions conditions = Conditions.empty();
        conditions.or("username").is(key);
        Conditions conditions1 = Conditions.empty().and(conditions).and("deleted").is(false);
        List<SysUser> pos = repository.findAll(conditions1);
        if (CollectionUtils.isEmpty(pos)) {
            return null;
        } else {
            SysUser po = pos.get(0);
            if (po != null) {
                SysUserDto dto = new SysUserDto();
                BeanUtil.copyProperties(po, dto);
                return dto;
            }
            return null;
        }
    }

    public SysUser update(SysUser po) {
        return repository.save(po);
    }

    public SysUser update(SysUserDto dto) {
        if (dto.getId() == null) {
            throw new BusinessException("缺少 ID");
        }
        SysUser po = getById(dto.getId());
        if (dto.getDeleted()) {
            po.setDeleted(true);
            po.setModifier(dto.getModifier());
            return repository.save(po);
        }
        dto.setPassword(po.getPassword());
//        //默认占位密码不更新该
//        if (!RbacConstant.PASSWORD_DEFAULT_ENCODE.equals(dto.getPassword())) {
//            validPasswordStrong(dto.getPassword());
//            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//            dto.setPassword(passwordEncoder.encode(dto.getPassword()));
//            dto.setUpdatePasswordTime(System.currentTimeMillis());
//        } else {
//            dto.setPassword(po.getPassword());
//            dto.setUpdatePasswordTime(po.getUpdatePasswordTime());
//        }
        BeanUtil.copyProperties(dto, po);
        //首次创建时候的租户ID不能变

        if (StringUtils.hasText(po.getUsername()) && getByUsername(po.getUsername(), po.getId()) != null) {
            throw new BusinessException("用户名不能重复");
        }

        return repository.save(po);
    }
//
//    public Page<SysUser> page(Pageable pageable, Map<String, Object> searchMap) {
//        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
//        Long id = MapUtil.getLong(searchMap, "id");
//        Long state = MapUtil.getLong(searchMap, "state");
//        Long orgId = MapUtil.getLong(searchMap, "orgId");
//        String keyword = MapUtil.getStr(searchMap, "keyword");
//
//        String sql = " from t_sys_user as a " + (orgId != null ? " inner join t_sys_org_user as b on b.user_id = a.id and b.is_deleted = 0  and b.org_id = :orgId " : "") + "where a.is_deleted = 0 and a.username <> 'sadmin'";
//        if (id != null) {
//            sql += " and a.id = :id ";
//        }
//        if (state != null) {
//            sql += " and a.state = :state ";
//        }
//
//        if (StringUtils.hasText(keyword)) {
//            keyword = "%" + keyword + "%";
//            searchMap.put("keyword", keyword);
//            sql += " and ( a.username like :keyword or a.name like :keyword)";
//        }
//
//        long count = namedParameterJdbcTemplate.queryForObject("select count(a.id) " + sql, searchMap, Long.class);
//        sql = "select a.* " + sql + " limit " + pageable.getPageSize() * pageable.getPageNumber() + " , " + pageable.getPageSize();
//        List<SysUser> userDtos = namedParameterJdbcTemplate.query(sql, searchMap, new BeanPropertyRowMapper<>(SysUser.class));
//        return new PageImpl<>(userDtos, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()), count);
//    }

    public List<SysUser> matchListEnable(String keyword) {
        Conditions conditions = Conditions.empty().and("deleted").is(false).and("state").is(0);
        AssertUtil.isTrue(null != keyword, "搜索keyword不能为空");
        String likeStr = "%" + keyword + "%";
        conditions.and(Conditions.empty().or("name").like(likeStr));
        return repository.findAll(conditions);
    }

    public SysUser save(SysUser po) {
        return repository.save(po);
    }


    public List<SysUser> getByIdNumberAndName(String idNumber, String name, Long id) {
        return repository.findAll(Conditions.empty().and("idNumber").is(idNumber).and("name").is(name).and("id").ne(id));
    }

    public SysUser getAccountByMobile(String mobile) {
        return repository.findOne(Conditions.empty().and("deleted").is(false).and("state").is(0).and("mobile").is(mobile));
    }

    public List<SysUser> getByUsernames(Set<String> usernames) {
        Conditions conditions = Conditions.empty();
        conditions.and("deleted").is(false);
        conditions.and("username").in(usernames);
        return repository.findAll(conditions);
    }
}
