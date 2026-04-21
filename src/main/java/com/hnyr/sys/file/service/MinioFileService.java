package com.hnyr.sys.file.service;

import com.hnyr.sys.file.entity.dto.FileDto;
import com.hnyr.sys.file.entity.vo.FileVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName: MinioFileService
 * @Description:
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
public interface MinioFileService {

    /**
     * 获取文件地址
     *
     * @param recordId 文件recordId
     * @return
     */
    String getFileUrl(String recordId);

    /**
     *
     * @param recordId
     * @param expiredSeconds
     * @return
     */
    String getFileUrl(String recordId, Integer expiredSeconds);

    /**
     * 获取文件地址
     *
     * @param publicRead 是否为公共读
     * @param path       文件key
     * @return
     */
    String getFileUrl(Boolean publicRead, String path);

    FileDto add(FileDto file);

    /**
     * upload
     *
     * @Author: demo
     * @Date: 18:42 20237/1
     * [userId,  file]
     * @return: com.shyh.modules.resource.entity.dto.FileDto
     **/
//    FileDto upload(String appCode, Long userId, MultipartFile file);

    /**
     * 文件上传
     *
     * @param appCode
     * @param userId
     * @param file
     * @param publicRead
     * @return
     */
//    FileDto upload(String appCode, Long userId, MultipartFile file, Boolean publicRead);

    FileDto upload(String appCode, Long userId, MultipartFile file, Boolean publicRead, String category, Boolean scale);

    FileDto uploadByte(String appCode, Long userId, byte[] bytes, Boolean publicRead, String category, String fileName);

    FileDto uploadInputStream(String appCode, Long userId, InputStream inputStream, Boolean publicRead, String category, String fileName);

    /**
     * getImage
     *
     * @Author: demo
     * @Date: 18:42 20237/1
     * [recordId]
     * @return: com.shyh.modules.resource.entity.dto.FileDto
     **/
    FileDto getFile(String recordId);

    /**
     * getFileList
     *
     * @Author: demo
     * @Date: 20:15 20237/1
     * [recordIds]
     * @return: java.util.List<com.shyh.modules.resource.entity.dto.FileDto>
     **/
    List<FileDto> getFileList(Set<String> recordIds);

    /**
     * 分页获取文件
     *
     * @param pageable
     * @param appCode
     * @param typeName
     * @param categoryId 分类ID 必须
     * @param enable
     * @param publicRead
     * @param path
     * @param recordId
     * @return
     */
    Page<FileDto> page(Pageable pageable, String appCode, String typeName,
                       String categoryId, Boolean enable, Boolean publicRead, String path, String recordId);

    void deleteFile(String fileId, Long userId);

    void clearFile(String fileId, Long userId);

    Page<FileDto> page(Pageable pageable, Map<String, Object> searchMap);

    FileDto getFileResource(String fileId);

    FileDto copyFile(FileVo sourceVo, String rootPath, Long operator, String targetCategory);
}
