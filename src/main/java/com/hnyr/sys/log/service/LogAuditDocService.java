package com.hnyr.sys.log.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import com.hnyr.sys.log.mongo.LogAuditDoc;
import com.hnyr.sys.rbac.RbacConstant;
import com.hnyr.sys.rbac.dao.SysUserDao;
import com.hnyr.sys.rbac.entity.dto.SysRoleDto;
import com.hnyr.sys.rbac.entity.po.SysUser;
import com.hnyr.sys.rbac.service.SysRoleUserService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

/**
 * @ClassName: LogAuditDocService
 * @Description: 操作日志service
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class LogAuditDocService {

    @Resource
    MongoTemplate mongoTemplate;

    @Resource
    private SysRoleUserService sysRoleUserService;

    @Resource
    private SysUserDao sysUserDao;

    private static final String[] IMPORT_ROLE_CODES = {RbacConstant.ROLE_CODE_SYS_ADMIN, RbacConstant.ROLE_CODE_SYS_SECURITY, RbacConstant.ROLE_CODE_SYS_AUDIT};

    private String getLogAuditCollection() {
        return LogAuditDoc.COLLECTION_NAME + "_" + DateTime.now().toString("yyyyMM");
    }

    private String getLogAuditCollectionImport() {
        return LogAuditDoc.COLLECTION_NAME_IMPORT + "_" + DateTime.now().toString("yyyyMM");
    }

    /**
     * 保存日志对象
     *
     * @param logAudit
     */
    public void saveRecord(LogAuditDoc logAudit) {
        LogAuditDoc doc = new LogAuditDoc();
        BeanUtil.copyProperties(logAudit, doc);
        // 判断是否存在用户id，如果用户存在，获取用户角色列表，判断角色中是否包含三员的角色，如果是单独存储到三员日志中
        // 三员日志存储遵循安全要求保留一定周期
        if (doc.getUserId() != null) {
            //获取用户的角色列表
            List<String> roleCodes = sysRoleUserService.getRoleCodeByUserId(doc.getUserId());
            List<String> importRole = Arrays.asList(IMPORT_ROLE_CODES);
            Boolean flag = false;
            for (String roleCode : roleCodes) {
                if (importRole.contains(roleCode)) {
                    doc.setRoleCode(roleCode);
                    flag = true;
                    break;
                }
            }
            if (flag) {
                mongoTemplate.insert(doc, getLogAuditCollectionImport());
            }
        }
        mongoTemplate.insert(doc, getLogAuditCollection());
    }

    public LogAuditDoc findOne(String id) {
        return mongoTemplate.findById(id, LogAuditDoc.class, getLogAuditCollection());
    }

    /**
     * 日志检索数据获取（分页）
     *
     * @return
     */
    public Page<LogAuditDoc> findRecordPage(Map<String, Object> searchMap) {
        Integer page = MapUtil.getInt(searchMap, "current");
        Integer size = MapUtil.getInt(searchMap, "pageSize");
        Long operator = MapUtil.getLong(searchMap, "operator");
        Boolean success = MapUtil.getBool(searchMap, "success");
        String name = MapUtil.getStr(searchMap, "name");
        String signature = MapUtil.getStr(searchMap, "signature");
        Integer time = MapUtil.getInt(searchMap, "time");
        Long begin = StringUtils.hasText(MapUtil.getStr(searchMap, "begin")) ? DateTime.parse(MapUtil.getStr(searchMap, "begin"), DateTimeFormat.forPattern("yyyy-MM-dd")).withTimeAtStartOfDay().getMillis() : null;
        Long end = StringUtils.hasText(MapUtil.getStr(searchMap, "end")) ? DateTime.parse(MapUtil.getStr(searchMap, "end"), DateTimeFormat.forPattern("yyyy-MM-dd")).withTimeAtStartOfDay().plusDays(1).getMillis() : null;
        String ip = MapUtil.getStr(searchMap, "ip");
        String url = MapUtil.getStr(searchMap, "url");
        String application = MapUtil.getStr(searchMap, "application");

        if (page == null) {
            page = 0;
        }
        if (size == null) {
            size = 50;
        }
        Criteria criteria = new Criteria();
        if (StringUtils.hasText(application)) {
            criteria.and("application").regex(application, "i");
        }
        if (operator != null) {
            criteria.and("userId").is("NumberLong(" + operator + ")");
        }
        if (success != null) {
            criteria.and("success").is(success);
        }
        if (time != null) {
            criteria.and("time").gt(time);
        }
        if (StringUtils.hasText(name)) {
            criteria.and("name").regex(name, "i");
        }
        if (StringUtils.hasText(url)) {
            criteria.and("url").regex(url, "i");
        }
        if (StringUtils.hasText(ip)) {
            criteria.and("ip").is(ip);
        }
        if (StringUtils.hasText(signature)) {
            criteria.and("signature").regex(signature, "i");
        }
        if (null != begin) {
            criteria.and("begin").gte(begin);
        }
        if (null != end) {
            criteria.and("end").lte(end);
        }
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.DESC, "begin");
        List<LogAuditDoc> docList = mongoTemplate.find(Query.query(criteria).with(pageable), LogAuditDoc.class, getLogAuditCollection());
        long count = mongoTemplate.count(Query.query(criteria), LogAuditDoc.class, getLogAuditCollection());
        return new PageImpl<>(docList, pageable, count);
    }

    /**
     * 根据应用、操作、类、时间段获取访问用户集合（用于访问量的数据获取）userId count
     *
     * @param application
     * @param name
     * @param signature
     * @param begin
     * @param end
     * @return
     */
    public List<Map> findUsersForBusiness(String application, String name, String signature, Date begin, Date end) {
        Criteria criteria = Criteria.where("application").is(application).and("name").is(name).and("signature").is(signature);
        if (begin != null) {
            criteria.and("begin").gte(begin);
        }
        if (end != null) {
            criteria.and("end").lte(end);
        }
        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
//                sort(Sort.DEFAULT_DIRECTION),
                group("userId")
                        .count().as("count")
                        .first("userId").as("userId"),
                project("userId", "count")
        );
        AggregationResults<Map> aggregate = mongoTemplate.aggregate(aggregation, getLogAuditCollection(), Map.class);
        return aggregate.getMappedResults();
    }

    public Page<LogAuditDoc> findImportRecordPage(Map<String, Object> searchMap) {
        Integer page = MapUtil.getInt(searchMap, "current");
        Integer size = MapUtil.getInt(searchMap, "pageSize");
        Long operator = MapUtil.getLong(searchMap, "operator");
        Boolean success = MapUtil.getBool(searchMap, "success");
        String name = MapUtil.getStr(searchMap, "name");
        String signature = MapUtil.getStr(searchMap, "signature");
        Integer time = MapUtil.getInt(searchMap, "time");
        Long begin = StringUtils.hasText(MapUtil.getStr(searchMap, "begin")) ? DateTime.parse(MapUtil.getStr(searchMap, "begin"), DateTimeFormat.forPattern("yyyy-MM-dd")).withTimeAtStartOfDay().getMillis() : null;
        Long end = StringUtils.hasText(MapUtil.getStr(searchMap, "end")) ? DateTime.parse(MapUtil.getStr(searchMap, "end"), DateTimeFormat.forPattern("yyyy-MM-dd")).withTimeAtStartOfDay().plusDays(1).getMillis() : null;
        String ip = MapUtil.getStr(searchMap, "ip");
        String url = MapUtil.getStr(searchMap, "url");
        String application = MapUtil.getStr(searchMap, "application");
        String roleCode = MapUtil.getStr(searchMap, "roleCode");

        if (page == null) {
            page = 0;
        }
        if (size == null) {
            size = 50;
        }
        Criteria criteria = new Criteria();
        if (StringUtils.hasText(application)) {
            criteria.and("application").regex(application, "i");
        }
        if (StringUtils.hasText(roleCode)) {
            criteria.and("roleCode").is(roleCode);
        }
        if (operator != null) {
            criteria.and("userId").is("NumberLong(" + operator + ")");
        }
        if (success != null) {
            criteria.and("success").is(success);
        }
        if (time != null) {
            criteria.and("time").gt(time);
        }
        if (StringUtils.hasText(name)) {
            criteria.and("name").regex(name, "i");
        }
        if (StringUtils.hasText(url)) {
            criteria.and("url").regex(url, "i");
        }
        if (StringUtils.hasText(ip)) {
            criteria.and("ip").is(ip);
        }
        if (StringUtils.hasText(signature)) {
            criteria.and("signature").regex(signature, "i");
        }
        if (null != begin) {
            criteria.and("begin").gte(begin);
        }
        if (null != end) {
            criteria.and("end").lte(end);
        }
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.DESC, "begin");
        List<LogAuditDoc> docList = mongoTemplate.find(Query.query(criteria).with(pageable), LogAuditDoc.class, getLogAuditCollectionImport());
        long count = mongoTemplate.count(Query.query(criteria), LogAuditDoc.class, getLogAuditCollectionImport());
        if (!CollectionUtils.isEmpty(docList)) {
            Set<Long> userIds = new HashSet<>();
            Set<String> roleCodes = new HashSet<>();
            docList.forEach(e -> {
                userIds.add(e.getUserId());
                roleCodes.add(e.getRoleCode());
            });
            List<SysRoleDto> roleList = sysRoleUserService.getRoleListByRoleCodes(roleCodes);
            List<SysUser> userList = sysUserDao.getInIds(new ArrayList<>(userIds), true);
            Map<String, SysRoleDto> roleMap = new HashMap<>();
            roleList.forEach(e -> {
                roleMap.put(e.getCode(), e);
            });
            Map<Long, SysUser> userMap = new HashMap<>();
            userList.forEach(e -> {
                userMap.put(e.getId(), e);
            });
            docList.forEach(e -> {
                if (roleMap.get(e.getRoleCode()) != null) {
                    e.setRoleName(roleMap.get(e.getRoleCode()).getName());
                }
                if (userMap.get(e.getUserId()) != null) {
                    e.setUserRealName(userMap.get(e.getUserId()).getName());
                }
            });
        }
        return new PageImpl<>(docList, pageable, count);
    }

}
