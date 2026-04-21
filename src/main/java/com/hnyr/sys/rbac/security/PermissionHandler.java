package com.hnyr.sys.rbac.security;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.JWTValidator;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.hnyr.sys.config.BusinessException;
import com.hnyr.sys.config.ErrorEnum;
import com.hnyr.sys.rbac.entity.vo.TokenUserVo;
import com.hnyr.sys.utils.AssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class PermissionHandler {
    @Resource
    RedisTemplate redisTemplate;

    @Value("${hnyr.token.key}")
    String sessionKey;
    @Value("${hnyr.token.refresh:30}")
    Integer refreshTime;
    @Value("${hnyr.jwt.key}")
    String jwtKey;
    @Value("${hnyr.token.timeout:60}")
    Integer expireMinutes;

    public String createJwtToken(String jwtKey, Map<String, Object> map, Integer expireMinutes) {
        JWT jwt = JWT.create().setSigner(JWTSignerUtil.hs512(jwtKey.getBytes())).addPayloads(map)
                .setIssuedAt(DateUtil.date())
                .setExpiresAt(DateUtil.offsetMinute(DateUtil.date(), expireMinutes));
        return jwt.sign();
    }

    public static boolean verifyJwtToken(String token, String jwtKey) {
        boolean flag = false;
        JWTSigner signer = JWTSignerUtil.hs512(jwtKey.getBytes());
        try {
            //校验signer 与 有效时间
            JWTValidator.of(token).validateAlgorithm(signer).validateDate(DateUtil.date());
            flag = JWTUtil.verify(token, signer);
        } catch (Exception e) {
            log.error("{}", e.getMessage());
            throw new BusinessException(ErrorEnum.INVALID_AUTH.getCode(), ErrorEnum.INVALID_AUTH.getMessage());
        }
        if (flag == false) {
            throw new BusinessException(ErrorEnum.NO_AUTH.getCode(), ErrorEnum.NO_AUTH.getMessage());
        }
        AssertUtil.isTrue(JWTUtil.verify(token, signer), "令牌已过期");
        return true;
    }

    public String createJwtToken(TokenUserVo vo) {
        Map<String, Object> m = new HashMap<String, Object>() {
            {
                put("uid", vo.getId());
//                put("uname", vo.getUsername());
//                put("name", vo.getName());
            }
        };

        String token = createJwtToken(jwtKey, m, expireMinutes);
        setUserCache(token, vo);
        return token;
    }

    private void setUserCache(String token, TokenUserVo vo) {
        redisTemplate.opsForValue().set(sessionKey + token, JSONUtil.toJsonStr(vo), expireMinutes, TimeUnit.MINUTES);
    }

    public TokenUserVo getTokenUser(String token) {
        token = sessionKey + token;
        long time = redisTemplate.getExpire(token, TimeUnit.MINUTES);
//        log.info("token 过期时间剩余：{}", time);
        if (time < 0) {
            throw new BusinessException(ErrorEnum.NO_AUTH.getCode(), ErrorEnum.NO_AUTH.getMessage());
        } else {
            TokenUserVo u = JSONUtil.toBean(redisTemplate.opsForValue().get(token).toString(), TokenUserVo.class);
            AssertUtil.notNull(u, "登录令牌已失效");
            //小于三十分钟续期
            if (time < expireMinutes) {
                redisTemplate.expire(token, expireMinutes, TimeUnit.MINUTES);
            }
            return u;
        }
    }

    public boolean validateJwtToken(String token) {
        token = sessionKey + token;
        long time = redisTemplate.getExpire(token, TimeUnit.MINUTES);
        if (time < 0) {
            return false;
        } else {
            TokenUserVo u = JSONUtil.toBean(redisTemplate.opsForValue().get(token).toString(), TokenUserVo.class);
            AssertUtil.notNull(u, "登录令牌已失效");
            //续期
            if (time < refreshTime) {
                setUserCache(token, u);
            }
            return true;
        }
    }


    private String getAclKey(Long userId) {
        return SecurityConstant.CACHE_RESOURCE_VALID + ":" + userId;
    }

    /**
     * 资源是否需要判断权限
     *
     * @param userId   用户ID
     * @param resource 资源code 多个
     * @return
     */
    public Boolean isResourceValid(Long userId, String[] resource) {
        Object cache = redisTemplate.opsForValue().get(getAclKey(userId));
        if (cache == null) {
            return false;
        } else {
            UserPermissionVo permission = JSONUtil.toBean(cache.toString(), UserPermissionVo.class);
            if (permission != null && !CollectionUtils.isEmpty(permission.getResources())) {
                for (String r : resource) {
                    if (permission.getResources().contains(r)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public void deleteToken(String token) {
        token = sessionKey + token;
        if (redisTemplate.hasKey(token)) {
            redisTemplate.delete(token);
        }
    }
}
