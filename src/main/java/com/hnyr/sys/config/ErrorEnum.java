package com.hnyr.sys.config;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: ErrorEnum
 * @Description: 公共错误码枚举
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
public enum ErrorEnum {
    //
    NO_AUTH(4001, "用户状态失效，请重新登录"),
    INVALID_AUTH(4002, "无效的访问令牌，请重新登录"),

    INVALID_PERMISSION(4003, "请检查是否有访问权限"),

    NOT_FOUND(4004, "请求的资源不存在"),
    RUNTIME_EXCEPTION(5000, "发生服务端运行异常");

    private int code;
    private String message;

    ErrorEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static String getCodeMessage(int value) {
        ErrorEnum[] es = values();
        for (ErrorEnum e : es) {
            if (e.code == value) {
                return e.getMessage();
            }
        }
        return null;
    }

    /**
     * 根据枚举的字符串获取枚举的值
     *
     * @return
     * @throws Exception
     */
    public static List<Map<String, Object>> getAllEnum() throws Exception {
        // 得到枚举类对象
        Class<Enum> clz = (Class<Enum>) Class.forName(ErrorEnum.class.getName());
        // 2.得到所有枚举常量
        Object[] objects = clz.getEnumConstants();
        Method getCode = clz.getMethod("getCode");
        Method getMessage = clz.getMethod("getMessage");
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;
        for (Object obj : objects) {
            map = new HashMap<String, Object>();
            map.put("code", getCode.invoke(obj));
            map.put("message", getMessage.invoke(obj));
            list.add(map);
        }
        return list;
    }
}
