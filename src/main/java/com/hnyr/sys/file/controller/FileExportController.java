package com.hnyr.sys.file.controller;


import cn.hutool.core.map.MapUtil;
import com.hnyr.sys.config.ResponseResult;
import com.hnyr.sys.file.mongo.FileRecordDoc;
import com.hnyr.sys.file.service.FileExportService;
import com.hnyr.sys.log.AuditLog;
import com.hnyr.sys.rbac.entity.vo.TokenUserVo;
import com.hnyr.sys.rbac.security.SecurityConstant;
import com.hnyr.sys.utils.DataConvertor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @ClassName: FileExportController
 * @Description: 文件导入导出
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
@Api(tags = "文件-导出/导入")
@RequestMapping("/api")
@RestController
public class FileExportController {


    @Resource
    FileExportService fileExportService;

    @PostMapping("/sys/file/export/gird")
    @ApiOperation("获取文件列表")
    public ResponseResult<Page<FileRecordDoc>> getInfoGrid(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu,
                                                           @RequestBody Map<String, Object> searchMap) {
        searchMap.put("sortField", "createTime");
        searchMap.put("sortOrder", "descend");
        Pageable pageable = DataConvertor.parseInSearchMap(searchMap);
        String fileType = MapUtil.getStr(searchMap, "fileType");
        return ResponseResult.success(fileExportService.grid(tu.getId(), fileType, pageable));
    }

    @ApiOperation("文件-导出")
    @PostMapping("/sys/file/export")
    @AuditLog
    public ResponseResult<?> courseAll(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu,
                                       @RequestBody Map<String, Object> paramBody) {
        fileExportService.exportFile(tu.getId(), paramBody);
        return ResponseResult.success();
    }

    @ApiOperation("文件-doc-导出")
    @PostMapping("/sys/file/doc/export")
    @AuditLog
    public ResponseResult<?> fileDocExport(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu,
                                           @RequestBody Map<String, Object> paramBody) {
        fileExportService.exportFileDoc(tu.getId(), paramBody);
        return ResponseResult.success();
    }
}
