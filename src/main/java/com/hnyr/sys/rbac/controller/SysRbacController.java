package com.hnyr.sys.rbac.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.hnyr.sys.config.ResponseResult;
import com.hnyr.sys.log.AuditLog;
import com.hnyr.sys.rbac.entity.dto.SysDataPurviewDefineDto;
import com.hnyr.sys.rbac.entity.dto.SysRoleUserDto;
import com.hnyr.sys.rbac.entity.dto.SysUserDataPurviewDto;
import com.hnyr.sys.rbac.entity.dto.SysUserDto;
import com.hnyr.sys.rbac.entity.vo.SysResourceVo;
import com.hnyr.sys.rbac.entity.vo.SysUserVo;
import com.hnyr.sys.rbac.entity.vo.TokenUserVo;
import com.hnyr.sys.rbac.security.AuthPermissions;
import com.hnyr.sys.rbac.security.SecurityConstant;
import com.hnyr.sys.rbac.service.*;
import com.hnyr.sys.utils.AssertUtil;
import com.hnyr.sys.utils.DataConvertor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

@RestController
@Api(tags = "SYS RBAC相关")
@RequestMapping("/api")
@Slf4j
public class SysRbacController {
    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysRoleUserService sysRoleUserService;
    @Resource
    private SysResourceService sysResourceService;
    @Resource
    private SysUserDataPurviewService sysUserDataPurviewService;
    @Resource
    private SysRoleResourceService sysRoleResourceService;
    @Resource
    private SysDataPurviewDefineService sysDataPurviewDefineService;

    @AuditLog
    @ApiOperation("用户修改个人密码")
    @PostMapping("/sys/rbac/personal/change_password")
    public Object changePassword(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu, @RequestBody Map<String, Object> map) {
        String oldPassword = MapUtil.getStr(map, "oldPassword");
        String newPassword = MapUtil.getStr(map, "newPassword");
        SysUserDto dto = sysUserService.getById(tu.getId());
        AssertUtil.isTrue(oldPassword != null, "未输入原密码");
        AssertUtil.isTrue(oldPassword.equals(dto.getPassword()), "输入的原密码错误");
        AssertUtil.isTrue(!newPassword.equals(dto.getPassword()), "新密码不得与当前密码相同");
        // TODO 可完善限制 ：新密码不与之前n次密码相同
        dto.setPassword(newPassword);
        dto.setModifier(tu.getId());
        sysUserService.changePassword(dto);
        return true;
    }

    @AuditLog
    @ApiOperation("用户修改个人初始化密码")
    @PostMapping("/sys/rbac/personal/init_password")
    public Object initPassword(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu, @RequestBody Map<String, Object> map) {
        String newPassword = MapUtil.getStr(map, "newPassword");
        SysUserDto dto = sysUserService.getById(tu.getId());
        AssertUtil.isTrue(!newPassword.equals(dto.getPassword()), "新密码不得与当前密码相同");
        dto.setPassword(newPassword);
        dto.setModifier(tu.getId());
        sysUserService.changePassword(dto);
        return true;
    }

    @AuditLog
    @ApiOperation("管理员重置密码")
    @PostMapping("/sys/rbac/personal/reset_password")
    public Object resetPassword(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu, @RequestBody Map<String, Object> map) {
        Long userId = MapUtil.getLong(map, "userId");
        SysUserDto dto = sysUserService.getById(userId);
        dto.setModifier(tu.getId());
        sysUserService.resetPassword(dto);
        return true;
    }

    @ApiOperation("加载用户基本信息")
    @GetMapping("/sys/rbac/personal/userinfo/load")
    public Object userInfoLoad(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu) {
        SysUserDto userDto = sysUserService.getById(tu.getId());
        return BeanUtil.copyProperties(userDto, SysUserVo.class);
    }

    @AuditLog
    @ApiOperation("用户修改个人信息")
    @PostMapping("/sys/rbac/personal/userinfo/modify")
    public Object userInfoModify(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu, @RequestBody SysUserDto dto) {
        dto.setId(tu.getId());
        dto.setModifier(tu.getId());
        sysUserService.updateUserinfo(dto);
        return true;
    }

    @AuditLog
    @ApiOperation("修改个人头像")
    @PostMapping("/sys/rbac/personal/change/avatar")
    public Object changeAvatar(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu, @RequestBody SysUserDto dto) {
        dto.setId(tu.getId());
        dto.setModifier(tu.getId());
        sysUserService.updateAvatar(dto);
        return dto.getAvatar();
    }

    @AuditLog
    @ApiOperation("角色用户关系绑定")
    @PostMapping("/sys/rbac/role-user/bind")
    @AuthPermissions(value = {"sys.rbac.role", "sys.rbac.grant"})
    public Object updateRoleUser(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu, @RequestBody SysRoleUserDto roleUser) {
        sysRoleUserService.bindByUserAndRoleId(roleUser.getUserId(), roleUser.getRoleId(), roleUser.getEnable());
        return true;
    }

    @AuditLog
    @ApiOperation("批量角色用户关系绑定")
    @PostMapping("/sys/rbac/role-user/bind/batch")
    @AuthPermissions(value = {"sys.rbac.role", "sys.rbac.grant"})
    public Object batchUpdateRoleUser(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu, @RequestBody Map<String, Object> params) {
        List userIds = MapUtil.get(params, "userIds", List.class);
        Long roleId = MapUtil.getLong(params, "roleId");
        Boolean enable = MapUtil.getBool(params, "enable");
        return sysRoleUserService.batchBindRoleUser(tu.getId(), userIds, roleId, enable);
    }

    @ApiOperation("获取用户有权限的角色范围")
    @PostMapping("/sys/rbac/role-user/roles")
    @AuthPermissions(value = "sys.rbac.role")
    public Object getRoles(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu, @RequestBody Map<String, Object> params) {
        return sysRoleUserService.getRolesByUserId(tu.getId());
    }

    @ApiOperation("获取用户有权限的角色范围")
    @PostMapping("/sys/rbac/role-user/roles-purview")
    @AuthPermissions(value = "sys.rbac.grant")
    public Object getRolesPurview(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu) {
        return ResponseResult.success(sysRoleUserService.getRolesByUserIdForGrant(tu.getId()));
    }

    @ApiOperation("获取角色对应的用户")
    @PostMapping("/sys/rbac/role-user/role/users")
    @AuthPermissions(value = {"sys.rbac.role", "sys.rbac.grant.users"})
    public Object getRoleUsers(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu, @RequestBody Map<String, Object> params) {
        Pageable pageable = DataConvertor.parseInSearchMap(params);
        return sysRoleUserService.getRoleUsersByRoleId(pageable, tu.getId(), params);
    }

    @AuditLog
    @ApiOperation("用户快速授权-全部")
    @PostMapping("/sys/rbac/role-user/bis/quick-bind")
    @AuthPermissions(value = {"sys.rbac.role", "sys.rbac.grant.users"})
    public Object bisQuickBind(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu, @RequestBody Map<String, Object> params) {
        Long roleId = MapUtil.getLong(params, "roleId");
        AssertUtil.isTrue(roleId != null, "缺少角色ID");
        String idNumbers = MapUtil.getStr(params, "idNumbers");
        AssertUtil.isTrue(StringUtils.hasText(idNumbers), "未输入工号");
        Set<String> idNumberList = Sets.newHashSet(idNumbers.split("\\n"));
        Map<Long, String> userMap = new HashMap<>();
        Set<String> existIdNumbers = new HashSet<>();
        List<TokenUserVo> us = sysUserService.getByUsernames(idNumberList);
        if (!CollectionUtils.isEmpty(us)) {
            us.forEach(s -> {
                userMap.put(s.getId(), s.getUsername());
                existIdNumbers.add(s.getUsername());
            });
        }
        idNumberList.removeAll(existIdNumbers);
        long notExistIdNumbersCount = idNumberList.size();
        String notExistInNumbers = org.apache.commons.lang3.StringUtils.join(idNumberList, ",");
        Map<String, Object> result = sysRoleUserService.batchBindRoleUser(tu.getId(), Lists.newArrayList(userMap.keySet()), roleId, true);

        result.put("fail", ((Integer) result.get("fail")) + notExistIdNumbersCount);
        String msg = "";
        if (notExistInNumbers.length() > 0) {
            msg += "以下工号不存在用户，请先添加或导入用户信息后授权：" + notExistInNumbers;
        }
        List list = (List) result.get("failIds");
        if (!CollectionUtils.isEmpty(list)) {
            List<String> failIdNumber = new ArrayList<>();
            for (Object o : list) {
                failIdNumber.add(userMap.get(o));
            }
            msg += (StringUtils.hasText(msg) ? "\n" : "") + org.apache.commons.lang3.StringUtils.join(failIdNumber, ",");
        }
        result.put("message", msg);
        return result;
    }

    @ApiOperation("获取用户资源树")
    @GetMapping("/sys/rbac/resource/tree/user")
    @AuthPermissions(value = {"sys.rbac.role"})
    public Object resourceTreeAll(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu, @RequestParam("roleId") Long roleId) {
        Map<String, Object> m = new HashMap<>(2);
        if (tu.getId() == 1) {
            m.put("tree", sysResourceService.getTreeAllResources());
        } else {
            m.put("tree", sysResourceService.getTreeResourcesByUser(tu.getId()));
        }
        m.put("checkedKeys", sysRoleResourceService.getResourceIdsByRoleId(roleId));
        return m;
    }

    @ApiOperation("获取用户资源树 - 全部")
    @PostMapping("/sys/rbac/resource/tree/users")
    @AuthPermissions(value = {"sys.rbac.role"})
    public Object resourceTreeUser(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu, @RequestBody Map<String, Object> map) {
        Long userId = MapUtil.getLong(map, "userId");
        Long roleId = MapUtil.getLong(map, "roleId");
        List<SysResourceVo> o = DataConvertor.listConvert(sysResourceService.getTreeResourcesByUserAndRoleId(userId, roleId), SysResourceVo.class);
        return ResponseResult.success(o);
    }

    @ApiOperation("获取用户资源树 - 用户授权")
    @PostMapping("/sys/rbac/resource/tree/user-purview")
    @AuthPermissions(value = {"sys.rbac.grant"})
    public Object resourceTreeUserGrant(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu, @RequestBody Map<String, Object> map) {
        Long userId = MapUtil.getLong(map, "userId");
        Long roleId = MapUtil.getLong(map, "roleId");
        List<SysResourceVo> o = DataConvertor.listConvert(sysResourceService.getTreeResourcesByUserAndRoleId(userId, roleId), SysResourceVo.class);
        Iterator<SysResourceVo> iterator = o.listIterator();
        while (iterator.hasNext()) {
            SysResourceVo vo = iterator.next();
            if ("sys.rbac.grant".equals(vo.getCode())) {
                iterator.remove();
                break;
            }
        }
        return ResponseResult.success(o);
    }

    @ApiOperation("获取数据权限范围定义")
    @GetMapping("/sys/rbac/data-purview/define")
    @AuthPermissions(value = "sys.rbac.purview")
    public Object getDataPurviewDefine(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu, @RequestParam(required = false) Boolean enable) {
        return sysDataPurviewDefineService.getAll(enable);
    }

    @ApiOperation("获取数据权限范围定义-授权使用")
    @GetMapping("/sys/rbac/data-purview/define-grant")
    @AuthPermissions(value = "sys.rbac.grant")
    public Object getDataPurviewDefineGrant(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu) {
        return sysDataPurviewDefineService.getAllSimple();
    }

    @ApiOperation("获取数据权限范围可选内容")
    @GetMapping("/sys/rbac/data-purview/content")
    public Object getDataPurviewContent(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu, @RequestParam Long id) {
        return ResponseResult.success(sysDataPurviewDefineService.getDataContent(id));
    }

    @AuditLog
    @ApiOperation("保存数据权限范围定义")
    @PostMapping("/sys/rbac/data-purview/define/save")
    @AuthPermissions(value = "sys.rbac.purview")
    public Object getDataPurviewContent(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu, @RequestBody SysDataPurviewDefineDto dto) {
        sysDataPurviewDefineService.save(dto);
        return true;
    }

    @AuditLog
    @ApiOperation("保存用户数据权限范围内容")
    @PostMapping("/sys/rbac/data-purview/save")
    @AuthPermissions(value = "sys.rbac.grant")
    public Object dataPurviewSave(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu, @RequestBody SysUserDataPurviewDto dto) {
        if (CollectionUtils.isEmpty(dto.getIdsList())) {
            dto.setIds("");
        } else {
            dto.setIds(org.apache.commons.lang3.StringUtils.join(dto.getIdsList(), ","));
        }
        sysUserDataPurviewService.save(dto);
        return true;
    }

    @ApiOperation("获取用户数据权限范围内容")
    @PostMapping("/sys/rbac/data-purview/get")
    @AuthPermissions(value = "sys.rbac.grant")
    public Object dataPurview(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu, @RequestBody SysUserDataPurviewDto dto) {
        return ResponseResult.success(sysUserDataPurviewService.getByUserIdAndResourceId(dto.getUserId(), dto.getResourceId()));
    }
}
