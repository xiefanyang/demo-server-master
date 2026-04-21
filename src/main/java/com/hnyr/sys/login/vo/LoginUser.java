package com.hnyr.sys.login.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName: LoginUser
 * @Description: 登录用户数据对象
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
@Data
@Accessors(chain = true)
public class LoginUser {
    private String username;
    private String password;
    private String captcha;
    private String salt;
}
