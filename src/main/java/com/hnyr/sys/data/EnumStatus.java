package com.hnyr.sys.data;

/**
 * @ClassName: StatusEnum
 * @Description: 常用状态码
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
public enum EnumStatus {

    //
    STATUS_DRAFT(0, "草稿"),
    STATUS_ENABLE(1, "正常"),
    STATUS_DISABLE(2, "冻结"),
    ;

    private Integer status;
    private String desc;

    EnumStatus(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public Integer getStatus() {
        return status;
    }

    public static String getStatusDesc(Integer value) {
        EnumStatus[] es = values();
        for (EnumStatus e : es) {
            if (e.status.equals(value)) {
                return e.getDesc();
            }
        }
        return null;
    }
}
