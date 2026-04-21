package com.hnyr.sys.file.service.impl;


import com.hnyr.sys.config.BusinessException;
import com.hnyr.sys.file.mongo.FileRecordDoc;
import com.hnyr.sys.file.service.FileExportContentService;
import com.hnyr.sys.file.service.FileExportService;
import com.hnyr.sys.minio.MinioFileClientService;
import com.hnyr.sys.minio.config.FileResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.io.*;
import java.util.Map;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class FileExportServiceImpl implements FileExportService {

    @Resource
    MinioFileClientService fileClient;
    @Resource
    MongoTemplate mongoTemplate;
    @Resource
    Map<String, FileExportContentService> fileExportContentServiceMap;

    public static final String COLLECTION_FILE = "file_common_export_log";

    private String getFileCollectionName() {
        return COLLECTION_FILE;
    }

    @Override
    public Page<FileRecordDoc> grid(Long userId, String fileType, Pageable pageable) {
        Criteria criteria = Criteria.where("userId").is(userId).and("fileType").is(fileType);
        Query query = Query.query(criteria);
        long total = mongoTemplate.count(query, FileRecordDoc.class, getFileCollectionName());
//        return PageableExecutionUtils.getPage(mongoTemplate.find(query.with(pageable),
//                FileRecordDoc.class, getFileCollectionName()), pageable, () -> total);
        return new PageImpl<>(mongoTemplate.find(query.with(pageable),
                FileRecordDoc.class, getFileCollectionName()), pageable, total);
    }

    /**
     * uploadFile
     *
     * @Author: demo
     * @Date: 19:50 20238/27
     * [file, filePath, publicRead]
     * @return: com.yuhong.aliyun.oss.FileResult
     **/
    private FileResult uploadFile(File file, String filePath, Boolean publicRead) {
        try {
            FileResult result = fileClient.saveFile(fileToByte(file), filePath, publicRead);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private FileResult uploadByte(byte[] bytes, String filePath, Boolean publicRead) {
        try {
            FileResult result = fileClient.saveFile(bytes, filePath, publicRead);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将文件转换成byte数组
     *
     * @param tradeFile
     * @return
     */
    public static byte[] fileToByte(File tradeFile) {
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(tradeFile);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    public void uploadExportFileByte(Long userId, String name, String fileType, byte[] fileByte, String filePath, FileRecordDoc fileRecordDoc) {
        uploadByte(fileByte, filePath, false);
        fileRecordDoc.setState(1);
        fileRecordDoc.setFileUrl(filePath);
        fileRecordDoc.setEndTime(new DateTime().getMillis());
        fileRecordDoc.setPublicRead(false);
        mongoTemplate.save(fileRecordDoc, getFileCollectionName());
    }

    public void uploadExportFileFailed(FileRecordDoc fileRecordDoc) {
        fileRecordDoc.setState(2);
        fileRecordDoc.setEndTime(new DateTime().getMillis());
        mongoTemplate.save(fileRecordDoc, getFileCollectionName());
    }

    private FileRecordDoc exportSave(Long userId, String fileType, Map<String, Object> paramBody, String name) {
        FileRecordDoc fileRecordDoc = new FileRecordDoc();
        fileRecordDoc.setUserId(userId);
        fileRecordDoc.setName(name);
        fileRecordDoc.setFileType(fileType);
        fileRecordDoc.setState(0);
        fileRecordDoc.setParams(paramBody);
        fileRecordDoc.setCreateTime(new DateTime().getMillis());
        return mongoTemplate.save(fileRecordDoc, getFileCollectionName());
    }

    @Override
    @Async
    public void exportFile(Long userId, Map<String, Object> paramBody) {
        FileRecordDoc fileRecordDoc = null;
        byte[] bytes = null;
        byte[] b = null;
        String filePath = null;
        String name = StringUtils.trimToEmpty((String) paramBody.get("name"));
        if (StringUtils.isEmpty(name)) {
            throw new BusinessException("缺少name");
        }
        String bean = StringUtils.trimToEmpty((String) paramBody.get("bean"));
        if (StringUtils.isEmpty(bean)) {
            throw new BusinessException("缺少bean");
        }
        String fileSuffix = StringUtils.trimToEmpty((String) paramBody.get("fileSuffix"));
        if (StringUtils.isEmpty(bean)) {
            throw new BusinessException("缺少文件后缀名");
        }
        name = name + "_" + userId + "_" + new DateTime().toString("yyyyMMddHHmmssSSS") + fileSuffix;

        try {
            FileExportContentService contentService = fileExportContentServiceMap.get(bean);
            Assert.notNull(contentService, "未找到处理类" + bean);
            fileRecordDoc = exportSave(userId, bean, paramBody, name);
            filePath = getFilePath(name);
            bytes = contentService.writeFile(userId, paramBody);
        } catch (Exception e) {
            log.error("e {}", e.getMessage());
            uploadExportFileFailed(fileRecordDoc);
            throw new BusinessException("导出出现异常");
        }

        if (bytes != null) {
            uploadExportFileByte(userId, name, bean, bytes, filePath, fileRecordDoc);
        }
    }

    private String getFilePath(String name) {
        return "/export/" + name;
    }

    /**
     * 单个docx文档生成，需要有模板
     *
     * @Author: demo
     * @Date: 9:17 202311/20
     * [userId, tenantId, appCode, paramBody]
     * @return: void
     **/
    @Override
    @Async
    public void exportFileDoc(Long userId, Map<String, Object> paramBody) {
        FileRecordDoc fileRecordDoc = null;
        byte[] bytes = null;
        String filePath = null;
        String name = StringUtils.trimToEmpty((String) paramBody.get("name"));
        if (StringUtils.isEmpty(name)) {
            throw new BusinessException("缺少name");
        }
        String bean = StringUtils.trimToEmpty((String) paramBody.get("bean"));
        if (StringUtils.isEmpty(bean)) {
            throw new BusinessException("缺少bean");
        }
        String fileSuffix = StringUtils.trimToEmpty((String) paramBody.get("fileSuffix"));
        if (StringUtils.isEmpty(bean)) {
            throw new BusinessException("缺少文件后缀名");
        }
        name = name + "_" + userId + "_" + new DateTime().toString("yyyyMMddHHmmssSSS") + fileSuffix;

        try {
            FileExportContentService contentService = fileExportContentServiceMap.get(bean);
            Assert.notNull(contentService, "未找到处理类" + bean);
            fileRecordDoc = exportSave(userId, bean, paramBody, name);
            filePath = getFilePath(name);
            bytes = contentService.writeFile(userId, paramBody);
        } catch (Exception e) {
            log.error("e {}", e.getMessage());
            uploadExportFileFailed(fileRecordDoc);
            throw new BusinessException("导出出现异常");
        }

        if (bytes != null) {
            uploadExportFileByte(userId, name, bean, bytes, filePath, fileRecordDoc);
        }
    }

    @Override
    @Async
    public void exportFileDocZip(Long userId, Map<String, Object> paramBody) {
        FileRecordDoc fileRecordDoc = null;
        byte[] bytes = null;
        String filePath = null;
        String name = StringUtils.trimToEmpty((String) paramBody.get("name"));
        if (StringUtils.isEmpty(name)) {
            throw new BusinessException("缺少name");
        }
        String bean = StringUtils.trimToEmpty((String) paramBody.get("bean"));
        if (StringUtils.isEmpty(bean)) {
            throw new BusinessException("缺少bean");
        }

        name = name + "_" + userId + "_" + new DateTime().toString("yyyyMMddHHmmssSSS") + ".zip";

        try {
            FileExportContentService contentService = fileExportContentServiceMap.get(bean);
            Assert.notNull(contentService, "未找到处理类" + bean);
            fileRecordDoc = exportSave(userId, bean, paramBody, name);
            filePath = getFilePath(name);
            bytes = contentService.writeFile(userId, paramBody);
        } catch (Exception e) {
            log.error("e {}", e.getMessage());
            uploadExportFileFailed(fileRecordDoc);
            throw new BusinessException("导出出现异常");
        }

        if (bytes != null) {
            uploadExportFileByte(userId, name, bean, bytes, filePath, fileRecordDoc);
        }
    }
}
