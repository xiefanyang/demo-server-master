package com.hnyr.weixiudemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hnyr.weixiudemo.domain.RepairCategoryFault;
import com.hnyr.weixiudemo.mapper.RepairCategoryFaultMapper;
import com.hnyr.weixiudemo.service.RepairCategoryFaultService;
import org.springframework.stereotype.Service;

/**
* @author 谢凡洋
* @description 针对表【repair_category_fault(维修类别与故障类别关联表)】的数据库操作Service实现
* @createDate 2026-04-20 19:21:20
*/
@Service
public class RepairCategoryFaultServiceImpl extends ServiceImpl<RepairCategoryFaultMapper, RepairCategoryFault>
    implements RepairCategoryFaultService{

}




