package com.hnyr.sys.log;

import com.hnyr.sys.config.BusinessException;
import com.hnyr.sys.log.mongo.LogAuditDoc;
import com.hnyr.sys.rbac.entity.vo.TokenUserVo;
import com.hnyr.sys.rbac.security.SecurityConstant;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.Filter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: ControllerMethodAspect
 * @Description: 操作日志切面
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
@Slf4j
@Aspect
public class ControllerMethodAspect {

    @Value("${audit.log.enable:'true'}")
    private String auditLogEnable;

    private AuditContext auditContext;

    private String application;

    public ControllerMethodAspect(AuditContext auditContext, String application) {
        this.auditContext = auditContext;
        this.application = application;
    }

    @SuppressWarnings("unchecked")
    @Around("@annotation(com.hnyr.sys.log.AuditLog)")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        HttpServletRequest request = getRequest();
        String flag = "false";
        if (flag.equals(auditLogEnable)) {
            return point.proceed();
        }

        if (request == null) {
            return point.proceed();
        }
        try {
            Method method = ((MethodSignature) point.getSignature()).getMethod();

            AuditLog auditLog = method.getAnnotation(AuditLog.class);
            if (auditLog == null) {
                return point.proceed();
            }
            String operation = auditLog.operation();
            if (!StringUtils.hasText(operation)) {
                ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
                if (apiOperation == null || apiOperation.value() == null) {
                    throw new BusinessException(9000, "日志：缺少注解");
                }
                operation = apiOperation.value();
            }
            LogAuditDoc record = new LogAuditDoc();
            record.setName(operation);
            record.setApplication(application);
            record.setBegin(System.currentTimeMillis());
            record.setSignature(point.getSignature().toString());

            record.setIp(getIpAddress(request));
            record.setUrl(request.getRequestURL().toString());
            if (point.getArgs() != null) {
                Object[] args = point.getArgs();
                List list = new ArrayList();
                if (args.length > 0) {
                    for (Object o : args) {
                        if (o instanceof HttpSession || o instanceof Filter || o instanceof MultipartFile
                                || o instanceof File || o instanceof Model
                                || o instanceof ServletRequest || o instanceof ServletResponse) {
                            // 忽略掉排除入参
                        } else {
                            list.add(o);
                        }
                    }
                    if (!CollectionUtils.isEmpty(list)) {
                        record.setParams(list);
                    }
                }
            }
            TokenUserVo tUser = (TokenUserVo) request.getAttribute(SecurityConstant.TOKEN_USER_KEY);
            if (tUser != null) {
                record.setUserId(tUser.getId());
            }
            record.setUserAgent(request.getHeader("User-Agent"));

            Object result = null;

            try {
                result = point.proceed();
                //成功状态
                record.setSuccess(true);
            } catch (Throwable e) {
                //成功状态
                record.setSuccess(false);
                //错误信息
                try {
                    if (e != null) {
                        record.setException(e.getMessage());
                    }
                } catch (Exception ex) {
                    ;
                }
                throw e;
            } finally {
                //操作结束时间
                record.setEnd(System.currentTimeMillis());
                record.setTime(record.getEnd() - record.getBegin());
                auditContext.record(record);
            }
            return result;

        } catch (Exception e) {
//            log.error("audit record error: " + e.getMessage(), e);
            throw e;
        }

    }


    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }


    /**
     * 通过Request 获取ip
     *
     * @return String
     */
    private String getIpAddress(HttpServletRequest request) {
        String unknown = "unknown";
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip.trim();
    }


}
