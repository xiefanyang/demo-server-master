package com.hnyr.sys.login.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.core.util.StrUtil;
import com.hnyr.sys.config.BusinessException;
import com.hnyr.sys.login.service.CaptchaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: CaptchaHandler
 * @Description: 验证码处理器
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
@Service
@Slf4j
public class CaptchaServiceImpl implements CaptchaService {

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 校验验证码
     *
     * @param imageKey
     * @param imageCode
     **/
    @Override
    public void checkImageCode(String imageKey, String imageCode) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String text = ops.get("imageCode:" + imageKey);
        if (StrUtil.isBlank(text)) {
            throw new BusinessException("验证码已失效");
        }
        // 根据判断验证码否成功
        if (!text.equals(imageCode.toLowerCase())) {
            throw new BusinessException("验证码输入错误");
        }
        //比对成功移除
        redisTemplate.delete(imageKey);
    }

    /**
     * 缓存验证码，有效期5分钟
     *
     * @param imageKey
     * @param code
     **/
    @Override
    public void saveImageCode(String imageKey, String code) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set("imageCode:" + imageKey, code.toLowerCase(), 5, TimeUnit.MINUTES);
    }

    /**
     * 获取验证码拼图（生成的抠图和带抠图阴影的大图及抠图坐标）
     **/
    @Override
    public String getCaptcha(String imageKey) {
        //参数校验
        ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(120, 35, 4, 1);
        saveImageCode(imageKey, captcha.getCode());
        String dataBase64 = captcha.getImageBase64Data();
        return dataBase64;

    }
}

