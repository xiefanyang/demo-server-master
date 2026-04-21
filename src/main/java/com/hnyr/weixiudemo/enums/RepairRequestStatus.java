package com.hnyr.weixiudemo.enums;

public enum RepairRequestStatus {
    WAIT_ASSIGN("WAIT_ASSIGN", "待指派"),
    ASSIGNED("ASSIGNED", "已指派"),
    WAIT_RATE("WAIT_RATE", "待评分"),
    FINISHED("FINISHED", "已完成"),
    CANCELLED("CANCELLED", "已取消"),
    REJECTED("REJECTED", "已驳回");

    private String code;
    private String desc;

    RepairRequestStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
