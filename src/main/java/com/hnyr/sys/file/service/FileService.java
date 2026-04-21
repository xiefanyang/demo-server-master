package com.hnyr.sys.file.service;

import com.hnyr.sys.file.entity.dto.FileDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * @ClassName: FileService
 * @Description:
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
public interface FileService {

    /**
     * 上传文件
     *
     * @param operator 上传人
     * @param category 上传文件的分类
     * @param file
     * @return
     */
    FileDto upload(Long operator, String category, MultipartFile file) throws Exception;

    /**
     * 获取文件资源
     *
     * @param fileId
     * @return
     */
    FileDto getFileResource(String fileId);

    List<FileDto> getFileList(Set<String> recordIds);

    FileDto getFile(String recordId);

    /**
     * 分页获取文件
     *
     * @param pageable
     * @param searchMap
     * @return
     */
    Page<FileDto> page(Pageable pageable, Map<String, Object> searchMap);

    void deleteFile(String fileId, Long userId);

    void clearFile(String fileId, Long userId);
}
