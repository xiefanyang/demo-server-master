package com.hnyr.sys.rbac.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.hnyr.sys.config.BusinessException;
import com.hnyr.sys.rbac.dao.SysDataPurviewDefineDao;
import com.hnyr.sys.rbac.entity.dto.SysDataPurviewDefineDto;
import com.hnyr.sys.rbac.entity.po.SysDataPurviewDefine;
import com.hnyr.sys.rbac.service.SysDataPurviewDefineService;
import com.hnyr.sys.utils.AssertUtil;
import com.hnyr.sys.utils.DataConvertor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: SysDataPurviewDefineServiceImpl
 * @Description: TODO java类作用描述
 * @Author: demo
 * @CreateDate: 2023/10/9 17:36
 * @Version: 1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysDataPurviewDefineServiceImpl implements SysDataPurviewDefineService {
    @Resource
    SysDataPurviewDefineDao dataPurviewDefineDao;

    @Override
    public SysDataPurviewDefine getById(Long id) {
        return dataPurviewDefineDao.getById(id);
    }

    @Override
    public List getDataContent(Long id) {
        SysDataPurviewDefine po = getById(id);
        AssertUtil.notNull(po, "未找到指定数据范围定义");
        if (po.getType() == 0) {
            return new ArrayList();
        }
        try {
            return dataPurviewDefineDao.getDataContent(po.getContent());
        } catch (Exception e) {
            throw new BusinessException("执行失败" + e.getMessage());
        }

    }

    @Override
    public void save(SysDataPurviewDefineDto dto) {
        SysDataPurviewDefine po;
        if (dto.getId() != null) {
            if (dto.getDeleted() == true) {
                // TODO 删除需要检查其他关联表（主要是资源与用户权限范围表是否已指定）
            }
            po = dataPurviewDefineDao.getById(dto.getId());
            BeanUtil.copyProperties(dto, po, "id", "createTime", "version");
        } else {
            po = BeanUtil.copyProperties(dto, SysDataPurviewDefine.class, "createTime", "version", "updateTime");
        }
        dataPurviewDefineDao.save(po);
    }

    @Override
    public List<SysDataPurviewDefineDto> getAll(Boolean enable) {
        return DataConvertor.listConvert(dataPurviewDefineDao.getAll(enable), SysDataPurviewDefineDto.class);
    }

    @Override
    public List<SysDataPurviewDefineDto> getAllSimple() {
        return dataPurviewDefineDao.getAllSimple();
    }
}
