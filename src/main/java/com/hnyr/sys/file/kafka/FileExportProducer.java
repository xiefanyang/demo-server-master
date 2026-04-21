package com.hnyr.sys.file.kafka;

import cn.hutool.json.JSONUtil;
import com.hnyr.sys.file.entity.dto.FileExportRecordDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FileExportProducer {
    @Value("${file.export.kafka.topic:'t_export_data'}")
    private String collectRecordTopic;
    private KafkaTemplate<String, String> kafkaTemplate;

    public FileExportProducer(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    /**
     * 操作记录
     *
     * @param recordMessage 记录的内容
     */
    @Async
    public void sendMessage(FileExportRecordDto recordMessage) {
        try {
            kafkaTemplate.send(collectRecordTopic, JSONUtil.toJsonStr(recordMessage));
        } catch (Exception e) {
            log.error("加入数据采集失败:" + e.getMessage());
        }
    }
}
