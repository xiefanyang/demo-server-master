package com.hnyr.sys.file.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import com.hnyr.sys.config.BusinessException;
import com.hnyr.sys.config.ResponseResult;
import com.hnyr.sys.file.entity.dto.FileDataProcessRecordDto;
import com.hnyr.sys.file.entity.dto.FileDto;
import com.hnyr.sys.file.entity.vo.FileVo;
import com.hnyr.sys.file.service.FileDataProcessRecordService;
import com.hnyr.sys.file.service.MinioFileService;
import com.hnyr.sys.log.AuditLog;
import com.hnyr.sys.rbac.entity.vo.TokenUserVo;
import com.hnyr.sys.rbac.security.SecurityConstant;
import com.hnyr.sys.utils.AssertUtil;
import com.hnyr.sys.utils.DataConvertor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName: FileController
 * @Description: 文件上传下载
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
@Api(tags = "文件上传下载")
@RequestMapping({"/api", "/api/wap"})
@RestController
@Slf4j
public class FileController {


//    @Resource
//    FileService fileService;

    @Resource
    MinioFileService fileService;


    @Resource
    private FileDataProcessRecordService fileDataProcessRecordService;

    @PostMapping(value = "/sys/file/upload/{category}", headers = "content-type=multipart/form-data")
    @AuditLog
    @ApiOperation("文件上传")
    public Object fileUpload(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu, @PathVariable String category, @RequestPart(name = "file") MultipartFile file) {
        FileDto fileDto = null;
        try {
            //默认私有
            fileDto = fileService.upload(null, tu.getId(), file, false, category + "/" + tu.getId(), true);
        } catch (Exception e) {
            log.error("上传异常 {}", e.getMessage());
            throw new BusinessException("上传出现异常，请重试");
        }
        FileVo vo = BeanUtil.copyProperties(fileDto, FileVo.class);
        vo.setFileUrl(fileService.getFileUrl(fileDto.getRecordId()));
        return ResponseResult.success(vo);
    }

    @PostMapping(value = "/sys/file/upload/{category}/{publicRead}", headers = "content-type=multipart/form-data")
    @AuditLog
    @ApiOperation("文件上传")
    public Object fileUploadByPublicRead(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu, @PathVariable String category, @PathVariable Boolean publicRead, @RequestPart(name = "file") MultipartFile file) {
        FileDto fileDto = null;
        try {
            fileDto = fileService.upload(null, tu.getId(), file, publicRead, category + "/" + tu.getId(), true);
        } catch (Exception e) {
            log.error("上传异常 {}", e.getMessage());
            throw new BusinessException("上传出现异常，请重试");
        }
        FileVo vo = BeanUtil.copyProperties(fileDto, FileVo.class);
        vo.setFileUrl(fileService.getFileUrl(fileDto.getRecordId()));
        return ResponseResult.success(vo);
    }

    @PostMapping(value = "/sys/file/upload/{category}/{publicRead}/{scale}", headers = "content-type=multipart/form-data")
    @AuditLog
    @ApiOperation("文件上传")
    public Object fileUploadByPublicRead(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu, @PathVariable String category, @PathVariable Boolean publicRead, @PathVariable Boolean scale, @RequestPart(name = "file") MultipartFile file) {
        FileDto fileDto = null;
        try {
            fileDto = fileService.upload(null, tu.getId(), file, publicRead, category + "/" + tu.getId(), scale);
        } catch (Exception e) {
            log.error("上传异常 {}", e.getMessage());
            throw new BusinessException("上传出现异常，请重试");
        }
        FileVo vo = BeanUtil.copyProperties(fileDto, FileVo.class);
        vo.setFileUrl(fileService.getFileUrl(fileDto.getRecordId()));
        return ResponseResult.success(vo);
    }

//
//    /**
//     * 获取文件地址
//     *
//     * @param fileId
//     * @return
//     */
//    @PostMapping("/sys/file/url")
//    public ResponseResult<String> getFileUrl(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu, @RequestParam("fileId") String fileId) {
//        return ResponseResult.success(FileUrl.toFileUrl(fileId));
//    }

    //查询并下载文件
    @GetMapping("/sys/file/download/{fileId}")
    @AuditLog
    @ApiOperation("下载文件")
    public void download(@PathVariable String fileId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //开始时间
        long begin = System.currentTimeMillis();
        FileDto fileDto = fileService.getFile(fileId);
        if (fileDto != null && fileDto.getDeleted() == false) {
            //查询文件
//            FileDto gridFsResource = fileService.getFileResource(fileId);
//            if (gridFsResource != null) {
            String contentType = fileDto.getType();
            response.reset();
            response.setContentType(contentType);
            //注意: 如果没有下面这行设置header, 结果会将文件的内容作为响应的 body 直接输出在页面上, 而不是下载文件
//                response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
            response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + URLEncoder.encode(fileDto.getOriginalName(), "UTF-8"));  //指定下载文件名
//                response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + URLEncoder.encode(fileDto.getOriginalName(), "UTF-8"));  //指定下载文件名
//                response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + new String(fileDto.getOriginalName().getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));  //指定下载文件名
            ServletOutputStream outputStream = response.getOutputStream();
            BufferedInputStream is = getFileStream(fileDto.getFileUrl());
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = is.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
            }
            is.close();
            outputStream.close();
            //下载结束
            long end = System.currentTimeMillis();
            long time = end - begin;
            String img = "图片";
            if (!img.equals(fileDto.getTypeName())) {
                log.info("文件: {} [{} {}] 读取下载耗时:{} 秒", fileDto.getOriginalName(), fileDto.getTypeName(), fileDto.getSizeStr(), time / 1000);
            }
//            }
        }
    }

    @GetMapping("/sys/file/common/image/{fileId}")
    @ApiOperation("读取公共图片")
    public void downloadCommonImg(@PathVariable String fileId, HttpServletResponse response) throws Exception {
        //开始时间
        long begin = System.currentTimeMillis();
        FileDto fileDto = fileService.getFile(fileId);
        if (fileDto != null && fileDto.getDeleted() == false) {
            //查询文件
            FileDto gridFsResource = fileService.getFileResource(fileId);
            if (gridFsResource != null) {
                String contentType = gridFsResource.getType();
                response.reset();
                response.setContentType(contentType);
                //注意: 如果没有下面这行设置header, 结果会将文件的内容作为响应的 body 直接输出在页面上, 而不是下载文件
//                response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
                response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + new String(fileDto.getOriginalName().getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));  //指定下载文件名
//                response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + new String(fileDto.getOriginalName().getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));  //指定下载文件名
                ServletOutputStream outputStream = response.getOutputStream();
                BufferedInputStream is = getFileStream(fileDto.getFileUrl());
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = is.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, len);
                }
                is.close();
                outputStream.close();
                //下载结束
                long end = System.currentTimeMillis();
                long time = end - begin;
                log.info("文件: {} [{} {}] 读取下载耗时:{} 秒", fileDto.getOriginalName(), fileDto.getTypeName(), fileDto.getSizeStr(), time / 1000);
            }
        }
    }

    @PostMapping("/sys/file/process/{recordId}")
    @ApiOperation("获取上传数据处理结果")
    public Object processRecord(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu, @PathVariable("recordId") String recordId) {
        FileDataProcessRecordDto fileData = fileDataProcessRecordService.getFileData(recordId);
        AssertUtil.isTrue(tu.getId().longValue() == fileData.getCreator(), "非本人数据不可读取");
        return fileData;
    }


    @PostMapping("/sys/file/process-page")
    @ApiOperation("获取上传数据处理历史")
    public Object pageProcess(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu, @RequestBody Map<String, Object> searchMap) {
        Pageable pageable = DataConvertor.parseInSearchMap(searchMap);
        searchMap.put("creator", tu.getId());
        AssertUtil.isTrue(StringUtils.hasText(MapUtil.getStr(searchMap, "bis")), "缺少业务标识");
        return fileDataProcessRecordService.page(pageable, searchMap);
    }

    @PostMapping("/sys/file/get/files")
    @ApiOperation("获取文件列表")
    public List<FileDto> getFileList(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu, @RequestBody List<String> fileIds) {
        Set<String> ids = new HashSet<>();
        if (CollectionUtils.isEmpty(fileIds)) {
            return null;
        }
//        String[] arr = fileIds.split(",");
//        for (String s : arr) {
//            ids.add(s);
//        }
        List<FileDto> fileDtos = fileService.getFileList(ids);
        return fileDtos;
    }


    public BufferedInputStream getFileStream(String webUrl) throws Exception {
        try {
            //建立链接
            URL url = new URL(webUrl);
            HttpURLConnection httpUrl = (HttpURLConnection) url.openConnection();
            //连接指定的资源
            httpUrl.connect();
            //获取网络输入流
            BufferedInputStream bis = new BufferedInputStream(httpUrl.getInputStream());
            return bis;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping("/sys/file/url")
    @ApiOperation(value = "获取文件地址")
    public ResponseResult<String> getFileUrl(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu,
                                             @RequestBody Map<String, Object> searchMap) {
        Boolean publicRead = MapUtil.getBool(searchMap, "publicRead");
        String path = MapUtil.getStr(searchMap, "path");
        return ResponseResult.success(fileService.getFileUrl(publicRead, path));
    }

    @PostMapping("/sys/file/get")
    @ApiOperation(value = "获取文件信息")
    public ResponseResult<String> getFile(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu,
                                          @RequestBody Map<String, Object> searchMap) {
        String fileId = MapUtil.getStr(searchMap, "fileId");
        FileDto fileDto = fileService.getFile(fileId);
        AssertUtil.notNull(fileDto, "未找到文件");
        FileVo vo = BeanUtil.copyProperties(fileDto, FileVo.class);
        vo.setFileUrl(fileService.getFileUrl(fileDto.getRecordId()));
        return ResponseResult.success(vo);
    }
}
