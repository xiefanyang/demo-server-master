package com.hnyr.weixiudemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.hnyr.weixiudemo.domain.Teacher;
import com.hnyr.weixiudemo.mapper.TeacherMapper;
import com.hnyr.weixiudemo.service.TeacherService;
import org.springframework.stereotype.Service;

/**
* @author 谢凡洋
* @description 针对表【teacher(一般教师信息表)】的数据库操作Service实现
* @createDate 2026-04-20 18:26:38
*/
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher>
    implements TeacherService {

}




