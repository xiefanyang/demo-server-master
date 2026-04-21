package com.hnyr.sys.file.entity.dto;

import com.hnyr.sys.data.entity.BaseBisDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
public class FileDataProcessRecordDto extends BaseBisDto {
    private Long finishedTime;
    private Integer state = 0;
    private Integer countNum = 0;
    private Integer successNum = 0;
    private Integer skipNum = 0;
    private Integer failNum = 0;
    private String resultContent;
    private Long creator;
    private String bis;
    private String fileId;
    private String serviceName;
    private String methodName;
    private Map<String,Object> params;

    private Integer emptyNum = 0;
    private List<String> errors;

    private List<Object> headers;
}
