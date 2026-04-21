package com.hnyr.sys.login.service;

/**
 * @ClassName: CapthaService
 * @Description:
 * @Author: demo
 * @CreateDate: 2023/9/27 11:20
 * @Version: 1.0
 */
public interface CaptchaService {
    /**
     * 检查
     * @param imageKey
     * @param imageCode
     * @return
     */
    void checkImageCode(String imageKey, String imageCode);

    /**
     * 保存图片验证码（按 key 存放图片 code 缓存）
     *
     * @param imageKey
     * @param code
     */
    void saveImageCode(String imageKey, String code);

    /**
     * 获取图片验证码
     *
     * @param imageKey 需全局唯一
     * @return
     */
    String getCaptcha(String imageKey);
}
