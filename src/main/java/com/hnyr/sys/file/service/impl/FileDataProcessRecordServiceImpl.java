package com.hnyr.sys.file.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.hnyr.sys.file.dao.FileDataProcessRecordDao;
import com.hnyr.sys.file.entity.dto.FileDataProcessRecordDto;
import com.hnyr.sys.file.entity.po.FileDataProcessRecord;
import com.hnyr.sys.file.kafka.FileDataProcessProducer;
import com.hnyr.sys.file.service.FileDataProcessRecordService;
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
public class FileDataProcessRecordServiceImpl implements FileDataProcessRecordService {
    @Resource
    FileDataProcessProducer fileDataProcessProducer;
    @Resource
    private FileDataProcessRecordDao fileDataProcessRecordDao;

    @Override
    public FileDataProcessRecordDto addFileData(FileDataProcessRecordDto dto) {
        AssertUtil.isTrue(dto.getCreator() != null, "缺少上传人");
        AssertUtil.isTrue(dto.getBis() != null, "缺少上传业务名称");
        AssertUtil.isTrue(dto.getState() != null, "缺少处理状态");
        FileDataProcessRecord entity = new FileDataProcessRecord();
        BeanUtil.copyProperties(dto, entity, "id", "version", "createTime");
        entity.setRecordId(IdUtil.objectId());
        fileDataProcessRecordDao.save(entity);
        BeanUtil.copyProperties(entity, dto);
        return dto;
    }

    @Override
    public FileDataProcessRecordDto addToQueue(FileDataProcessRecordDto dto) {
        dto = addFileData(dto);
        fileDataProcessProducer.sendMessage(dto);
        return dto;
    }

    @Override
    public FileDataProcessRecordDto updateFileData(FileDataProcessRecordDto dto) {
        AssertUtil.isTrue(dto.getRecordId() != null, "缺少记录ID");
        AssertUtil.isTrue(dto.getState() != null, "缺少处理状态");
        FileDataProcessRecord entity = fileDataProcessRecordDao.get(dto.getRecordId());
        entity.setCountNum(dto.getCountNum());
        entity.setFailNum(dto.getFailNum());
        entity.setSuccessNum(dto.getSuccessNum());
        entity.setSkipNum(dto.getSkipNum());
        entity.setState(dto.getState());
        if (dto.getFinishedTime() == null &&
                (dto.getState().intValue() == 2 || dto.getState().intValue() == 3)) {
            entity.setFinishedTime(System.currentTimeMillis());
        } else if (dto.getFinishedTime() != null){
            entity.setFinishedTime(System.currentTimeMillis());
        }
        entity.setResultContent(dto.getResultContent());
        fileDataProcessRecordDao.save(entity);
        return dto;
    }

    @Override
    public FileDataProcessRecordDto getFileData(String recordId) {
        FileDataProcessRecord entity = fileDataProcessRecordDao.get(recordId);
        return entity == null ? null : BeanUtil.copyProperties(entity, FileDataProcessRecordDto.class);
    }

    @Override
    public Page<FileDataProcessRecordDto> page(Pageable pageable, Map<String, Object> searchMap) {
        return DataConvertor.pageConvert(fileDataProcessRecordDao.page(pageable, searchMap), pageable, FileDataProcessRecordDto.class);
    }
}
