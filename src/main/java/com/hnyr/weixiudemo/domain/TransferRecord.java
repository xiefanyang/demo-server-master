package com.hnyr.weixiudemo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 移交记录表
 * @TableName transfer_record
 */
@TableName(value ="transfer_record")
@Data
public class TransferRecord implements Serializable {
    /**
     * 移交记录主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 移交来源用户ID
     */
    @TableField(value = "from_user_id")
    private Long fromUserId;

    /**
     * 移交目标用户ID
     */
    @TableField(value = "to_user_id")
    private Long toUserId;

    /**
     * 发起人ID
     */
    @TableField(value = "created_by")
    private Long createdBy;

    /**
     * 创建时间
     */
    @TableField(value = "created_at")
    private Date createdAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}