package com.hnyr.xy.branch.service.impl;

import com.google.common.collect.Lists;
import com.hnyr.sys.config.BusinessException;
import com.hnyr.sys.data.entity.BatchResultVo;
import com.hnyr.sys.rbac.service.SysRoleUserService;
import com.hnyr.xy.branch.entity.dto.BranchMemberDto;
import com.hnyr.xy.branch.service.BranchMemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: BranchMemberServiceImpl
 * @Description:
 * @Author: demo
 * @CreateDate: 2023/10/12 15:35
 * @Version: 1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BranchMemberServiceImpl implements BranchMemberService {
    @Resource
    SysRoleUserService sysRoleUserService;

    @Override
    public Page<BranchMemberDto> page(Pageable pageable, Map<String, Object> searchMap) {
        //TODO 仅作为示例 请替换实际编码
        /**********example start***********/
        List<BranchMemberDto> list = new ArrayList<>();
        list.add(new BranchMemberDto(5L, "1111", Boolean.TRUE, 1, "202300001", "测试管理员", "测试院系", "测试专业"));
        list.add(new BranchMemberDto(7L, "1111", Boolean.TRUE, 0, "202300003", "测试老师3", "测试院系", "测试专业"));
        list.add(new BranchMemberDto(6L, "2222", Boolean.TRUE, 0, "202300002", "测试老师2", "测试院系", "测试专业"));
        Page<BranchMemberDto> page = new PageImpl<>(list, pageable, 10);
        /**********example end***********/
        return page;
    }

    @Override
    public void setOrUnSetBranchManager(BranchMemberDto dto) {
        //TODO 本方法仅作为从业务模块设置管理员 加入和移除系统角色的参考，实际可以根据需求自行编写 批量或单个操作授权
        //特定系统角色 硬编码 （正常可以从 用户授权 功能直接界面添加，此处为业务模块中设定，需硬编码）
        String roleCode = "xy_admin.branch_leader";
        //判断是设置 还是 移除 管理员
        if (dto.getLeader() == 1) {
            //添加
            //TODO 需要调用"设置系统角色用户"
            sysRoleUserService.bindRoleUserRelationByRoleCode(Lists.newArrayList(dto.getUserId()), roleCode, Boolean.TRUE);
        } else {
            //移除
            //TODO 需要调用"移除系统角色用户"
            sysRoleUserService.bindRoleUserRelationByRoleCode(Lists.newArrayList(dto.getUserId()), roleCode, Boolean.FALSE);
        }
        //TODO 更新BranchMember

    }

    @Override
    public BatchResultVo batchSaveMember(String branchId, List<Long> members, Boolean enable) {
        BatchResultVo result = new BatchResultVo();
        //TODO 遍历更新，记录 总数、成功、失败、跳过及相关错误信息，例如：
        int test = 100;
        for (int i = 0; i < test; i++) {
            //设置总数计数
            result.setTotal(result.getTotal() + 1);
            if (i % 8 == 0) {
                //模拟跳过计数 根据情况，有些不需要有跳过的情况，只需要成功、失败
                result.setSkip(result.getSkip() + 1);
                result.getSkipMsg().add("xx跳过：因xx ");
            } else if (i % 5 == 0) {
                //模拟失败 可以是判断处理失败；也可以调用其他方法catch exception处理
                try {
                    // do ...
                } catch (Exception e) {
                    result.setError(result.getError() + 1);
                    // 异常可以根据业务异常抛出 instanceof 进行信息包装
                    result.getSkipMsg().add("xx失败： xx" + (e instanceof BusinessException ? e.getMessage() : "未知异常"));
                }
            } else {
                //模拟成功
                result.setOk(result.getOk() + 1);
            }
        }
        return result;
    }
}
