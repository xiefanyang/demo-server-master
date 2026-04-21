package com.hnyr.xy.branch.entity.dto;

import com.hnyr.sys.data.entity.BaseDto;
import lombok.Data;

/**
 * @ClassName: BranchMemberDto
 * @Description:
 * @Author: demo
 * @CreateDate: 2023/10/10 15:16
 * @Version: 1.0
 */
@Data
public class BranchMemberDto extends BaseDto {
    /**
     * TODO 以下内容仅作为示例，相关字段需要根据表设计字段，补充
     */
    private Long userId;
    private String branchId;
    private Boolean enable = Boolean.TRUE;
    private Integer leader = 0;

    /**
     * 相关检索组装字段
     */
    private String uname;
    private String name;
    private String department;
    private String major;

    public BranchMemberDto(Long userId, String branchId, Boolean enable, Integer leader, String uname, String name, String department, String major) {
        this.userId = userId;
        this.branchId = branchId;
        this.enable = enable;
        this.leader = leader;
        this.uname = uname;
        this.name = name;
        this.department = department;
        this.major = major;
    }
}
