package com.hnyr.sys.data.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: TreeVo
 * @Description: 树形组件标准数据对象
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
@Data
public class TreeVo {
    private String label;
    private Object value;
    private List<TreeVo> children = new ArrayList<>();
    private Boolean open = Boolean.TRUE;
}
