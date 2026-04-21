package com.hnyr.sys.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.Servlet;
import java.util.List;

/**
 * @ClassName: BasicErrorConfig
 * @Description: 基础错误处理配置
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass({Servlet.class, DispatcherServlet.class})
@AutoConfigureBefore(WebMvcAutoConfiguration.class)
@EnableConfigurationProperties(WebProperties.class)
public class BasicErrorConfig {
    @Autowired(required = false)
    private List<ErrorViewResolver> errorViewResolvers;
    private final ServerProperties serverProperties;

    public BasicErrorConfig(ServerProperties serverProperties) {
        this.serverProperties = serverProperties;
    }

    @Bean
    public BasicErrorController basicErrorController(ErrorAttributes errorAttributes) {
        return new BasicErrorController(errorAttributes, this.serverProperties.getError(),
                this.errorViewResolvers);
    }
}
