package com.hnyr.sys.file.service;

import java.util.Map;

/**
 * @ClassName: FileExportContentService
 * @Description:
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
public interface FileExportContentService {
    /**
     * 导出文件处理
     *
     * @param userId
     * @param paramBody
     * @return
     * @throws Exception
     */
    byte[] writeFile(Long userId, Map<String, Object> paramBody) throws Exception;
}
