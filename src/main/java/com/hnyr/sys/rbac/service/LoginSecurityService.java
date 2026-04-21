package com.hnyr.sys.rbac.service;

public interface LoginSecurityService {

    /**
     * 是否允许通过IP
     *
     * @param ip
     * @return
     */
    Boolean loginIpAllowed(String ip);

    /**
     * 登录剩余次数
     *
     * @param username
     * @return
     */
    Integer loginTooManyLeftTimes(String username);

    /**
     * 失败次数累加
     *
     * @param username
     */
    void loginFailedIncrease(String username);

    /**
     * 清理登录次数
     *
     * @param username
     */
    void clearLoginTimes(String username);
}
