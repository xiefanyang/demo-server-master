package com.hnyr.sys.login.controller;

import cn.hutool.core.util.IdUtil;
import com.hnyr.sys.config.BusinessException;
import com.hnyr.sys.config.ErrorEnum;
import com.hnyr.sys.config.ResponseResult;
import com.hnyr.sys.log.AuditLog;
import com.hnyr.sys.login.service.CaptchaService;
import com.hnyr.sys.login.service.LoginService;
import com.hnyr.sys.login.vo.LoginUser;
import com.hnyr.sys.rbac.EnumLogin;
import com.hnyr.sys.rbac.RbacConstant;
import com.hnyr.sys.rbac.entity.vo.TokenUserVo;
import com.hnyr.sys.rbac.security.SecurityConstant;
import com.hnyr.sys.rbac.security.UserPermissionVo;
import com.hnyr.sys.rbac.service.PermissionService;
import com.hnyr.sys.utils.AssertUtil;
import com.hnyr.sys.utils.NetworkUtil;
import com.hnyr.sys.utils.SmUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: SysLoginController
 * @Description: 登录登出相关
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
@Slf4j
@Api(tags = "登录相关")
@RequestMapping("/api")
@RestController
public class SysLoginController {
    @Resource
    LoginService loginService;
    @Resource
    PermissionService permissionService;
    @Resource
    CaptchaService captchaService;
    /**
     * 密码修改处理：0 不需修改 -1 初始密码修改（强制） 1 强制修改（如超期） 2 提示修改
     * 仅正常通过 web 或 wap 端登录时 验证，单点登录的不进行本系统密码校验
     */
    private final int passwordNoChange = 0;
    private final int passwordInitChange = -1;
    private final int passwordMustChange = 1;
    private final int passwordNeedChange = 2;
    @Value("${hnyr.login.pwd.timeout:90}")
    private Integer passwordOutDays;
    @Value("${hnyr.login.fail.locked}")
    private Integer loginFailLimit;
    //是否超期必须修改密码
    private Boolean timeoutMustChangePassword = true;

    @Value("${security.oauth2.server.logout-uri}")
    private String oauth2LogoutUrl;

    @ApiOperation(value = "生成验证码拼图")
    @PostMapping("/sys/captcha/get-captcha")
    public ResponseResult getCaptcha(HttpServletRequest request, HttpServletResponse response) {
        String salt = IdUtil.fastSimpleUUID();
        String dataBase64 = captchaService.getCaptcha(salt);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("salt", salt);
        map.put("captcha", dataBase64);
        return ResponseResult.success(map);
    }

    /**
     * 验证密码限定
     *
     * @param vo
     * @return
     */
    private Integer checkPassword(TokenUserVo vo) {
        Integer pwdStatus = passwordNoChange;
        if (vo.getUpdatePasswordTime() == null || vo.getUpdatePasswordTime().longValue() == 0L) {
            //初始密码必须修改
            pwdStatus = passwordInitChange;
        } else {
            DateTime now = DateTime.now();
            if (now.isAfter(new DateTime(vo.getUpdatePasswordTime()).plusDays(passwordOutDays))) {
                //超过90天 需要提示修改密码
                pwdStatus = timeoutMustChangePassword ? passwordMustChange : passwordNeedChange;
            }
        }
        return pwdStatus;
    }

    /**
     * 验证码校验
     *
     * @param loginUser
     */
    private void checkCaptcha(LoginUser loginUser) {
        //参数中包含验证码
        String uCaptcha = loginUser.getCaptcha();
        if (StringUtils.hasText(uCaptcha)) {
            // 比对验证码
            captchaService.checkImageCode(loginUser.getSalt(), uCaptcha);
        } else {
            // 如默认不显示验证码，限定登录几次后显示验证码
            Integer loginLeftCount = loginService.bgLoginTooManyLeftTimes(loginUser.getUsername());
            if (loginLeftCount < loginFailLimit) {
                AssertUtil.isTrue(StringUtils.hasText(uCaptcha), EnumLogin.ERROR_CAPTCHA_NULL.getDesc());
            }
        }
    }

    @PostMapping({"/sys/login", "/wap/login"})
    @ApiOperation(value = "用户登录")
    @AuditLog
    public Object login(HttpServletRequest request, @RequestBody LoginUser loginUser) {
        try {
            checkCaptcha(loginUser);
            String username = loginUser.getUsername();
            AssertUtil.isTrue(null != username, EnumLogin.ERROR_USERNAME_NULL.getDesc());
            String password = loginUser.getPassword();
            AssertUtil.isTrue(null != password, EnumLogin.ERROR_PASSWORD_NULL.getDesc());
            log.debug("{} {}", username, password);
            password = cn.hutool.core.codec.Base64.decodeStr(password, "UTF-8");
            password = SmUtils.sm4DecFromFont(password);
            TokenUserVo vo = loginService.login(username, password, NetworkUtil.getIpAddress(request));
            Map<String, Object> result = new HashMap<>();
            result.put("token", vo.getToken());
            result.put("pwdStatus", checkPassword(vo));
            return result;
        } catch (Exception e) {
            log.error("{}", e.getMessage());
            throw new BusinessException(ErrorEnum.NO_AUTH.getCode(), e instanceof BusinessException ? e.getMessage() : "登录失败");
        }
    }

    @PostMapping({"/sys/userinfo", "/wap/userinfo"})
    @ApiOperation(value = "获取用户信息与权限")
    @AuditLog
    public Object userInfo(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo u) {
        UserPermissionVo permissionVo = null;
        if (u.getId() == 1) {
            //TODO 暂时测试用，后续需要移除
            permissionVo = permissionService.getSuperAdminPermission();

        } else {
            permissionVo = permissionService.renewAccountPermissionByUserId(u.getId());
        }
        //做三员管理，需要移除有超级管理员权限的相关内容，所以系统管理员不在具有超级管理员权限，如有必要，需要创建相关的角色绑定对应的资源即可
//        permissionVo = permissionService.renewAccountPermissionByUserId(uid);
        List<String> permissions = permissionVo == null ? new ArrayList<>() : permissionVo.getResources();
        u.setRes(permissions);
        u.setId(null);
        Map<String, Object> map = new HashMap<>();
        map.put("name", u.getName());
        map.put("idNumber", u.getUsername());
        map.put("nickname", u.getNickname());
        map.put("avatar", StringUtils.hasText(u.getAvatar()) ? u.getAvatar() : RbacConstant.LOGO_USER_DEFAULT);
        map.put("gender", u.getGender());
        map.put("permissions", u.getRes());
        return map;
    }

    @AuditLog
    @PostMapping("/sys/logout")
    @ApiOperation(value = "用户退出")
    public Object logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(token)) {
            loginService.logout(token);
        }
        return ResponseResult.success(oauth2LogoutUrl);
    }
}
