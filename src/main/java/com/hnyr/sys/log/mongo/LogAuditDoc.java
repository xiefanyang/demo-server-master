package com.hnyr.sys.log.mongo;

import lombok.Data;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import java.util.List;

/**
 * @ClassName: LogAuditDoc
 * @Description: 操作日志
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
@Data
public class LogAuditDoc {

    public static final String COLLECTION_NAME = "LOG_AUDIT";
    public static final String COLLECTION_NAME_IMPORT = "LOG_AUDIT_IMPORT";

    @Id
    private String id;
    /**
     * 应用名称
     */
    private String application;
    /**
     * 类型
     */
    private String type;
    /**
     * aop的类的信息
     */
    private String signature;
    /**
     * 操作名称
     */
    private String name;
    /**
     * 开始时间
     */
    private Long begin;
    /**
     * 来源ip
     */
    private String ip;
    /**
     * 请求地址
     */
    private String url;
    /**
     * 用户id
     */
    private Long userId;
    private String userRealName;
    /**
     * 用户角色code,可能有多个，用逗号隔开
     */
    private String roleCode;
    private String roleName;
    /**
     * 成功状态
     */
    private boolean success;
    /**
     * 异常信息
     */
    private String exception;
    /**
     * 结束时间
     */
    private Long end;
    /**
     * 耗时
     */
    private long time;
    /**
     * user-agent
     */
    private String userAgent;
    /**
     * 标识是否迁移过来的数据
     */
    private Integer migrationsData;
    /**
     * 参数
     */
    private List<Object> params;

    @Transient
    private String beginTime;
    @Transient
    private String endTime;

    public String getBeginTime() {
        if (begin != null) {
            return new DateTime(begin).toString("yyyy-MM-dd HH:mm:ss");
        }
        return "";
    }

    public String getEndTime() {
        if (end != null) {
            return new DateTime(end).toString("yyyy-MM-dd HH:mm:ss");
        }
        return "";
    }
}
