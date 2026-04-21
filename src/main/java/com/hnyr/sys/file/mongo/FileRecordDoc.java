package com.hnyr.sys.file.mongo;

import lombok.Data;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Transient;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Map;

@Data
public class FileRecordDoc implements Serializable {
    @Id
    private String id;
    private String name;
    private Long userId;
    private String fileType;
    private Map<String, Object> params;
    private String fileUrl;
    private Long createTime;
    private Long endTime;
    private Boolean publicRead;
    /**
     * 0处理中， 1处理成功，2处理失败
     */
    private Integer state;

    private String errorMsg;

    @Transient
    private String createTimeStr;

    public String getCreateTimeStr() {
        if (createTime != null && createTime > 0) {
            return new DateTime(createTime).toString("yyyy-MM-dd HH:mm:ss");
        }
        return createTimeStr;
    }
}
