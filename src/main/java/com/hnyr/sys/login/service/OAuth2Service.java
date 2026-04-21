package com.hnyr.sys.login.service;

import com.hnyr.sys.login.vo.OAuth2Token;
import com.hnyr.sys.login.vo.OAuth2User;

/**
 * 统一门户授权登录
 */
public interface OAuth2Service {

    /**
     * 获取授权url
     *
     * @return
     */
    String getAuthorizeUrl(String redirectUri);


    /**
     * 通过code换access_token
     *
     * @param code
     * @return
     */
    OAuth2Token getAccessToken(String code, String redirectUri);


    OAuth2User getUser(String token);
}
