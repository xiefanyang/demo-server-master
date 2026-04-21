package com.hnyr.sys.utils;

import com.hnyr.sys.config.BusinessException;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Map;

/**
 * @ClassName: AssertUtil
 * @Description: Assert工具类包装
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
public class AssertUtil {
    /**
     * 服务调用异常
     *
     * @param expression
     * @param message
     */
    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new BusinessException(message);
        }
    }

    public static void notNull(Object o, String message) {
        if (null == o) {
            throw new BusinessException(message);
        }
    }

    public static void notBlank(Object o, String message) {
        if (null == o || !StringUtils.hasText(o.toString())) {
            throw new BusinessException(message);
        }
    }

    public static void notEmpty(Collection c, String message) {
        if (CollectionUtils.isEmpty(c)) {
            throw new BusinessException(message);
        }
    }

    public static void notEmpty(Map c, String message) {
        if (CollectionUtils.isEmpty(c)) {
            throw new BusinessException(message);
        }
    }

    public static void throwException(String message) {
        throw new BusinessException(message);
    }

    public static void throwException(int code, String message) {
        throw new BusinessException(code, message);
    }

}
