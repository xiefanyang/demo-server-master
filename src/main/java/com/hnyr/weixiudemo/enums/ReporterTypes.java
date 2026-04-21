package com.hnyr.weixiudemo.enums;

public enum ReporterTypes {
    STUDENT("STUDENT", "学生"),
    TEACHER("TEACHER", "教师");
    private final String code;
    private final String desc;

    ReporterTypes(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
