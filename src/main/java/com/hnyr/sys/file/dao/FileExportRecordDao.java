package com.hnyr.sys.file.dao;

import cn.hutool.core.map.MapUtil;
import com.hnyr.sys.data.Conditions;
import com.hnyr.sys.file.dao.repository.FileExportRecordRepository;
import com.hnyr.sys.file.entity.po.FileExportRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @ClassName: FileExportRecordDao
 * @Description:
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
@Component
public class FileExportRecordDao {

    @Resource
    private FileExportRecordRepository fileExportRecordRepository;

    public FileExportRecord save(FileExportRecord record) {
        return fileExportRecordRepository.save(record);
    }

    public FileExportRecord get(String id) {
        return fileExportRecordRepository.findOne(Conditions.empty().and("recordId").is(id));
    }

    public Page<FileExportRecord> page(Pageable pageable, Map<String, Object> searchMap) {
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Order.desc("id")));
        Conditions conditions = Conditions.empty()
                .and("creator").is(MapUtil.getLong(searchMap, "creator"))
                .and("bis").is(MapUtil.getStr(searchMap, "bis"))
                .and("deleted").is(false);
        Integer state = MapUtil.getInt(searchMap, "state");
        if (state != null) {
            conditions.and("state").is(state);
        }
        return fileExportRecordRepository.findAll(conditions, pageable);

    }

}
