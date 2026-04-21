package com.hnyr.sys.file.controller;

import com.hnyr.sys.config.BusinessException;
import com.hnyr.sys.config.ResponseResult;
import com.hnyr.sys.file.entity.dto.FileDto;
import com.hnyr.sys.file.service.MinioFileService;
import com.hnyr.sys.file.util.FileUrl;
import com.hnyr.sys.log.AuditLog;
import com.hnyr.sys.minio.config.MinioConst;
import com.hnyr.sys.rbac.entity.vo.TokenUserVo;
import com.hnyr.sys.rbac.security.SecurityConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: FileEditorController
 * @Description: 富文本编辑器文件上传下载
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
@RestController
@Slf4j
@Api(tags = "富文本上传下载相关")
@RequestMapping("/api")
public class FileEditorController {

    @Resource
    MinioFileService fileService;

    @PostMapping("/sys/file/editor/upload/img")
    @AuditLog
    @Deprecated
    @ApiOperation("富文本图片上传")
    public ResponseResult<FileDto> fileUploadImg(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu, @RequestParam("file") MultipartFile file) {
        String category = "富文本图片";
        FileDto fileDto = null;
        try {
            fileDto = fileService.upload(null, tu.getId(), file, true, category, true);
        } catch (Exception e) {
            log.error("上传异常 {}", e.getMessage());
            throw new BusinessException("上传出现异常，请重试");
        }
        return ResponseResult.success(fileDto);
    }

    @PostMapping("/sys/file/editor/upload/video")
    @AuditLog
    @Deprecated
    @ApiOperation("富文本视频上传")
    public ResponseResult<?> fileUploadVideo(Long uid, @RequestParam("files") MultipartFile[] files) {
        String category = "富文本视频";
        FileDto fileDto = null;
        List<Map<String, Object>> fs = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                fileDto = fileService.upload(null, uid, file, true, category, false);
                Map<String, Object> m = new HashMap<>();
                m.put("url", FileUrl.toImageUrl(fileDto.getRecordId()));
                fs.add(m);
            }
        } catch (Exception e) {
            log.error("上传异常 {}", e.getMessage());
            throw new BusinessException("上传出现异常，请重试");
        }
        return ResponseResult.success(fs);
    }

}
