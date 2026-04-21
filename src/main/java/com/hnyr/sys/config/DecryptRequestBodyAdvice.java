package com.hnyr.sys.config;

import com.hnyr.sys.rbac.service.ApiEncryptService;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import javax.annotation.Resource;
import java.lang.reflect.Type;

/**
 * @ClassName: DecryptRequestBodyAdvice
 * @Description: 请求包解密处理
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
@RestControllerAdvice
public class DecryptRequestBodyAdvice implements RequestBodyAdvice {
    @Resource
    private ApiEncryptService apiEncryptService;

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 检查 headers 中是否指定了 _sm（前台请求的时候在 header 中设置），进行请求包的直接解密
        String headerSm = "_sm";
        if (!CollectionUtils.isEmpty(inputMessage.getHeaders().get(headerSm))) {
            //请求信息解密，参考DecryptHttpInputMessage解密类
            return new DecryptHttpInputMessage(inputMessage);
        } else {
            return inputMessage;
        }
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }
}
