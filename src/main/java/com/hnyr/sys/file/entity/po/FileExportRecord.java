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
@Table(name = "t_sys_file_export_record")
public class FileExportRecord extends BaseBisEntity {
    @Column(columnDefinition = "bigint COMMENT '处理结束时间'")
    private Long finishedTime;
    @Column(columnDefinition = "tinyint COMMENT '处理结果 0 待处理 1 处理中 2处理结束' default 0 ", nullable = false)
    private Integer state;
    @Column(columnDefinition = "varchar(100) COMMENT '要导出的文件名称'", nullable = false)
    private String fileName;
    @Column(columnDefinition = "tinyint COMMENT '是否处理成功' ")
    private Integer success;
    @Column(columnDefinition = "text COMMENT '失败结果' ")
    private String errorMessage;
    @Column(columnDefinition = "bigint COMMENT '导出人'", nullable = false)
    private Long creator;
    @Column(columnDefinition = "varchar(50) COMMENT '导出业务，如：导出教师基本信息'", nullable = false)
    private String bis;
    @Column(columnDefinition = "varchar(64) COMMENT '文件id' ")
    private String fileId;
}
