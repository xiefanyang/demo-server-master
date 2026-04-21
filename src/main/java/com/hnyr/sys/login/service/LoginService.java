package com.hnyr.sys.login.service;

import com.hnyr.sys.rbac.entity.dto.SysUserDto;
import com.hnyr.sys.rbac.entity.vo.TokenUserVo;

/**
 * @ClassName: LoginService
 * @Description:
 * @Author: demo
 * @CreateDate: 2023/9/27 11:20
 * @Version: 1.0
 */
public interface LoginService {
    /**
     * 用户名密码登录
     *
     * @param username  用户名/手机号/工学号/
     * @param password
     * @param requestIp
     * @return
     */
    TokenUserVo login(String username, String password, String requestIp);

    /**
     * 用户退出
     * @param token
     */
    void logout(String token);
    /**
     * 用户登录验证 （安全）
     * @param code     用户名/email/mobile/工号学号（明文）
     * @param password 密码（加密）
     * @return
     */
    SysUserDto findAccountByPasswordSecurity(String code, String password);

    public Integer bgLoginTooManyLeftTimes(String username);


    /**
     * 统一门户授权登录
     *
     * @param username  用户名/手机号/工学号/
     * @param requestIp
     * @return
     */
    TokenUserVo auth(String username, String requestIp);
}
