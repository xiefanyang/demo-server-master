package com.hnyr.sys.rbac.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: TokenUserVo
 * @Description: 登录态用户信息
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
@Data
@Accessors(chain = true)
public class TokenUserVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    //    private String idNumber;
    private String gender;
    private String username;
    private String nickname;
    private String avatar;
    private Long updatePasswordTime;
    private String updatePasswordTimeStr;
    private Long creator;
    private Long modifier = -1L;
    private String token;
    private List<String> res;

    public String getUpdatePasswordTimeStr() {
        if (updatePasswordTime != null && updatePasswordTime > 0) {
            return new DateTime(updatePasswordTime).toString("yyyy-MM-dd HH:mm");
        }
        return "";
    }

//    public String getAvatar() {
//        if (StringUtils.hasText(avatar) && !StringUtils.startsWithIgnoreCase(avatar, "data:image")) {
//            if (avatar.contains("/sys/file")) {
//                return avatar;
//            }
//            return FileUrl.toImageUrl(avatar);
//        } else {
//            return LOGO_USER_DEFAULT;
//        }
//    }
}
