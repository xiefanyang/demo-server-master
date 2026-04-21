package com.hnyr.sys.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.plugins.WebFluxRequestHandlerProvider;
import springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: Swagger3Config
 * @Description: swagger配置
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
@EnableOpenApi
@Configuration
public class Swagger3Config {
    /**
     * 是否开启swagger，生产环境一般关闭，所以这里成变量
     */
    @Value("${swagger.enable}")
    private Boolean enable;
    /**
     * 项目应用名
     */
    @Value("${swagger.application-name}")
    private String applicationName;
    /**
     * 项目版本信息
     */
    @Value("${swagger.application-version}")
    private String applicationVersion;
    /**
     * 项目描述信息
     */
    @Value("${swagger.application-description}")
    private String applicationDescription;

    @Value("${swagger.application-contact-name:}")
    String applicationContactName;
    @Value("${swagger.application-contact-url:}")
    String applicationContactUrl;
    @Value("${swagger.application-contact-email:}")
    String applicationContactEmail;

    @Bean
    public Docket docketSys() {
        return create("SYSTEM", "/api/sys/**");
    }

    @Bean
    public Docket docketDemo() {
        return create(applicationName.toUpperCase(), "/api/" + applicationName + "/**");
    }

    private Docket create(String groupName, String path) {
        return new Docket(DocumentationType.OAS_30)
                .securityContexts(Arrays.asList(SecurityContext.builder()
                        .securityReferences(Arrays.asList(SecurityReference.builder().reference("Authorization").scopes(new AuthorizationScope[]{new AuthorizationScope("global", "accessEverything")}).build())).build())).securitySchemes(Arrays.asList(new ApiKey("Authorization", "Authorization", "header"))).apiInfo(apiInfo()).enable(enable).select()
                //apis： 添加swagger接口提取范围
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
//                .paths(PathSelectors.any())
                .paths(PathSelectors.ant(path)).build().groupName(groupName);
    }


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title(applicationName)
                .description(applicationDescription)
                .contact(new Contact(applicationContactName, applicationContactUrl, applicationContactEmail))
                .version(applicationVersion).build();
    }

    /**
     * 解决swagger在springboot2.7以后的空指针异常
     */

    @Bean
    public static BeanPostProcessor springfoxHandlerProviderBeanPostProcessor() {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof WebMvcRequestHandlerProvider || bean instanceof WebFluxRequestHandlerProvider) {
                    customizeSpringfoxHandlerMappings(getHandlerMappings(bean));
                }
                return bean;
            }

            private <T extends RequestMappingInfoHandlerMapping> void customizeSpringfoxHandlerMappings(List<T> mappings) {
                List<T> copy = mappings.stream()
                        .filter(mapping -> mapping.getPatternParser() == null)
                        .collect(Collectors.toList());
                mappings.clear();
                mappings.addAll(copy);
            }

            @SuppressWarnings("unchecked")
            private List<RequestMappingInfoHandlerMapping> getHandlerMappings(Object bean) {
                try {
                    Field field = ReflectionUtils.findField(bean.getClass(), "handlerMappings");
                    field.setAccessible(true);
                    return (List<RequestMappingInfoHandlerMapping>) field.get(bean);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new IllegalStateException(e);
                }
            }

        };

    }
}
