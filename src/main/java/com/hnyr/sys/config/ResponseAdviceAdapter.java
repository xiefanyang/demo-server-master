package com.hnyr.sys.config;

import cn.hutool.json.JSONUtil;
import com.hnyr.sys.rbac.service.ApiEncryptService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName: ResponseAdviceAdapter
 * @Description: 响应数据包加密
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
@RestControllerAdvice(basePackages = "com.hnyr")
@Slf4j
public class ResponseAdviceAdapter implements ResponseBodyAdvice<Object> {
    @Resource
    private ApiEncryptService apiEncryptService;
    private static final String ERROR_ATTRIBUTE = DefaultErrorAttributes.class.getName()
            + ".ERROR";

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    private void encryptBody(ResponseResult body, ServerHttpRequest request) {
        try {
            String path = request.getURI().getPath();
            if (StringUtils.isNotBlank(path) && apiEncryptService.checkEncryptResponseApi(path)) {
                if (body.getData() != null) {
                    //服务端加密 暂时不启用
//                    body.setData(SmUtils.sm2EncResponse(body.getData()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest httpRequest, ServerHttpResponse httpResponse) {
        ResponseResult rs;
        if (body instanceof ResponseResult) {
            rs = (ResponseResult) body;
        } else {
            HttpServletRequest request = ((ServletServerHttpRequest) httpRequest).getServletRequest();
            HttpServletResponse response = ((ServletServerHttpResponse) httpResponse).getServletResponse();
            if (response.getStatus() >= HttpStatus.BAD_REQUEST.value()) {
                if (response.getStatus() == HttpStatus.NOT_FOUND.value()) {
                    return ResponseResult.error(ErrorEnum.NOT_FOUND.getCode(), ErrorEnum.NOT_FOUND.getMessage());
                }
                log.error("request_uri: {}", getPath(request));
                //转换异常
                Throwable throwable = getError(request);
                String errMsg = throwable.getMessage();
                int errCode = ErrorEnum.RUNTIME_EXCEPTION.getCode();
                if (throwable == null) {
                    return ResponseResult.error(errCode, errMsg);
                }
                if (throwable instanceof HttpMessageNotWritableException) {
                    errMsg = ((HttpMessageNotWritableException) throwable).getRootCause().getMessage();
                } else if (throwable instanceof HttpMessageNotReadableException) {
                    errMsg = "请求中包含错误格式的数据,请检查";
                } else {
                    if (throwable instanceof BusinessException) {
                        if (throwable.getMessage() != null) {
                            errMsg = throwable.getMessage();
                        }
                        errCode = ((BusinessException) throwable).getCode();
                    }
                }
                response.setStatus(HttpStatus.OK.value());
                rs = ResponseResult.error(errCode, errMsg);
            } else {
                rs = ResponseResult.success(body);
            }
        }
        //处理返回值是String的情况
        if (body instanceof String) {
            return JSONUtil.toJsonStr(rs);
        }
        encryptBody(rs, httpRequest);
        return rs;
    }


    private String getPath(HttpServletRequest request) {
        return (String) request.getAttribute("javax.servlet.error.request_uri");
    }

    public Throwable getError(HttpServletRequest request) {
        Throwable exception = (Throwable) request.getAttribute(ERROR_ATTRIBUTE);
        if (exception == null) {
            exception = (Throwable) request.getAttribute("javax.servlet.error.exception");
        }
        return exception;
    }


}
