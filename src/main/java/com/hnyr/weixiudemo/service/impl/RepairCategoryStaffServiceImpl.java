package com.hnyr.weixiudemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hnyr.weixiudemo.domain.RepairCategoryStaff;
import com.hnyr.weixiudemo.mapper.RepairCategoryStaffMapper;
import com.hnyr.weixiudemo.service.RepairCategoryStaffService;
import org.springframework.stereotype.Service;

/**
* @author 谢凡洋
* @description 针对表【repair_category_staff(维修类别与工作人员关联表（多对多）)】的数据库操作Service实现
* @createDate 2026-04-20 19:21:20
*/
@Service
public class RepairCategoryStaffServiceImpl extends ServiceImpl<RepairCategoryStaffMapper, RepairCategoryStaff>
    implements RepairCategoryStaffService{

}




