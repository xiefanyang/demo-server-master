package com.hnyr.sys.rbac;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum EnumLogin {
    //

    ERROR_USERNAME_NULL(40001, "必须输入账号"),
    ERROR_PASSWORD_NULL(40002, "必须输入密码"),
    ERROR_USERNAME_NOT_FOUND(40003, "用户名不存在"),
    ERROR_PASSWORD_NOT_MATCH(40004, "密码不正确"),
    ERROR_CAPTCHA_NULL(40005, "必须输入验证码"),
    ERROR_CAPTCHA_NOT_MATCH(40006, "验证码输入错误"),
    ERROR_LOCK_LEFT(40007, "您还有${num}次机会，超出将暂时限制登录"),
    ERROR_LOCKED(40008, "登录失败次数过多已锁定，请${minutes}分钟后重试"),
    ERROR_FAILED_CAPTCHA(40009, "连续输入${num}次错误，请输入验证码"),
    ERROR_NOT_ALLOWED_IP(40010, "当前IP不允许登录该系统"),


    MSG_CHANGE_DEFAULT_PASSWORD(30001, "您需要修改初始密码"),
    ERROR_PASS_LOCKED(30002, "修改密码失败次数过多账户已锁定即将跳转到登录页面，请${minutes}分钟后重试"),
    ERROR_PASSD_LOCKED(30003, "修改密码失败次数过多账户已锁定，请稍后再试"),
    ;

    private int code;
    private String desc;

    EnumLogin(int type, String desc) {
        this.code = type;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String getCodeDesc(int value) {
        EnumLogin[] es = values();
        for (EnumLogin e : es) {
            if (e.code == value) {
                return e.getDesc();
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
        Class<Enum> clz = (Class<Enum>) Class.forName(EnumLogin.class.getName());
        // 2.得到所有枚举常量
        Object[] objects = clz.getEnumConstants();
        Method getCode = clz.getMethod("getType");
        Method getMessage = clz.getMethod("getDesc");
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;
        for (Object obj : objects) {
            map = new HashMap<String, Object>();
            map.put("type", getCode.invoke(obj));
            map.put("desc", getMessage.invoke(obj));
            list.add(map);
        }
        return list;
    }
}
