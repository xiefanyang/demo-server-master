package com.hnyr.sys.log;

import cn.hutool.json.JSONUtil;
import com.hnyr.sys.log.mongo.LogAuditDoc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;

/**
 * @ClassName: AuditContext
 * @Description: 操作日志消息发送
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
@Slf4j
public class AuditContext {

    @Value("${audit.log.kafka.topic:'audit_topic_dm_learn'}")
    private String auditTopic;
    private KafkaTemplate<String, String> kafkaTemplate;

    public AuditContext(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    /**
     * 操作记录
     *
     * @param recordMessage 记录的内容
     */
    @Async
    public void record(LogAuditDoc recordMessage) {
        try {
            kafkaTemplate.send(auditTopic, JSONUtil.toJsonStr(recordMessage));
        } catch (Exception e) {
            log.error("记录日志失败:" + e.getMessage());
        }
    }
}
