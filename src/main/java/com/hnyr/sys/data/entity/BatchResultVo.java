package com.hnyr.sys.data.entity;

import lombok.Data;

import java.util.List;

/**
 * @ClassName: BatchResultVo
 * @Description: 批量处理包装
 * @Author: demo
 * @CreateDate: 2023/10/8 15:50
 * @Version: 1.0
 */
@Data
public class BatchResultVo {
    /**
     * 处理总数量
     */
    private Integer total = 0;
    /**
     * 成功数量
     */
    private Integer ok = 0;
    /**
     * 失败数量
     */
    private Integer error = 0;
    /**
     * 跳过数量
     */
    private Integer skip = 0;
    /**
     * 失败汇总信息（根据需要使用）
     */
    private String errorMsgStr;
    /**
     * 跳过汇总信息（根据需要使用）
     */
    private String skipMsgStr;
    /**
     * 失败明细信息（根据需要使用）
     */
    private List<String> errorMsg;
    /**
     * 跳过明细信息（根据需要使用）
     */
    private List<String> skipMsg;
}
