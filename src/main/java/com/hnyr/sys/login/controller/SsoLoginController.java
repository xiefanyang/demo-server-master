package com.hnyr.sys.login.controller;

import cn.hutool.core.map.MapUtil;
import com.hnyr.sys.config.BusinessException;
import com.hnyr.sys.config.ErrorEnum;
import com.hnyr.sys.log.AuditLog;
import com.hnyr.sys.login.service.LoginService;
import com.hnyr.sys.login.service.OAuth2Service;
import com.hnyr.sys.login.vo.OAuth2User;
import com.hnyr.sys.rbac.entity.vo.TokenUserVo;
import com.hnyr.sys.utils.NetworkUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: SysLoginController
 * @Description: sso相关
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
@Slf4j
@Api(tags = "sso")
@RequestMapping("/api")
@RestController
public class SsoLoginController {
    @Resource
    LoginService loginService;

    @Resource
    OAuth2Service oAuth2Service;

    @Resource
    private RedisTemplate redisTemplate;

    @PostMapping({"/sys/login/oauth2", "/wap/login/oauth2"})
    @ApiOperation(value = "用户登录（统一门户授权）")
    @AuditLog
    public Object oauth2Login(HttpServletRequest request, @RequestBody Map<String, Object> params) {
        String key = MapUtil.getStr(params, "key");
        String accessToken = (String) redisTemplate.opsForValue().get("OAUTH2_ACCESS_TOKEN:" + key);
        if (org.apache.commons.lang3.StringUtils.isEmpty(accessToken)) {
            throw new BusinessException(ErrorEnum.NO_AUTH.getCode(), "统一门户授权失败：无效key");
        }
        try {
            // 获取用户信息
            OAuth2User user = oAuth2Service.getUser(accessToken);
            // 获取登录Token
            TokenUserVo vo = loginService.auth(user.getSerialNo(), NetworkUtil.getIpAddress(request));
            Map<String, Object> result = new HashMap<>();
            result.put("token", vo.getToken());
            // 密码更新状态：0 不需要更新
            result.put("pwdStatus", 0);
            return result;
        } catch (Exception e) {
            log.error("{}", e.getMessage());
            throw new BusinessException(ErrorEnum.NO_AUTH.getCode(), e instanceof BusinessException ? e.getMessage() : "统一门户授权失败");
        } finally {
            // 删除缓存
            redisTemplate.delete("OAUTH2_ACCESS_TOKEN:" + key);
        }
    }
}
