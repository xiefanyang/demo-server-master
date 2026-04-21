package com.hnyr.sys.file.kafka;

import cn.hutool.json.JSONUtil;
import com.hnyr.sys.config.BusinessException;
import com.hnyr.sys.config.SpringContextHolder;
import com.hnyr.sys.file.entity.dto.FileDataProcessRecordDto;
import com.hnyr.sys.file.service.FileDataProcessRecordService;
import com.hnyr.sys.utils.AssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

import javax.annotation.Resource;
import java.lang.reflect.Method;

@Configuration
@Slf4j
public class FileDataProcessConsumer {

    @Resource
    private FileDataProcessRecordService fileDataProcessRecordService;

    @KafkaListener(topics = "${file.data.process.kafka.topic:t_import_data_process}", groupId = "${file.data.process.kafka.group:g_import_data_process}")
    public void handle(String context) {
        FileDataProcessRecordDto record = null;
        try {
//            log.info("{}", context);
            record = JSONUtil.toBean(context, FileDataProcessRecordDto.class);
            AssertUtil.isTrue(record != null, "缺少data数据");
            String serviceName = record.getServiceName();
            String methodName = record.getMethodName();
            Object service = SpringContextHolder.getBean(serviceName);
            AssertUtil.isTrue(service != null, "未找到数据处理器");
            Method method = service.getClass().getDeclaredMethod(methodName, FileDataProcessRecordDto.class);
            AssertUtil.isTrue(method != null, "未找到数据处理器");
            method.invoke(service, record);
        } catch (Exception e) {
            log.error("api-record error:{}", e.getMessage());
            if (record != null) {
                record.setState(3).setResultContent("解析数据异常! " + (e instanceof BusinessException ? e.getMessage() : ""));
                fileDataProcessRecordService.updateFileData(record);
            }
        }
    }
}
