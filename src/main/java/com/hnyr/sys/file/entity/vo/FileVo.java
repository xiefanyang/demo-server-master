package com.hnyr.sys.file.entity.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class FileVo implements Serializable {
    private String recordId;
    private String category = "";
    private String type;
    private String suffix;
    private Long size;
    private Long fileTime;
    private String originalName;
    private String typeName;
    private String sizeStr;
    private String fileUrl;

    private String name;
    private String url;

    public String getName() {
        return originalName;
    }

    public String getUrl() {
        return fileUrl;
    }

}
