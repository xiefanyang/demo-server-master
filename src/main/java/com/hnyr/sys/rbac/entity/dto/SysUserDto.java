package com.hnyr.sys.rbac.entity.dto;

import com.hnyr.sys.data.entity.BaseBisDto;
import lombok.Data;
import org.springframework.util.StringUtils;

import static com.hnyr.sys.rbac.RbacConstant.LOGO_USER_DEFAULT;

@Data
public class SysUserDto extends BaseBisDto {
    private Long creator;
    private Long modifier = -1L;
    private String name;
    /**
     * 三方账号按照一定规则自动创建
     */
    private String username;
    private String password;
    private String email = "";
    private String mobile = "";
    private String nickname;
    private String idCard = "";
    private String idNumber = "";
    private String avatar = "";
    private String avatarUrl;
    private Integer state = 0;
    private String gender = "";
    private Long updatePasswordTime = -1L;
    private Long orgId;
    private Long id;
    private Integer type;

    // 用户基础信息
    private Long departmentId;
    private Long majorId;
    private Long classId;
    private String grade;

    public String getAvatarUrl() {
        if (StringUtils.hasText(avatar)) {
            return avatar;
        }
        return LOGO_USER_DEFAULT;
    }
}
