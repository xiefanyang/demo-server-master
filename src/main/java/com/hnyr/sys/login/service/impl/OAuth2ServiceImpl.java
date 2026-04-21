package com.hnyr.sys.login.service.impl;


import cn.hutool.core.util.URLUtil;
import com.hnyr.sys.config.BusinessException;
import com.hnyr.sys.login.service.OAuth2Service;
import com.hnyr.sys.login.vo.OAuth2ResponseResult;
import com.hnyr.sys.login.vo.OAuth2Token;
import com.hnyr.sys.login.vo.OAuth2User;
import com.hnyr.sys.utils.AssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @ClassName: OAuth2ServiceImpl
 * @Description: OAuth2 service
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class OAuth2ServiceImpl implements OAuth2Service {


    @Value("${security.oauth2.client.client-id}")
    private String clientId;
    @Value("${security.oauth2.client.client-secret}")
    private String clientSecret;
    @Value("${security.oauth2.client.access-token-uri}")
    private String accessTokenUri;
    @Value("${security.oauth2.client.user-authorization-uri}")
    private String userAuthorizationUri;
    @Value("${security.oauth2.client.scope}")
    private String scope;

    @Value("${security.oauth2.resource.user-info-uri}")
    private String userInfoUri;


    @Resource
    RestTemplate restTemplate;

    @Override
    public String getAuthorizeUrl(String redirectUri) {
        // 1. 生成随机字符串state
        String state = RandomStringUtils.randomAlphanumeric(6);
        String encodeRedirectUri = URLUtil.encode(redirectUri);
        // 2. 拼接url
        return String.format("%s?response_type=code&client_id=%s&redirect_uri=%s&scope=%s&state=%s",
                userAuthorizationUri, clientId, encodeRedirectUri, scope, state);
    }

    @Override
    public OAuth2Token getAccessToken(String code, String redirectUri) {
        log.info("code: {}", code);
        AssertUtil.isTrue(StringUtils.isNotEmpty(code), "code不能为空");
        String encodeRedirectUri = URLUtil.encode(redirectUri);
        // 拿到code之后获取access_token
        String url =
                String.format("%s?grant_type=authorization_code&client_id=%s&client_secret=%s&code=%s&redirect_uri=%s",
                        accessTokenUri, clientId, clientSecret, code, encodeRedirectUri);
        log.info(url);
        ResponseEntity<OAuth2ResponseResult<OAuth2Token>> tokenEntity = restTemplate.exchange(url, HttpMethod.POST, null,
                new ParameterizedTypeReference<OAuth2ResponseResult<OAuth2Token>>() {
                });
        log.info("tokenEntity: {}", tokenEntity);
        OAuth2ResponseResult<OAuth2Token> result = tokenEntity.getBody();
        if (Objects.isNull(result)) {
            throw new BusinessException("获取access_token失败");
        }
        log.info("tokenEntity: {}", result);
        if (!result.isSuccess()) {
            throw new RuntimeException(result.getErrMsg());
        }

        return result.getData();
    }

    @Override
    public OAuth2User getUser(String token) {
        AssertUtil.isTrue(StringUtils.isNotEmpty(token),
                "access_token不能为空");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<Object> entity = new HttpEntity<>(null, headers);
        ResponseEntity<OAuth2ResponseResult<OAuth2User>> responseEntity = restTemplate.exchange(userInfoUri,
                HttpMethod.POST, entity, new ParameterizedTypeReference<OAuth2ResponseResult<OAuth2User>>() {
                });
        OAuth2ResponseResult<OAuth2User> result = responseEntity.getBody();
        log.info("userResult: {}", result);
        if (Objects.isNull(result)) {
            throw new RuntimeException("获取用户信息失败");
        }
        if (!result.isSuccess()) {
            throw new RuntimeException(result.getErrMsg());
        }

        return result.getData();
    }
}
