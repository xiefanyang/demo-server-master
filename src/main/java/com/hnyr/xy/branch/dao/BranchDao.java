package com.hnyr.xy.branch.dao;

import cn.hutool.core.map.MapUtil;
import com.hnyr.sys.data.Conditions;
import com.hnyr.xy.branch.dao.repository.BranchRepository;
import com.hnyr.xy.branch.entity.dto.BranchDto;
import com.hnyr.xy.branch.entity.po.Branch;
import org.springframework.data.domain.*;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: BranchDao
 * @Description:
 * @Author: demo
 * @CreateDate: 2023/10/11 17:40
 * @Version: 1.0
 */
@Component
public class BranchDao {
    /*************************************************************
     * 1、数据访问操作：避免在dao中做特定业务判定，通常仅作数据读写，尽可能降低数据库连接状态代码执行时长控制
     * 2、可使用jpa和jdbcTemplate2种方式，jdbc需控制sql联表数量原则上不超过3个，超过的可以通过拆分多次请求的方式
     **************************************************************/
    @Resource
    BranchRepository repository;
    @Resource
    JdbcTemplate jdbcTemplate;

    /**
     * 根据id获取
     *
     * @param id
     * @return
     */
    public Branch getById(Long id) {
        return repository.findOne(id);
    }

    /**
     * 根据rid获取
     *
     * @param recordId
     * @return
     */
    public Branch getByRid(String recordId) {
        return repository.findOne(Conditions.empty().and("recordId").is(recordId).and("deleted").is(false));
    }

    /**
     * 保存
     *
     * @param po
     */
    public void save(Branch po) {
        if (po.getDeleted() == null) {
            po.setDeleted(false);
        }
        repository.save(po);
    }

    public List<String> getManagerBranchIds(Long userId) {
        String sql = "select branch_id from t_bis_xy_branch_member where user_id = :userId and leader = 1 and enable = 1 and is_deleted = 0 ";
        Map<String, Object> params = new HashMap<>(1);
        params.put("userId", userId);
        return new NamedParameterJdbcTemplate(jdbcTemplate).queryForList(sql, params, String.class);
    }

    /**
     * 分页获取（jpa方式）
     *
     * @param pageable  分页对象
     * @param searchMap 检索参数
     * @return
     */
    public Page<Branch> page(Pageable pageable, Map<String, Object> searchMap) {
        Conditions conditions = Conditions.empty().and("deleted").is(false);
        List branchIds = MapUtil.get(searchMap, "branchIds", List.class);
        if (!CollectionUtils.isEmpty(branchIds)) {
            conditions.and("record_id").in(branchIds);
        }
        String keyword = MapUtil.getStr(searchMap, "keyword");
        if (StringUtils.hasText(keyword)) {
            conditions.and("name").like("%" + keyword + "%");
        }
        Boolean enable = MapUtil.getBool(searchMap, "enable");
        if (enable != null) {
            conditions.and("enable").is(enable);
        }

        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(Sort.Order.asc("parentId"), Sort.Order.asc("sort")));
        return repository.findAll(conditions, pageable);
    }

    /**
     * jdbcTemplate 例子
     *
     * @param pageable
     * @param searchMap
     * @return
     */
    public Page<BranchDto> pageJdbc(Pageable pageable, Map<String, Object> searchMap) {
        NamedParameterJdbcTemplate n = new NamedParameterJdbcTemplate(jdbcTemplate);
        String sql = " from t_bis_xy_branch where is_deleted = 0 ";
        String keyword = MapUtil.getStr(searchMap, "keyword");
        if (StringUtils.hasText(keyword)) {
            sql += " and name like :keyword ";
            searchMap.put("keyword", "%" + keyword + "%");
        }
        Boolean enable = MapUtil.getBool(searchMap, "enable");
        if (enable != null) {
            sql += " and enable = :enable ";
        }
        List branchIds = MapUtil.get(searchMap, "branchIds", List.class);
        if (!CollectionUtils.isEmpty(branchIds)) {
            sql += " and record_id in (:branchIds)";
        }
        long count = n.queryForObject("select  count(1) " + sql, searchMap, Long.class);
        List<BranchDto> list;
        if (count == 0) {
            list = new ArrayList<>();
        } else {
            list = n.query("select * " + sql + " order by parent_id,sort, id, desc limit " + pageable.getPageSize() * pageable.getPageNumber() + " , " + pageable.getPageSize(), searchMap, new BeanPropertyRowMapper<>(BranchDto.class));
        }
        return new PageImpl<>(list, pageable, count);
    }
}
