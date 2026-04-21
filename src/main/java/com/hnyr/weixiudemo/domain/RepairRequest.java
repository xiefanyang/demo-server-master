package com.hnyr.weixiudemo.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.weixiudemo.enums.RepairRequestStatus;
import com.weixiudemo.enums.ReporterTypes;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 维修记录表
 * @TableName repair_request
 */
@TableName(value ="repair_request")
@Data
public class RepairRequest implements Serializable {
    /**
     * 维修记录主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 报修人ID
     */
    @TableField(value = "reporter_id")
    private Long reporterId;

    /**
     * 上报人类别
     */
    @TableField(value = "reporter_type")
    private ReporterTypes reporterType;

    /**
     * 工单状态
     */
    @TableField(value = "status")
    private RepairRequestStatus status;

    /**
     * 校区ID(关联校区表)
     */
    @TableField(value = "campus_id")
    private Integer campusId;

    /**
     * 院系id(关联院系表)
     */
    @TableField(value = "department_id")
    private Integer departmentId;

    /**
     * 维修类别ID
     */
    @TableField(value = "repair_category_id")
    private Long repairCategoryId;

    /**
     * 故障类别ID
     */
    @TableField(value = "fault_category_id")
    private Long faultCategoryId;

    /**
     * 上报时间
     */
    @TableField(value = "reported_time")
    private Date reportedTime;

    /**
     * 报修说明
     */
    @TableField(value = "description")
    private String description;

    /**
     * 维修地点
     */
    @TableField(value = "location")
    private String location;

    /**
     * 报修图片URL列表(最多三张)
     */
    @TableField(value = "report_image_urls")
    private String reportImageUrls;

    /**
     * 指派人ID（关联user表）
     */
    @TableField(value = "assignee_id")
    private Long assigneeId;

    /**
     * 指派时间
     */
    @TableField(value = "assigned_time")
    private Date assignedTime;

    /**
     * 被指派人ID（关联user表）
     */
    @TableField(value = "handler_id")
    private Long handlerId;

    /**
     * 原指派人ID（关联user表）
     */
    @TableField(value = "original_handler_id")
    private Long originalHandlerId;

    /**
     * 接单时间
     */
    @TableField(value = "handled_time")
    private Date handledTime;

    /**
     * 维修时间
     */
    @TableField(value = "completed_time")
    private Date completedTime;

    /**
     * 接单时长，单位小时
     */
    @TableField(value = "handled_duration")
    private Integer handledDuration;

    /**
     * 维修时长，单位小时
     */
    @TableField(value = "repair_duration")
    private Integer repairDuration;

    /**
     * 维修图片URL列表
     */
    @TableField(value = "repair_image_urls")
    private String repairImageUrls;

    /**
     * 维修内容
     */
    @TableField(value = "complete_description")
    private String completeDescription;

    /**
     * 维修评分
     */
    @TableField(value = "rate_score")
    private Integer rateScore;

    /**
     * 技术评分
     */
    @TableField(value = "rate_score_tech")
    private Integer rateScoreTech;

    /**
     * 态度评分
     */
    @TableField(value = "rate_score_attitude")
    private Integer rateScoreAttitude;

    /**
     * 形象评分
     */
    @TableField(value = "rate_score_image")
    private Integer rateScoreImage;

    /**
     * 自动好评标识
     */
    @TableField(value = "auto_rate_flag")
    private Integer autoRateFlag;

    /**
     * 自动评分时间
     */
    @TableField(value = "auto_rate_time")
    private Date autoRateTime;

    /**
     * 取消时间
     */
    @TableField(value = "cancel_time")
    private Date cancelTime;

    /**
     * 驳回时间
     */
    @TableField(value = "reject_time")
    private Date rejectTime;

    /**
     * 驳回人员ID
     */
    @TableField(value = "reject_user_id")
    private Long rejectUserId;

    /**
     * 驳回原因
     */
    @TableField(value = "reject_reason")
    private String rejectReason;

    /**
     * 逻辑删除标识
     */
    @TableLogic(value = "0")
    @TableField(value = "is_deleted")
    private Integer isDeleted;

    /**
     * 创建时间
     */
    @TableField(value = "created_at")
    private Date createdAt;

    /**
     * 更新时间
     */
    @TableField(value = "updated_at")
    private Date updatedAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}