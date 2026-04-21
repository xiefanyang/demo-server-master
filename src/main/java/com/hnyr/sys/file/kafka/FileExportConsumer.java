package com.hnyr.sys.file.kafka;

import cn.hutool.json.JSONUtil;
import com.hnyr.sys.config.BusinessException;
import com.hnyr.sys.config.SpringContextHolder;
import com.hnyr.sys.file.entity.dto.FileExportRecordDto;
import com.hnyr.sys.file.service.FileExportRecordService;
import com.hnyr.sys.utils.AssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

import javax.annotation.Resource;
import java.lang.reflect.Method;

@Configuration
@Slf4j
public class FileExportConsumer {

    @Resource
    private FileExportRecordService fileExportRecordService;

    @KafkaListener(topics = "${file.export.kafka.topic:t_export_data}", groupId = "${file.export.kafka.group:g_export_data}")
    public void handle(String context) {
        FileExportRecordDto record = null;
        try {
//            log.info("{}", context);
            record = JSONUtil.toBean(context, FileExportRecordDto.class);
            AssertUtil.isTrue(record != null, "缺少data数据");
            String serviceName = record.getServiceName();
            String methodName = record.getMethodName();
            Object service = SpringContextHolder.getBean(serviceName);
            AssertUtil.isTrue(service != null, "未找到数据处理器");
            Method method = service.getClass().getDeclaredMethod(methodName, FileExportRecordDto.class);
            AssertUtil.isTrue(method != null, "未找到数据处理器");
            method.invoke(service, record);
        } catch (Exception e) {
            log.error("api-record error:{}", e.getMessage());
            if (record != null) {
                record.setState(2).setErrorMessage("解析数据异常! " + (e instanceof BusinessException ? e.getMessage() : ""));
                fileExportRecordService.updateFileData(record);
            }
        }
    }
}
