package com.hnyr.weixiudemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.hnyr.weixiudemo.domain.Staff;
import com.hnyr.weixiudemo.mapper.StaffMapper;
import com.hnyr.weixiudemo.service.StaffService;
import org.springframework.stereotype.Service;

/**
* @author 谢凡洋
* @description 针对表【staff(工作人员表)】的数据库操作Service实现
* @createDate 2026-04-20 18:26:38
*/
@Service
public class StaffServiceImpl extends ServiceImpl<StaffMapper, Staff>
    implements StaffService {

}




