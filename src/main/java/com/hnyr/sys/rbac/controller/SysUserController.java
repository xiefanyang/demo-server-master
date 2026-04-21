package com.hnyr.sys.rbac.controller;

import cn.hutool.core.bean.BeanUtil;
import com.hnyr.sys.data.entity.PageVo;
import com.hnyr.sys.log.AuditLog;
import com.hnyr.sys.rbac.entity.dto.SysUserDto;
import com.hnyr.sys.rbac.entity.vo.SysRoleUserVo;
import com.hnyr.sys.rbac.entity.vo.TokenUserVo;
import com.hnyr.sys.rbac.security.AuthPermissions;
import com.hnyr.sys.rbac.security.SecurityConstant;
import com.hnyr.sys.rbac.service.SysUserService;
import com.hnyr.sys.utils.DataConvertor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@Slf4j
@RestController
@Api(tags = "用户管理", hidden = true)
@RequestMapping("/api")
public class SysUserController {
    @Resource
    SysUserService sysUserService;

    /**
     * 分页获取用户
     *
     * @param searchMap
     * @return
     */
    @PostMapping("/sys/rbac/user/page")
    @ApiOperation("分页获取用户")
    @AuditLog
    @AuthPermissions(value = "sys.rbac.user")
    public Object getRoleUserSearch(@RequestBody Map<String, Object> searchMap) {
        Pageable pageable = DataConvertor.parseInSearchMap(searchMap);
        PageVo<SysRoleUserVo> p = sysUserService.page(pageable, searchMap);
        return p;
    }

    @AuditLog
    @ApiOperation("保存用户")
    @PostMapping("/sys/rbac/user/save")
    @AuthPermissions(value = {"sys.rbac.user"})
    public Object addUser(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu, @RequestBody SysUserDto accountDto) {
        Integer type = accountDto.getType();
        if (accountDto.getId() == null) {
            accountDto.setCreator(tu.getId());
            accountDto = sysUserService.add(accountDto, type);
        } else {
            accountDto.setModifier(tu.getId());
            accountDto = sysUserService.modify(accountDto);
        }
        return BeanUtil.copyProperties(accountDto, TokenUserVo.class);
    }


    @AuditLog
    @ApiOperation("修改用户状态")
    @PostMapping("/sys/rbac/user/change/state")
    @AuthPermissions(value = "sys.rbac.user")
    public Object changeState(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu, @RequestBody SysUserDto accountDto) {
        accountDto.setModifier(tu.getId());
        sysUserService.modify(accountDto);
        return true;
    }
}
