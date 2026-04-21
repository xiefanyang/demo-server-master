package com.hnyr.sys.file.service;

import com.hnyr.sys.file.mongo.FileRecordDoc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

/**
 * @ClassName: MinioFileService
 * @Description:
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
public interface FileExportService {

    Page<FileRecordDoc> grid(Long userId, String fileType, Pageable pageable);

    void exportFile(Long userId, Map<String, Object> paramBody);

    /**
     * doc文件为模板填充模式，需要模板或者其他处理方案
     * @Author: demo
     * @Date: 10:37 202311/20
     * [userId, tenantId, appCode, paramBody]
     * @return: void
     **/
    void exportFileDoc(Long userId, Map<String, Object> paramBody);

    void exportFileDocZip(Long userId, Map<String, Object> paramBody);
}
