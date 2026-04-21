package com.hnyr.sys.login.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.experimental.Accessors;
/**
 * @ClassName: OAuth2Token
 * @Description:
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
@Data
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OAuth2Token {
    private String accessToken;

    private String tokenType;

    private Integer expiresIn;

    private String scope;

    private String jti;
}
