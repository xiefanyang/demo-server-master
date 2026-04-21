package com.hnyr.sys.rbac.service;

import com.hnyr.sys.rbac.entity.dto.SysDataPurviewDefineDto;
import com.hnyr.sys.rbac.entity.po.SysDataPurviewDefine;

import java.util.List;

/**
 * @ClassName: SysDataPurviewDefineService
 * @Description:
 * @Author: demo
 * @CreateDate: 2023/10/9 17:35
 * @Version: 1.0
 */
public interface SysDataPurviewDefineService {
    SysDataPurviewDefine getById(Long id);

    List getDataContent(Long id);

    void save(SysDataPurviewDefineDto dto);

    List<SysDataPurviewDefineDto> getAll(Boolean enable);

    List<SysDataPurviewDefineDto> getAllSimple();
}
