package com.hnyr.sys.rbac.controller;

import cn.hutool.crypto.digest.DigestUtil;
import com.hnyr.sys.log.AuditLog;
import com.hnyr.sys.rbac.entity.dto.SysUserDto;
import com.hnyr.sys.rbac.service.SysRoleUserService;
import com.hnyr.sys.rbac.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@RestController
@Api(tags = "用户管理", hidden = true)
@RequestMapping("/api/open")
public class OpenUserController {
    @Resource
    SysUserService sysUserService;

    @Resource
    SysRoleUserService sysRoleUserService;


    private static final String SECRET = "ZG1sZWFybg";

    private boolean checkSignature(SysUserDto accountDto, String sign) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", accountDto.getName());
        params.put("username", accountDto.getUsername());
        params.put("type", accountDto.getType());
        params.put("departmentId", accountDto.getDepartmentId());
        params.put("gender", accountDto.getGender());
        params.put("secret", SECRET);
        // 将除sign参数之外的参数名称放到List中
        List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);
        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            sb.append(params.get(key));
        }
        // 加密的数据
        String sha1Hex = DigestUtil.sha1Hex(sb.toString());
        log.info("参数签名：" + sha1Hex);
        // 判断是否相等
        return sign.equals(sha1Hex);
    }


    @AuditLog
    @ApiOperation("保存用户")
    @PostMapping("/sys/user/save")
    public Object addUser(@RequestParam String sign, @RequestBody SysUserDto accountDto) {
        if (!checkSignature(accountDto, sign)) {
            return "FAIL";
        }
        // 判断用户是否存在
        SysUserDto userDto = sysUserService.getByUsername(accountDto.getUsername());
        if (Objects.nonNull(userDto)) {
            accountDto.setId(userDto.getId());
        }
        Long uid = 1L;
        Integer type = accountDto.getType();
        if (accountDto.getId() == null) {
            accountDto.setCreator(uid);
            sysUserService.add(accountDto, type);
        } else {
            accountDto.setModifier(uid);
            sysUserService.modify(accountDto);
        }
        return "OK";
    }


}
