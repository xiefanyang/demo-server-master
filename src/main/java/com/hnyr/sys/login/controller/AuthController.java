package com.hnyr.sys.login.controller;


import cn.hutool.core.map.MapUtil;
import com.hnyr.sys.config.BusinessException;
import com.hnyr.sys.config.ResponseResult;
import com.hnyr.sys.login.service.OAuth2Service;
import com.hnyr.sys.login.vo.OAuth2Token;
import com.hnyr.sys.rbac.security.PermissionHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: AuthController
 * @Description: 统一身份对接
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
@Controller
@Slf4j
@RequestMapping("/api")
public class AuthController {

    @Resource
    OAuth2Service oAuth2Service;

    @Resource
    PermissionHandler permissionHandler;

    @Resource
    RedisTemplate redisTemplate;

    // 业务回调处理地址
    @Value("${bis.oauth2.callback.redirect-uri}")
    private String redirectUri;

    // 手机端回调处理地址
    @Value("${bis.oauth2.wap.callback.redirect-uri}")
    private String wapRedirectUri;

    @Value("${bis.oauth2.frontend.redirect-uri}")
    String targetUrl;

    @Value("${bis.oauth2.wap.frontend.redirect-uri}")
    String wapTargetUrl;

    /**
     * OAuth2 授权回调
     *
     * @return
     */
    @GetMapping("/open/oauth2/callback")
    public String callback(String code) {
        try {
            // 获取access_token
            OAuth2Token accessToken = oAuth2Service.getAccessToken(code, redirectUri);
            // 缓存access_token
            redisTemplate.opsForValue().set("OAUTH2_ACCESS_TOKEN:" + accessToken.getJti(),
                    accessToken.getAccessToken(),
                    accessToken.getExpiresIn(),
                    TimeUnit.SECONDS);
            return "redirect:" + targetUrl + "?key=" + accessToken.getJti();
        } catch (Exception e) {
            log.error("{}", e.getMessage());
            throw new BusinessException(e.getMessage());
        }
    }


    /**
     * OAuth2 授权回调
     *
     * @return
     */
    @GetMapping("/open/wap/oauth2/callback")
    public String wapCallback(String code) {
        try {
            // 获取access_token
            OAuth2Token accessToken = oAuth2Service.getAccessToken(code, wapRedirectUri);
            // 缓存access_token
            redisTemplate.opsForValue().set("OAUTH2_ACCESS_TOKEN:" + accessToken.getJti(),
                    accessToken.getAccessToken(),
                    accessToken.getExpiresIn(),
                    TimeUnit.SECONDS);
            return "redirect:" + wapTargetUrl + "?key=" + accessToken.getJti();
        } catch (Exception e) {
            log.error("{}", e.getMessage());
            throw new BusinessException(e.getMessage());
        }
    }


    @PostMapping({"/sys/login/token/check", "/wap/login/token/check"})
    @ResponseBody
    public ResponseResult<Integer> validateToken(@RequestBody Map<String, String> params) {
        String token = MapUtil.getStr(params, "token");
        if (StringUtils.isNotEmpty(token) && permissionHandler.validateJwtToken(token)) {
            return ResponseResult.success(1);
        }
        return ResponseResult.success(0);
    }
}
