package com.hnyr.sys.rbac.service.impl;

import com.google.common.collect.Lists;
import com.hnyr.sys.config.BusinessException;
import com.hnyr.sys.rbac.EnumLogin;
import com.hnyr.sys.rbac.service.LoginSecurityService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class LoginSecurityServiceImpl implements LoginSecurityService {
    @Resource
    RedisTemplate redisTemplate;

    String allAllow = "0.0.0.0";

    String ipBsLogin = "ip_login_bs";

    String loginTime = "login:bs:times:";
    //10分钟
    int loginErrorTime = 10 * 60;
    //登录次数限制
    int loginTimeLimit = 10;

    @Override
    public Boolean loginIpAllowed(String ip) {
        log.info("检查后台登录请求IP：{}", ip);
        BoundHashOperations operations = redisTemplate.boundHashOps(ipBsLogin);
        if (operations.size() > 0) {
            String allow = (String) operations.get("allow");
            String deny = (String) operations.get("deny");
            if (deny != null) {
                String[] denys = deny.split(",");
                List<String> ipList = Lists.newArrayList(denys);
                if (ipList.contains(ip)) {
                    log.info("拒绝IP后台登录：{}", ip);
                    return false;
                }
            }
            if (allow != null) {
                String[] ipsArray = allow.split(",");
                List<String> ipList = Lists.newArrayList(ipsArray);
                if (ipList.contains(allAllow) || ipList.contains(ip)) {
                    log.info("允许IP后台登录：{}", ip);
                    return true;
                }
            }
            log.info("拒绝IP后台登录：{}", ip);
            return false;
        }
        return true;
    }

    @Override
    public Integer loginTooManyLeftTimes(String username) {
        String key = getKey(username);
        BoundListOperations listOperations = redisTemplate.boundListOps(key);
        Long times = listOperations.size();
        Long loginTimes = times == null ? 1 : times;
        if (loginTimes != null) {
            if (loginTimes > loginTimeLimit) {
                //超过次数了
                return -1;
            } else {
                loginTimes++;
            }
        }
        int loginTooManyLeftTimes = loginTimeLimit - loginTimes.intValue();
        if (loginTooManyLeftTimes == -1) {
            log.error("登录次数过多限制" + loginErrorTime + "分钟   {} ", username);
            throw new BusinessException(EnumLogin.ERROR_LOCKED.getCode(), EnumLogin.ERROR_LOCKED.getDesc().replace("${minutes}", loginErrorTime + ""));
        }
        return loginTooManyLeftTimes;
    }

    @Override
    public void loginFailedIncrease(String username) {
        String key = getKey(username);
        BoundListOperations listOperations = redisTemplate.boundListOps(key);
        Long loginTimes = listOperations.size();
        loginTimes++;
        listOperations.leftPush(DateTime.now().toString("yyyyMMddHHmmss"));
        if (loginTimes == 1) {
            listOperations.expire(loginErrorTime, TimeUnit.SECONDS);
        } else {
            listOperations.expire(listOperations.getExpire(), TimeUnit.SECONDS);
        }
    }

    private String getKey(String username) {
        return loginTime + username;
    }

    @Override
    public void clearLoginTimes(String username) {
        String key = getKey(username);
        redisTemplate.delete(key);
    }
}
