package com.hnyr.sys.rbac.service;

import com.hnyr.sys.rbac.entity.dto.SysUserDataPurviewDto;

import java.util.List;

public interface SysUserDataPurviewService {
    SysUserDataPurviewDto getByUserIdAndResourceId(Long userId, Long resourceId);

    SysUserDataPurviewDto getByUserIdAndResourceCode(Long userId, String resourceCode);

    void save(SysUserDataPurviewDto dto);

    /**
     * 获取用户指定资源的数据权限配置
     * 如权限配置类型为默认：则返回空集合，业务中正常处理，忽略数据权限范围
     * 如权限配置类型为自定义：则必须返回指定数据类型的id集合，如空为未配置，抛出未配置异常，中断数据访问
     *
     * @param userId
     * @param resourceCode
     * @return
     */
    List getUserDataPurviewIds(Long userId, String resourceCode);
}
