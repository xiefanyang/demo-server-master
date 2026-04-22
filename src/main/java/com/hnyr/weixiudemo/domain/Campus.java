package com.hnyr.weixiudemo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 校区表
 * @TableName campus
 */
@TableName(value ="campus")
@Data
public class Campus implements Serializable {
    /**
     * 主键ID（自增，必备）
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 校区名称
     */
    @TableField(value = "campus_name")
    private String campusName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
