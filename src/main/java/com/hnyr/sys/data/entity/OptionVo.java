package com.hnyr.sys.data.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName: OptionVo
 * @Description: 下拉框标准数据对象
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
@Data
@Accessors(chain = true)
public class OptionVo {
    private String label;
    private Object value;
    private Boolean disabled = Boolean.FALSE;
}
