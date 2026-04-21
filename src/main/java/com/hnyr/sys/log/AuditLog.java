package com.hnyr.sys.log;

import java.lang.annotation.*;

/**
 * @ClassName: AuditLog
 * @Description: 操作日志自定义注解
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AuditLog {
    /**
     * 操作名称
     */
    String operation() default "";
}
