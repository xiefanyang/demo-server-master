package com.hnyr.xy.branch.entity.po;

import com.hnyr.sys.data.entity.BaseBisEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @ClassName: BranchUser
 * @Description: 校友分会成员
 * @Author: demo
 * @CreateDate: 2023/10/7 19:11
 * @Version: 1.0
 */
@Data
@Entity
@Table(name = "t_bis_xy_branch_member")
public class BranchMember extends BaseBisEntity {
    @Column(columnDefinition = "bigint comment '成员id'", nullable = false)
    private Long userId;
    @Column(columnDefinition = "varchar(64) comment '分会id' default ''", nullable = false)
    private String branchId;
    @Column(columnDefinition = "bit comment '状态 1正常 0冻结' default 1", nullable = false)
    private Boolean enable;
    @Column(columnDefinition = "smallint comment '成员身份 0成员 1分会管理员' default 0", nullable = false)
    private Integer leader;
}
