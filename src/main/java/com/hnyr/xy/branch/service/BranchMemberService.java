package com.hnyr.xy.branch.service;

import com.hnyr.sys.data.entity.BatchResultVo;
import com.hnyr.xy.branch.entity.dto.BranchMemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: BranchMemberService
 * @Description:
 * @Author: demo
 * @CreateDate: 2023/10/12 15:29
 * @Version: 1.0
 */
public interface BranchMemberService {
    /**
     * 获取分会会员
     *
     * @param pageable
     * @param searchMap
     * @return
     */
    Page<BranchMemberDto> page(Pageable pageable, Map<String, Object> searchMap);

    /**
     * 设置取消分会管理员
     *
     * @param dto
     */
    void setOrUnSetBranchManager(BranchMemberDto dto);

    /**
     * 批量添加或移除会员
     *
     * @param branchId 目标分会id
     * @param members  要操作的用户id集合
     * @param enable   true 添加 false 移除
     * @return
     */
    BatchResultVo batchSaveMember(String branchId, List<Long> members, Boolean enable);
}
