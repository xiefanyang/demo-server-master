package com.hnyr.xy.branch.entity.po;

import com.hnyr.sys.data.entity.BaseBisEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @ClassName: Branch
 * @Description: 校友会
 * @Author: demo
 * @CreateDate: 2023/10/7 19:11
 * @Version: 1.0
 */
@Data
@Entity
@Table(name = "t_bis_xy_branch")
public class Branch extends BaseBisEntity {
    /****************************************
     * 说明（参考mysql规范）：
     * 1、属于业务数据记录，数据id不建议使用数据库自增id，集成BaseBisEntity；固定结构、字典、配置型可以数据库自增id，继承BaseEntity。所有entity必须继承二者之一
     * 2、请根据需要在@Table中定义index和unique约束，尽量不超过5个
     * 3、@Column中明确指定字段类型长度，comment，并全部设置nullable = false
     * 4、尽量避免设定外键，关联操作选择通过在service一个事务中进行程序控制（参考mysql建议）
     * 5、所有数据如无必要，均需要采用假删除（is_deleted字段设置为1），各检索条件均需添加该字段条件判断数据有效
     *****************************************/
    @Column(columnDefinition = "varchar(100) comment '名称' default ''", unique = true, nullable = false)
    private String name;
    @Column(columnDefinition = "bit comment '状态 1正常 0冻结' default 1", nullable = false)
    private Boolean enable;
    @Column(columnDefinition = "bigint comment '父级id，总会为0' default 1", nullable = false)
    private Long parentId;
    @Column(columnDefinition = "int comment '排序' default 1", nullable = false)
    private Integer sort;
    @Column(columnDefinition = "varchar(64) comment '会标' default ''", nullable = false)
    private String logo;
}
