package com.hnyr.sys.data.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * @ClassName: BaseBisEntity
 * @Description: entity 基类 （ 用户业务记录类数据对象 用recordId作为唯一字段及关联字段，不是用 id，规避规律爆破和大数据量 id 自增耗尽导致关联问题）
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
@MappedSuperclass
public abstract class BaseBisEntity extends BaseEntity {
    @Column(columnDefinition = "varchar(64)", nullable = false, unique = true, updatable = false)
    private String recordId;

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }
}
