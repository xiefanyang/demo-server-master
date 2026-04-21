package com.hnyr.sys.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @ClassName: AuditConfig
 * @Description: 操作日志配置类
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableAsync
@AutoConfigureAfter(KafkaAutoConfiguration.class)
public class AuditConfig {
    @Autowired
    @Value("${spring.application.name:'UNKNOWN_APP'}")
    private String application;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;


    @Bean
    public AuditContext auditContext() {
        return new AuditContext(kafkaTemplate);
    }


    @Bean
    public ControllerMethodAspect auditWebMethodAspect() {
        return new ControllerMethodAspect(auditContext(), application);
    }

}
