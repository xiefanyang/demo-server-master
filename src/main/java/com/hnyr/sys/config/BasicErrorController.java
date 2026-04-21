package com.hnyr.sys.config;

import cn.hutool.core.bean.BeanUtil;
import com.hnyr.sys.utils.AssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: AbstractErrorController
 * @Description: 错误与异常处理包装重写
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
@Slf4j
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class BasicErrorController extends AbstractErrorController {
    private final ErrorProperties errorProperties;

    public BasicErrorController(ErrorAttributes errorAttributes, ErrorProperties errorProperties) {
        this(errorAttributes, errorProperties, Collections.<ErrorViewResolver>emptyList());
    }

    public BasicErrorController(ErrorAttributes errorAttributes, ErrorProperties errorProperties, List<ErrorViewResolver> errorViewResolvers) {
        super(errorAttributes, errorViewResolvers);
        AssertUtil.isTrue(errorAttributes != null, "ErrorProperties must not be null");
        this.errorProperties = errorProperties;
    }

    @RequestMapping(produces = "text/html")
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        HttpStatus status = getStatus(request);
        //转换异常
        Map<String, Object> model = BeanUtil.beanToMap(getError(request));
        response.setStatus(status.value());
        model.put("status", status.value() + " " + status.getReasonPhrase());
        ModelAndView modelAndView = resolveErrorView(request, response, status, model);
        if (status.value() == HttpStatus.NOT_FOUND.value()) {
            return modelAndView == null ? new ModelAndView("404", model) : modelAndView;
        }
        return modelAndView == null ? new ModelAndView("error", model) : modelAndView;
    }

    @RequestMapping
    @ResponseBody
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        // 获取异常参数
        ErrorAttributeOptions options = ErrorAttributeOptions.of(
                ErrorAttributeOptions.Include.MESSAGE,
                ErrorAttributeOptions.Include.EXCEPTION,
                ErrorAttributeOptions.Include.STACK_TRACE,
                ErrorAttributeOptions.Include.BINDING_ERRORS);
        Map<String, Object> body = this.getErrorAttributes(request, options);
        HttpStatus status = getStatus(request);
        //转换异常
        String clientType = request.getHeader(Constants.CLIENT_TYPE);
        if (clientType != null && Constants.FEIGN_REQUEST_TYPE.equals(clientType)) {
            return new ResponseEntity<>(body, status);
        } else {
            return new ResponseEntity(getError(request), HttpStatus.OK);
        }
    }

    public static final String ERROR_ATTRIBUTE = DefaultErrorAttributes.class.getName() + ".ERROR";

    private ResponseResult getError(HttpServletRequest request) {
        HttpStatus status = getStatus(request);
        if (status.value() == HttpStatus.NOT_FOUND.value()) {
            return ResponseResult.error(ErrorEnum.NOT_FOUND.getCode(), ErrorEnum.NOT_FOUND.getMessage());
        }
        Throwable throwable = (Throwable) request.getAttribute(ERROR_ATTRIBUTE);
        if (throwable == null) {
            throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
        }
        //转换异常
        String errMsg = throwable == null ? ErrorEnum.RUNTIME_EXCEPTION.getMessage() :
                (throwable instanceof BusinessException ? throwable.getMessage() : ErrorEnum.RUNTIME_EXCEPTION.getMessage());
        int errCode = ErrorEnum.RUNTIME_EXCEPTION.getCode();

        if (throwable == null) {
            return ResponseResult.error(errCode, errMsg);
        }
        log.error("{}", throwable.getMessage());
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
        return ResponseResult.error(errCode, errMsg);
    }
}