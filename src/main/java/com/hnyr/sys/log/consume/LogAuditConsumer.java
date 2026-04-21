package com.hnyr.sys.log.consume;

import cn.hutool.json.JSONUtil;
import com.hnyr.sys.log.mongo.LogAuditDoc;
import com.hnyr.sys.log.service.LogAuditDocService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

import javax.annotation.Resource;

/**
 * @ClassName: LogAuditConsumer
 * @Description: log 消息消费
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
@Configuration
@Slf4j
public class LogAuditConsumer {

    @Resource
    LogAuditDocService logAuditDocService;

    @KafkaListener(topics = "${audit.log.kafka.topic}", groupId = "${audit.log.kafka.group}")
    public void handle(String context) {
        try {
//            log.info("{}", context);
            LogAuditDoc recordMessage = JSONUtil.toBean(context, LogAuditDoc.class);
            logAuditDocService.saveRecord(recordMessage);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("api-record error:{}");
        }
    }
}
