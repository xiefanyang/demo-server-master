package com.hnyr.sys.file.dao;

import cn.hutool.core.map.MapUtil;
import com.hnyr.sys.data.Conditions;
import com.hnyr.sys.file.dao.repository.FileDataProcessRecordRepository;
import com.hnyr.sys.file.entity.po.FileDataProcessRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @ClassName: FileDataProcessRecordDao
 * @Description:
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
@Component
public class FileDataProcessRecordDao {

    @Resource
    private FileDataProcessRecordRepository fileDataProcessRecordRepository;

    public FileDataProcessRecord save(FileDataProcessRecord record) {
        return fileDataProcessRecordRepository.save(record);
    }

    public FileDataProcessRecord get(String id) {
        return fileDataProcessRecordRepository.findOne(Conditions.empty().and("recordId").is(id));
    }

    public Page<FileDataProcessRecord> page(Pageable pageable, Map<String, Object> searchMap) {
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Order.desc("id")));
        Conditions conditions = Conditions.empty()
                .and("creator").is(MapUtil.getLong(searchMap, "creator"))
                .and("bis").is(MapUtil.getStr(searchMap, "bis"))
                .and("deleted").is(false);
        Integer state = MapUtil.getInt(searchMap, "state");
        if (state != null) {
            conditions.and("state").is(state);
        }
        return fileDataProcessRecordRepository.findAll(conditions, pageable);

    }

}
