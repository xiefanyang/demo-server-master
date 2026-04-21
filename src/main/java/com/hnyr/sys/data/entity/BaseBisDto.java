package com.hnyr.sys.data.entity;

import javax.persistence.MappedSuperclass;

/**
 * @ClassName: BaseBisDto
 * @Description: dto 基类 （对应 BaseBisEntity）
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
@MappedSuperclass
public class BaseBisDto extends BaseDto {
    private String recordId;

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }
}
