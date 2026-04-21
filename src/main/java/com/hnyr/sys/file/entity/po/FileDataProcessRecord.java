package com.hnyr.sys.file.entity.po;

import com.hnyr.sys.data.entity.BaseBisEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "t_sys_file_data_process_record")
public class FileDataProcessRecord extends BaseBisEntity {
    @Column(columnDefinition = "bigint COMMENT '处理结束时间'")
    private Long finishedTime;
    @Column(columnDefinition = "tinyint COMMENT '处理结果 0 待处理 1 处理中 2处理结束 3 处理失败' default 0 ", nullable = false)
    private Integer state;
    @Column(columnDefinition = "int COMMENT '总数据'")
    private Integer countNum;
    @Column(columnDefinition = "int COMMENT '成功数据'")
    private Integer successNum;
    @Column(columnDefinition = "int COMMENT '跳过数据'")
    private Integer skipNum;
    @Column(columnDefinition = "int COMMENT '失败数据'")
    private Integer failNum;
    @Column(columnDefinition = "longtext COMMENT '处理结果' ")
    private String resultContent;
    @Column(columnDefinition = "bigint COMMENT '上传人'", nullable = false)
    private Long creator;
    @Column(columnDefinition = "varchar(50) COMMENT '上传业务名称，如：导入课程信息'", nullable = false)
    private String bis;
    @Column(columnDefinition = "varchar(64) COMMENT '文件id' ", nullable = false)
    private String fileId;
}