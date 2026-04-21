package com.hnyr.sys.file.service.impl;

import com.hnyr.sys.file.service.FileExportContentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service("fileExportContentService")
@Transactional(rollbackFor = Exception.class)
public class FileExportContentServiceImpl implements FileExportContentService {
    @Override
    public byte[] writeFile(Long userId, Map<String, Object> paramBody) {
        return new byte[0];
    }
}
