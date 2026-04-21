package com.hnyr.xy.branch.entity.dto;

import com.hnyr.sys.data.entity.BaseBisDto;
import com.hnyr.sys.file.entity.dto.FileDto;
import lombok.Data;

/**
 * @ClassName: BranchDto
 * @Description: 校友会
 * @Author: demo
 * @CreateDate: 2023/10/7 19:11
 * @Version: 1.0
 */
@Data
public class BranchDto extends BaseBisDto {
    /****************************************
     * 命名为：entityDto，继承对照PO：BaseBisDto含recordId，BaseDto不含recordId
     *****************************************/
    private String name;
    private Boolean enable = Boolean.TRUE;
    private Long parentId;
    private Integer sort;
    private String logo;
    private FileDto logoFile;
}
