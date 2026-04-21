package com.hnyr.sys.file.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.hnyr.sys.file.dao.FileExportRecordDao;
import com.hnyr.sys.file.entity.dto.FileExportRecordDto;
import com.hnyr.sys.file.entity.po.FileExportRecord;
import com.hnyr.sys.file.kafka.FileExportProducer;
import com.hnyr.sys.file.service.FileExportRecordService;
import com.hnyr.sys.utils.AssertUtil;
import com.hnyr.sys.utils.DataConvertor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
public class FileExportRecordServiceImpl implements FileExportRecordService {
    @Resource
    FileExportProducer fileExportProducer;
    @Resource
    private FileExportRecordDao fileExportRecordDao;

    private FileExportRecordDto addFileData(FileExportRecordDto dto) {
        AssertUtil.isTrue(dto.getCreator() != null, "缺少上传人");
        AssertUtil.isTrue(dto.getBis() != null, "缺少上传业务名称");
        AssertUtil.isTrue(dto.getState() != null, "缺少处理状态");
        FileExportRecord entity = new FileExportRecord();
        BeanUtil.copyProperties(dto, entity, "id", "version", "createTime");
        entity.setRecordId(IdUtil.objectId());
        fileExportRecordDao.save(entity);
        BeanUtil.copyProperties(entity, dto);
        return dto;
    }

    @Override
    public FileExportRecordDto addToQueue(FileExportRecordDto dto) {
        dto = addFileData(dto);
        fileExportProducer.sendMessage(dto);
        return dto;
    }

    @Override
    public FileExportRecordDto updateFileData(FileExportRecordDto dto) {
        AssertUtil.isTrue(dto.getRecordId() != null, "缺少记录ID");
        AssertUtil.isTrue(dto.getState() != null, "缺少处理状态");
        FileExportRecord entity = fileExportRecordDao.get(dto.getRecordId());
        entity.setSuccess(dto.getSuccess());
        entity.setState(dto.getState());
        if (dto.getFinishedTime() == null &&
                (dto.getState().intValue() == 2 || dto.getState().intValue() == 3)) {
            entity.setFinishedTime(System.currentTimeMillis());
        } else if (dto.getFinishedTime() != null) {
            entity.setFinishedTime(System.currentTimeMillis());
        }
        entity.setErrorMessage(dto.getErrorMessage());
        fileExportRecordDao.save(entity);
        return dto;
    }

    @Override
    public FileExportRecordDto getFileData(String recordId) {
        FileExportRecord entity = fileExportRecordDao.get(recordId);
        return entity == null ? null : BeanUtil.copyProperties(entity, FileExportRecordDto.class);
    }

    @Override
    public Page<FileExportRecordDto> page(Pageable pageable, Map<String, Object> searchMap) {
        return DataConvertor.pageConvert(fileExportRecordDao.page(pageable, searchMap), pageable, FileExportRecordDto.class);
    }
}
