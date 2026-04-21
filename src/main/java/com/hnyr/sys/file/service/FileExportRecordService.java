package com.hnyr.sys.file.service;

import com.hnyr.sys.file.entity.dto.FileExportRecordDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

/**
 * @ClassName: FileExportRecordService
 * @Description:
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
public interface FileExportRecordService {
    /**
     * 将数据加入处理队列：前台上传成功后调用（如 开始处理前调用，则state 0 否则直接启动 1）
     * creator、state、bis不可为空
     *
     * @param dto
     * @return
     */

    FileExportRecordDto addToQueue(FileExportRecordDto dto);

    /**
     * 更新数据：处理成功后调用
     *
     * @param dto
     * @return
     */
    FileExportRecordDto updateFileData(FileExportRecordDto dto);

    /**
     * 获取数据处理状态
     *
     * @param recordId
     * @return
     */
    FileExportRecordDto getFileData(String recordId);

    /**
     * 检索上传处理记录
     *
     * @param pageable
     * @param searchMap 必须包含creator 上传人 bis 上传业务名
     * @return
     */
    Page<FileExportRecordDto> page(Pageable pageable, Map<String, Object> searchMap);
}
