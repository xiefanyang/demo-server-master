package com.hnyr.sys.rbac.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class SysRoleUserVo extends SysUserVo {
    private List<Long> roleIds;
}
