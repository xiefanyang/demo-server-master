package com.hnyr.sys.file.controller;

import cn.hutool.core.map.MapUtil;
import com.hnyr.sys.config.ResponseResult;
import com.hnyr.sys.file.entity.dto.FileDto;
import com.hnyr.sys.file.service.MinioFileService;
import com.hnyr.sys.log.AuditLog;
import com.hnyr.sys.rbac.entity.vo.TokenUserVo;
import com.hnyr.sys.rbac.security.AuthPermissions;
import com.hnyr.sys.rbac.security.SecurityConstant;
import com.hnyr.sys.utils.DataConvertor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: SysFileController
 * @Description: 文件管理控制器
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
@RestController
@Api(tags = "文件管理")
@RequestMapping("/api")
public class SysFileController {

    @Resource
    private MinioFileService fileService;

    @PostMapping("/sys/file/page")
    @ApiOperation("获取文件列表")
    @AuditLog
    @AuthPermissions("sys.system.file")
    public ResponseResult<Page<FileDto>> page(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu, @RequestBody Map<String, Object> searchMap) {
        Pageable pageable = DataConvertor.parseInSearchMap(searchMap);
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Order.desc("createTime")));
        return ResponseResult.success(fileService.page(pageable, searchMap));
    }

    @AuditLog
    @ApiOperation("文件移除")
    @PostMapping("/sys/file/delete")
    @AuthPermissions("sys.system.file")
    public ResponseResult<Boolean> delete(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu, @RequestParam("recordId") String recordId) {
        fileService.deleteFile(recordId, tu.getId());
        return ResponseResult.success();
    }

    @PostMapping("/sys/file/clear")
    @AuditLog
    @ApiOperation("文件彻底删除")
    @AuthPermissions("sys.system.file")
    public ResponseResult<Boolean> clear(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu, @RequestParam("recordId") String recordId) {
        fileService.clearFile(recordId, tu.getId());
        return ResponseResult.success();
    }

    @AuditLog
    @ApiOperation("文件批量移除")
    @PostMapping("/sys/file/delete/batch")
    @AuthPermissions("sys.system.file")
    public ResponseResult<Boolean> deleteBatch(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu, @RequestBody Map<String, Object> map) {
        List<String> recordIds = MapUtil.get(map, "recordIds", List.class);
        if (!CollectionUtils.isEmpty(recordIds)) {
            for (String recordId : recordIds) {
                fileService.deleteFile(recordId, tu.getId());
            }
        }

        return ResponseResult.success();
    }

    @PostMapping("/sys/file/clear/batch")
    @AuditLog
    @ApiOperation("文件批量彻底删除")
    @AuthPermissions("sys.system.file")
    public ResponseResult<Boolean> clearBatch(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu, @RequestBody Map<String, Object> map) {
        List<String> recordIds = MapUtil.get(map, "recordIds", List.class);
        if (!CollectionUtils.isEmpty(recordIds)) {
            for (String recordId : recordIds) {
                fileService.clearFile(recordId, tu.getId());
            }
        }
        return ResponseResult.success();
    }
}
