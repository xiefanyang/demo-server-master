package com.hnyr.sys.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @ClassName: HttpInvokerServiceExporterAspect
 * @Description: CVE-2016-1000027安全漏洞补丁方案
 * @Author: demo
 * @CreateDate: 2023/10/17 20:25
 * @Version: 1.0
 */
@Component
@Aspect
@Slf4j
public class HttpInvokerServiceExporterAspect {
    /**
     * 设置AOP切点
     */
    @Pointcut("execution(* org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter.handleRequest(..))")
    public void myPointcut() {
    }


    /**
     * 执行方法环绕
     *
     * @param point
     */
    @Around(value = "myPointcut()")
    public Object aroundHandleRequest(ProceedingJoinPoint point) throws Throwable {
        log.info("-----------禁用org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter.handleRequest，防止CVE-2016-1000027安全漏洞------------");
        return null;
    }
}
