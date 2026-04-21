package com.hnyr.xy.branch.controller;

import cn.hutool.core.map.MapUtil;
import com.hnyr.sys.config.ResponseResult;
import com.hnyr.sys.log.AuditLog;
import com.hnyr.sys.rbac.entity.vo.TokenUserVo;
import com.hnyr.sys.rbac.security.AuthPermissions;
import com.hnyr.sys.rbac.security.SecurityConstant;
import com.hnyr.sys.rbac.service.SysUserDataPurviewService;
import com.hnyr.sys.utils.AssertUtil;
import com.hnyr.sys.utils.DataConvertor;
import com.hnyr.xy.branch.entity.dto.BranchMemberDto;
import com.hnyr.xy.branch.service.BranchMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: BranchController
 * @Description: 校友成员
 * @Author: demo
 * @CreateDate: 2023/10/11 17:38
 * @Version: 1.0
 */
@Slf4j
@Api("校友系统 - 校友会成员管理")
@RestController
@RequestMapping("/api")
public class BranchMemberController {
    @Resource
    BranchMemberService branchMemberService;
    @Resource
    SysUserDataPurviewService sysUserDataPurviewService;

    /**
     * 分页获取用户
     *
     * @param tu        访问用户对象
     * @param searchMap 封装传递的检索参数（包含列表分页参数）
     * @return
     */
    @PostMapping("/bis/xy/branch/member/page")
    @ApiOperation("分页获取校友分会成员")
    @AuthPermissions(value = "bis.xy.branch.index.member")
    public ResponseResult page(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu, @RequestBody Map<String, Object> searchMap) {
        String branchId = MapUtil.getStr(searchMap, "branchId");
        AssertUtil.notBlank(branchId, "缺少分会ID");
        //TODO 需要检查是否为分会管理员 。@AuthPermissions(value = "bis.xy.branch.index.member") 仅控制功能权限，不控制数据权限
        /**
         * 判断数据权限有2种方法，根据实际情况进行处理（二选一即可）
         * （1）统一授权模式：业务中不做设置管理员功能，业务表不标记管理员信息，如：分会成员中的leader字段，如不加该字段，甚至管理员不存在于分会成员中，则为"完全系统管理员角色设定模式"，此模式数据权限托管为角色权限管理模块
         *  该模式：应从"用户授权"中对应角色 添加用户，并为该用户设定可管理的数据权限范围 ，如：添加A用户，选定分会管理权限范围（多选）
         *  好处：减少开发量（不需要定制业务表中的管理员设置功能），管理员隔离出业务避免干扰，根据统一配置，自动获取拼装过滤语句
         *
         * （2）业务授权模式：业务中标记管理员信息，如：分会成员中的leader字段，包含该字段，仅加入角色，数据权限为自管模式【当前功能的方式】
         *  该模式：应自行根据当前用户与要访问的数据自行做数据权限的处理
         *  好处：灵活性好，复杂情况可定制，接口隔离性好
         *
         *  数据权限过滤的后端校验作用：（1）未指定数据访问目标，自动过滤设定权限的数据范围（2）指定数据访问目标，判定是否可进行访问（避免相同功能权限，指向爆破越权访问）
         */
        // TODO （二选一） 方法一 示例： bis.xy.branch.index 该资源为管理员设定了 管理指定的分会，返回结果为 对应的分会id集合，可直接比对判定，也可以拿到sql里拼装过滤
//        List purviewIds = sysUserDataPurviewService.getUserDataPurviewIds(tu.getId(), "bis.xy.branch.index");
//        AssertUtil.isTrue(purviewIds.contains(branchId), "您没有维护该分会成员的权限");
        // TODO （二选一）  方法二 示例：
        // TODO 根据当前用户id（tu.getId())从成员表中 读取 user_id = :userId and branch_id = :branchId and enable = 1 and leader = 1 and is_deleted = 0 的数据，确定具有该组的管理权限

        //工具方法解析前台分页参数包装分页对象（注意前后台名称）
        Pageable pageable = DataConvertor.parseInSearchMap(searchMap);
        return ResponseResult.success(branchMemberService.page(pageable, searchMap));
    }

    /**
     * 设置取消管理员
     *
     * @param tu  访问用户对象（如需使用当前用户信息，如id，可get使用）
     * @param dto 保存提交的数据包体
     * @return
     */
    @AuditLog
    @PostMapping("/bis/xy/branch/member/set/manager")
    @ApiOperation("设置取消管理员")
    @AuthPermissions(value = "bis.xy.branch.index.member")
    public ResponseResult setManager(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu, @RequestBody BranchMemberDto dto) {
        //TODO 数据权限判定，参考上面
        branchMemberService.setOrUnSetBranchManager(dto);
        return ResponseResult.success();
    }

    /**
     * 批量添加、冻结分会成员
     *
     * @param tu   访问用户对象（如需使用当前用户信息，如id，可get使用）
     * @param data 保存提交的数据包体
     * @return
     */
    @AuditLog
    @PostMapping("/bis/xy/branch/member/save/batch")
    @ApiOperation("批量添加、冻结分会成员")
    @AuthPermissions(value = "bis.xy.branch.index.member")
    public ResponseResult save(@RequestAttribute(SecurityConstant.TOKEN_USER_KEY) TokenUserVo tu, @RequestBody Map<String, Object> data) {
        //TODO 数据权限判定，参考上面
        String branchId = MapUtil.getStr(data, "branchId");
        AssertUtil.notBlank(branchId, "缺少分会ID");
        List<Long> members = MapUtil.get(data, "members", List.class);
        AssertUtil.notEmpty(data, "未指定成员");
        Boolean enable = MapUtil.getBool(data, "enable");
        AssertUtil.notNull(enable, "未指定enable字段");
        return ResponseResult.success(branchMemberService.batchSaveMember(branchId, members, enable));
    }
}
