package com.hnyr.sys.file.dao;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import com.hnyr.sys.data.Conditions;
import com.hnyr.sys.file.dao.repository.FileRepository;
import com.hnyr.sys.file.entity.dto.FileDto;
import com.hnyr.sys.file.entity.po.File;
import com.hnyr.sys.file.util.FileUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName: FileDao
 * @Description:
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
@Component
public class FileDao {

    @Resource
    private FileRepository fileRepository;

    public File add(FileDto fileDto) {
        File po = new File();
        BeanUtil.copyProperties(fileDto, po);
        po.setSize(fileDto.getSize());
        po.setEnable(true);
        String suffix = po.getOriginalName().substring(po.getOriginalName().lastIndexOf("."));
        po.setSuffix(suffix);
        po.setTypeName(FileUtil.getFileTypeName(suffix));
        if (!StringUtils.hasText(fileDto.getType())) {
            po.setType(FileUtil.getMimeType(suffix));
        } else {
            po.setType(fileDto.getType());
        }
        return fileRepository.save(po);
    }

    public void update(File po) {
        fileRepository.save(po);
    }

    public File getFile(String recordId) {
        return fileRepository.findOne(Conditions.empty().and("recordId").is(recordId)
                .and("deleted").is(false));
    }

    public List<File> findFileList(Set<String> recordIds) {
        return fileRepository.findAll(Conditions.empty().and("recordId").in(recordIds)
                .and("deleted").is(false));
    }

    public Page<File> page(Pageable pageable, Map<String, Object> searchMap) {
        Conditions conditions = Conditions.empty().and("deleted").is(false);
        if (MapUtil.getBool(searchMap, "enable") != null) {
            conditions.and("enable").is(MapUtil.getBool(searchMap, "enable"));
        }
        if (StringUtils.hasText(MapUtil.getStr(searchMap, "categoryId"))) {
            conditions.and("categoryId").is(MapUtil.getStr(searchMap, "categoryId"));
        }
        if (StringUtils.hasText(MapUtil.getStr(searchMap, "recordId"))) {
            conditions.and("recordId").is(MapUtil.getStr(searchMap, "recordId"));
        }
        if (StringUtils.hasText(MapUtil.getStr(searchMap, "typeName"))) {
            conditions.and("typeName").is(MapUtil.getStr(searchMap, "typeName"));
        }

        return fileRepository.findAll(conditions, pageable);
    }
}
