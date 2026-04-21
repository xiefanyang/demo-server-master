package com.hnyr.sys.login.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.hnyr.sys.config.BusinessException;
import com.hnyr.sys.login.service.LoginService;
import com.hnyr.sys.rbac.EnumLogin;
import com.hnyr.sys.rbac.dao.SysUserDao;
import com.hnyr.sys.rbac.entity.dto.SysUserDto;
import com.hnyr.sys.rbac.entity.vo.TokenUserVo;
import com.hnyr.sys.rbac.security.PermissionHandler;
import com.hnyr.sys.rbac.service.LoginSecurityService;
import com.hnyr.sys.utils.AssertUtil;
import com.hnyr.sys.utils.SmUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @ClassName: LoginServiceImpl
 * @Description: 登录相关 service
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class LoginServiceImpl implements LoginService {

    @Resource
    RedisTemplate redisTemplate;
    @Resource
    LoginSecurityService loginSecurityService;
    @Value("${hnyr.login.fail.locked}")
    private Integer LOGIN_FAIL_LOCK_COUNT;
    @Value("${hnyr.token.timeout:60}")
    Integer expireMinutes;

    @Resource
    PermissionHandler permissionHandler;
    @Resource
    SysUserDao sysUserDao;

    @SneakyThrows
    @Override
    public SysUserDto findAccountByPasswordSecurity(String code, String password) {
        SysUserDto user = sysUserDao.getByUserNameOrEmailOrIdCardOrMobile(code);
        AssertUtil.isTrue(null != user, "用户不存在");
        AssertUtil.isTrue(user.getState() == 0, "用户已冻结");
        //国密算法
        AssertUtil.isTrue(SmUtils.sm3Enc(password).equals(user.getPassword()), "密码错误");
        return user;
    }

    public TokenUserVo login(String username, String password, String requestIp) {
        int bgLoginTooManyLeftTimes = loginSecurityService.loginTooManyLeftTimes(username);
        TokenUserVo vo = new TokenUserVo();
        try {
            allowIp(requestIp);
            SysUserDto account = findAccountByPasswordSecurity(username, password);
            BeanUtil.copyProperties(account, vo);
            vo.setToken(permissionHandler.createJwtToken(vo));
            log.info("登录成功：{}", account.getUsername());
            loginSecurityService.clearLoginTimes(username);
        } catch (Exception e) {
            loginFailed(e, username, bgLoginTooManyLeftTimes);
        }

        return vo;
    }

    @Override
    public void logout(String token) {
        permissionHandler.deleteToken(token);
    }

    /**
     * 检查IP白名单，并获取剩余登录次数
     *
     * @param remoteIp
     * @return
     */
    private void allowIp(String remoteIp) {
        if (!loginSecurityService.loginIpAllowed(remoteIp)) {
            throw new BusinessException(EnumLogin.ERROR_NOT_ALLOWED_IP.getCode(), EnumLogin.ERROR_NOT_ALLOWED_IP.getDesc());
        }
    }

    /**
     * 登录失败处理
     *
     * @param e
     * @param username
     * @param bgLoginTooManyLeftTimes
     */
    private void loginFailed(Exception e, String username, int bgLoginTooManyLeftTimes) {
        log.error("e : {}", e.getMessage());
        loginSecurityService.loginFailedIncrease(username);
        if (bgLoginTooManyLeftTimes <= LOGIN_FAIL_LOCK_COUNT) {
            throw new BusinessException(EnumLogin.ERROR_LOCK_LEFT.getCode(), e.getMessage() + " " + EnumLogin.ERROR_LOCK_LEFT.getDesc().replace("${num}", bgLoginTooManyLeftTimes + ""));
        }
        throw new BusinessException(e.getMessage());
    }

    public Integer bgLoginTooManyLeftTimes(String username) {
        return loginSecurityService.loginTooManyLeftTimes(username);
    }

    @Override
    public TokenUserVo auth(String username, String requestIp) {
        int bgLoginTooManyLeftTimes = loginSecurityService.loginTooManyLeftTimes(username);
        SysUserDto account;
        TokenUserVo vo = new TokenUserVo();
        try {
            allowIp(requestIp);
            account = sysUserDao.getByUserNameOrEmailOrIdCardOrMobile(username);
            if (Objects.isNull(account)) {
                throw new BusinessException("找不到该账户信息");
            }
            BeanUtil.copyProperties(account, vo);
            // 设置token
            vo.setToken(permissionHandler.createJwtToken(vo));
            log.info("登录成功：{}", account.getUsername());
            loginSecurityService.clearLoginTimes(username);
        } catch (Exception e) {
            loginFailed(e, username, bgLoginTooManyLeftTimes);
        }
        return vo;
    }

}
