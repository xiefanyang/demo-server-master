package com.hnyr.sys.log.controller;

import com.hnyr.sys.config.ResponseResult;
import com.hnyr.sys.log.AuditLog;
import com.hnyr.sys.log.mongo.LogAuditDoc;
import com.hnyr.sys.log.service.LogAuditDocService;
import com.hnyr.sys.rbac.entity.vo.TokenUserVo;
import com.hnyr.sys.rbac.security.AuthPermissions;
import com.hnyr.sys.rbac.security.SecurityConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: LogAuditController
 * @Description: 操作日志检索与审计导出等
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
@RestController
@Api(tags = "操作日志")
@RequestMapping("/api")
public class LogAuditController {

    @Resource
    private LogAuditDocService recordDocService;

    @PostMapping("/sys/log/audit/list")
    @ApiOperation("检索操作日志")
    @AuditLog
    @AuthPermissions(value = "sys.audit.search")
    public ResponseResult<Page<LogAuditDoc>> findRecordPage(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tokenUser, @RequestBody Map<String, Object> searchMap) {
        Page<LogAuditDoc> recordDocs = recordDocService.findRecordPage(searchMap);
        return ResponseResult.success(recordDocs);
    }

    @PostMapping("/sys/log/audit/import/list")
    @ApiOperation("审计员检索三员操作日志")
    @AuditLog
    @AuthPermissions(value = "sys.audit.import.search")
    public ResponseResult<Page<LogAuditDoc>> findImportRecordPage(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tokenUser, @RequestBody Map<String, Object> searchMap) {
        Page<LogAuditDoc> recordDocs = recordDocService.findImportRecordPage(searchMap);
        return ResponseResult.success(recordDocs);
    }

    @ApiOperation("检索操作日志详情")
    @AuditLog
    @GetMapping("/sys/log/audit/detail")
    @AuthPermissions(value = "sys.audit.search")
    public ResponseResult<LogAuditDoc> findRecord(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tokenUser, @RequestParam(value = "id", required = false) String id) {
        return ResponseResult.success(recordDocService.findOne(id));
    }

    @ApiOperation("获取操作日志用户访问统计")
    @AuditLog
    @GetMapping("/sys/log/audit/access/user/record")
    @AuthPermissions(value = "sys.audit.search")
    public ResponseResult<List<Map>> groupByUserForBusiness(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tokenUser,
                                                            @RequestParam(value = "application", required = false) String application,
                                                            @RequestParam(value = "name", required = false) String name,
                                                            @RequestParam(value = "signature", required = false) String signature,
                                                            @RequestParam(value = "begin", required = false) String begin,
                                                            @RequestParam(value = "end", required = false) String end) {
        List<Map> business = recordDocService.findUsersForBusiness(
                application,
                name,
                signature,
                begin != null ? DateTime.parse(begin, DateTimeFormat.forPattern("yyyy-MM-dd")).toDate() : null,
                end != null ? DateTime.parse(end, DateTimeFormat.forPattern("yyyy-MM-dd")).toDate() : null);
        // 返回示例:
        // [0] userId=张三, count=12
        // [1] userId=李四三, count=9
        // ...
        return ResponseResult.success(business);
    }

    // TODO 缺少导出
}
