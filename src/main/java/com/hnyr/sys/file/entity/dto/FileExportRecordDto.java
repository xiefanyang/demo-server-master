package com.hnyr.sys.file.entity.dto;

import com.hnyr.sys.data.entity.BaseBisDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(chain = true)
public class FileExportRecordDto extends BaseBisDto {
    private Long finishedTime;
    private Integer state;
    private String fileName;
    private Integer success;
    private String errorMessage;
    private Long creator;
    private String bis;
    private String fileId;
    private String serviceName;
    private String methodName;
    private Map<String, Object> params;
}