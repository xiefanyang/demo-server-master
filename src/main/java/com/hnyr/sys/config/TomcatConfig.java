package com.hnyr.sys.config;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: TomcatConfig
 * @Description: tomcat 配置，禁用 tomcat 内置 session 管理（前后端分离不需要，如使用 druid 连接池的 admin 监控，需要保留 session，需要移除本配置）
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
@Configuration
public class TomcatConfig {
    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> cookieProcessorCustomizer() {
        // 自定义session管理器,关闭session
        return factory -> factory.addContextCustomizers(context -> context.setManager(new NoSessionManager()));
    }
}
