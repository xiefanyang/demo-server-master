package com.hnyr.sys.file.service;

import com.hnyr.sys.file.entity.dto.FileDataProcessRecordDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

/**
 * @ClassName: FileDataProcessRecordService
 * @Description:
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
public interface FileDataProcessRecordService {
    FileDataProcessRecordDto addFileData(FileDataProcessRecordDto dto);

    /**
     * 将数据加入处理队列：前台上传成功后调用（如 开始处理前调用，则state 0 否则直接启动 1）
     * creator、state、bis不可为空
     *
     * @param dto
     * @return
     */

    FileDataProcessRecordDto addToQueue(FileDataProcessRecordDto dto);

    /**
     * 更新数据：处理成功后调用
     *
     * @param dto
     * @return
     */
    FileDataProcessRecordDto updateFileData(FileDataProcessRecordDto dto);

    /**
     * 获取数据处理状态
     *
     * @param recordId
     * @return
     */
    FileDataProcessRecordDto getFileData(String recordId);

    /**
     * 检索上传处理记录
     *
     * @param pageable
     * @param searchMap 必须包含creator 上传人 bis 上传业务名
     * @return
     */
    Page<FileDataProcessRecordDto> page(Pageable pageable, Map<String, Object> searchMap);
}
