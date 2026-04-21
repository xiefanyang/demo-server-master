package com.hnyr.sys.rbac.controller;

import com.hnyr.sys.log.AuditLog;
import com.hnyr.sys.rbac.entity.po.SysRoleUser;
import com.hnyr.sys.rbac.entity.vo.SysRoleResourceVo;
import com.hnyr.sys.rbac.entity.vo.SysRoleVo;
import com.hnyr.sys.rbac.entity.vo.TokenUserVo;
import com.hnyr.sys.rbac.security.AuthPermissions;
import com.hnyr.sys.rbac.security.SecurityConstant;
import com.hnyr.sys.rbac.service.SysRoleResourceService;
import com.hnyr.sys.rbac.service.SysRoleService;
import com.hnyr.sys.rbac.service.SysRoleUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@Api(tags = "角色管理", hidden = true)
@RequestMapping("/api")
public class SysRoleController {
    @Resource
    SysRoleService sysRoleService;
    @Resource
    SysRoleResourceService sysRoleResourceService;
    @Resource
    SysRoleUserService sysRoleUserService;

    @PostMapping("/sys/rbac/role/get")
    @ApiOperation("获取角色信息")
    @AuthPermissions(value = {"sys.rbac.role"})
    public Object roleDetail(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu, @RequestParam Long roleId) {
        return sysRoleService.getRole(roleId);
    }

    @AuditLog
    @ApiOperation("角色保存")
    @PostMapping("/sys/rbac/role/save")
    @AuthPermissions(value = "sys.rbac.role")
    public Object saveResource(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu, @RequestBody SysRoleVo resource) {
        sysRoleService.save(resource);
        return true;
    }

    @AuditLog
    @ApiOperation("角色移除")
    @GetMapping("/sys/rbac/role/remove")
    @AuthPermissions(value = "sys.rbac.role")
    public Object remove(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu, @RequestParam Long id) {
        sysRoleService.remove(id);
        return true;
    }

    @AuditLog
    @ApiOperation("资源角色绑定")
    @PostMapping("/sys/rbac/role-resource/grant")
    @AuthPermissions(value = "sys.rbac.role")
    public Object grantRoleResource(@RequestBody SysRoleResourceVo vo) {
        return sysRoleResourceService.changeGrant(vo.getResourceId(), vo.getRoleId(), vo.getEnable());
    }

    @AuditLog
    @ApiOperation("用户角色绑定")
    @PostMapping("/sys/rbac/role-user/grant")
    @AuthPermissions(value = {"sys.rbac.role", "sys.rbac.user.bind_role"})
    public Object grantRoleUser(@RequestBody SysRoleUser vo) {
        return sysRoleUserService.changeGrant(vo.getUserId(), vo.getRoleId(), vo.getEnable());
    }
}
