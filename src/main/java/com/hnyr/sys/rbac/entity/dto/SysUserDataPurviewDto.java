package com.hnyr.sys.rbac.entity.dto;

import com.google.common.collect.Lists;
import com.hnyr.sys.data.entity.BaseDto;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class SysUserDataPurviewDto extends BaseDto {
    private Long purviewId;
    private String ids;
    private Long userId;
    private Long roleId;
    private Long resourceId;
    private Boolean enable;
    private List<String> idsList;


    public List<String> getIdsList() {
        if (!CollectionUtils.isEmpty(idsList)) {
            return idsList;
        }
        if (StringUtils.isNotBlank(ids)) {
            return Lists.newArrayList(ids.split(","));
        }
        return new ArrayList<>();
    }
}
