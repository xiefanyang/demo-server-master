package com.hnyr.sys.login.vo;

import lombok.Data;
import lombok.experimental.Accessors;
/**
 * @ClassName: OAuth2User
 * @Description:
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
@Data
@Accessors(chain = true)
public class OAuth2User {

    private String name;

    private Integer sex;

    private Integer category;

    private String serialNo;

    private String schoolName;

    private String academyName;

    private String majorName;

}
