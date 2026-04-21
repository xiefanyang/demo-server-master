package com.hnyr.sys.rbac.entity.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysUserVo implements Serializable {
    private static final long serialVersionUID = 1L;
    //主键
    private Long id;
    //创建时间
    private Long createTime;
    //更新时间
    private Long updateTime;
    private String recordId;
    private Long creator;
    private Long modifier;
    private String name;
    private String gender;
    //三方账号按照一定规则自动创建
    private String username;
    private String email;
    private String mobile;
    private String nickname;
    private String idNumber;
    private String idCard;
    private String avatar;
    private Integer state;
    private Integer type;
    private Long updatePasswordTime;
    private Boolean enable;
}
