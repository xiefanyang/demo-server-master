package com.hnyr.sys.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: InterceptorConfig
 * @Description: 拦截配置
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Value("${cors.origin:*}")
    String corsOrigin;
    @Value("${cors.header:*}")
    String corsHeader;
    @Value("${cors.method:*}")
    String corsMethod;
    @Value("${cors.credentials:true}")
    Boolean corsCredentials;
    @Value("${cors.max-age:3600}")
    Long corsMaxAge;

    @Value("${hnyr.exclude.xql:}")
    String customExcludeXql;

    private static final String excludesXql = "/favicon.ico,/static/,/swagger,/actuator,/sys/file/upload,/img/,/js/,/css/,/dv/,/ds/,";
    @Value("${hnyr.exclude.path:}")
    String customExcludePath;
    private static final String FAVICON_URL = "/favicon.ico";
    private static final String HOMEPAGE_URL = "/index.html";

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/").addResourceLocations("/**");
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }

    private static final String[] excludes = {FAVICON_URL, HOMEPAGE_URL, "/api/sys/captcha/get-captcha", "/api/sys/login", "/api/sys/login/**", "/api/sys/logout/**", "/api/wap/login/**", "/api/wap/logout", "/api/static/**", "/404", "/error", "/assets/**", "/static/**", "/open/**", "/api/open/**", "/swagger-**", "/swagger-ui/**", "/swagger-resources/**", "/webjars/**", "/v3/api-docs", "/v3/api-docs/**"};

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> excludeList = Lists.newArrayList(excludes);
        if (StringUtils.hasText(customExcludePath)) {
            excludeList.addAll(Lists.newArrayList(customExcludePath.split(",")));
        }
        //设置（模糊）匹配的url
        registry.addInterceptor(authTokenInterceptor())
                .addPathPatterns("/api/**")
                .excludePathPatterns(excludeList);
    }


    /**
     * 将拦截器作为bean写入配置中
     *
     * @return
     */
    @Bean
    public AuthInterceptor authTokenInterceptor() {
        return new AuthInterceptor();
    }

    @Bean
    public FilterRegistrationBean xssFilterRegistrationBean() {
        String xql = excludesXql;
        if (StringUtils.hasText(customExcludeXql)) {
            xql = excludesXql + customExcludeXql;
        }
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new XssAndSqlFilter());
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.setEnabled(true);
        filterRegistrationBean.addUrlPatterns("/*");
        Map<String, String> initParameters = Maps.newHashMap();
        initParameters.put("excludes", xql);
        initParameters.put("isIncludeRichText", "true");
        filterRegistrationBean.setInitParameters(initParameters);
        return filterRegistrationBean;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders(corsHeader)
                .allowedMethods(corsMethod)
                .maxAge(corsMaxAge)
                .allowCredentials(corsCredentials)
                .allowedOriginPatterns(corsOrigin);
    }

}
