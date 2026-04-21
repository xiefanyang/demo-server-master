package com.hnyr.sys.rbac.controller;

import com.hnyr.sys.config.BusinessException;
import com.hnyr.sys.log.AuditLog;
import com.hnyr.sys.rbac.entity.vo.SysResourceVo;
import com.hnyr.sys.rbac.entity.vo.TokenUserVo;
import com.hnyr.sys.rbac.security.AuthPermissions;
import com.hnyr.sys.rbac.security.SecurityConstant;
import com.hnyr.sys.rbac.service.SysResourceService;
import com.hnyr.sys.rbac.service.SysRoleResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@Api(tags = "资源管理", hidden = true)
@RequestMapping("/api")
public class SysResourceController {
    @Resource
    SysResourceService resourceService;
    @Resource
    SysRoleResourceService sysRoleResourceService;

    /**
     * 资源树获取【全部】
     *
     * @return
     */
    @ApiOperation("资源树获取")
    @PostMapping("/sys/rbac/resource/tree/grid")
    @AuthPermissions(value = "sys.rbac.resource")
    public Object resourceTreeGrid(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu) {
        return resourceService.getTreeAllResources();
    }

    @GetMapping("/sys/rbac/resource/tree/all")
    @AuthPermissions(value = {"sys.rbac.resource", "sys.rbac.role"})
    public Object resourceTreeAll(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu, @RequestParam("roleId") Long roleId) {
        Map<String, Object> m = new HashMap<>(2);
        m.put("tree", resourceService.getTreeAllResources());
        m.put("checkedKeys", sysRoleResourceService.getResourceIdsByRoleId(roleId));
        return m;
    }

    @AuditLog
    @ApiOperation("资源保存")
    @PostMapping("/sys/rbac/resource/save")
    @AuthPermissions(value = "sys.rbac.resource")
    public Object saveResource(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu, @RequestBody SysResourceVo resource) {
        resourceService.save(resource);
        return true;
    }

    @AuditLog
    @ApiOperation("资源移除")
    @GetMapping("/sys/rbac/resource/remove")
    @AuthPermissions(value = "sys.rbac.resource")
    public Object remove(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu, @RequestParam Long id) {
//        resourceService.save(resource);
        throw new BusinessException("暂不支持删除");
//        return true;
    }

}
