package com.hnyr.weixiudemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hnyr.weixiudemo.domain.Student;
import com.hnyr.weixiudemo.mapper.StudentMapper;
import com.hnyr.weixiudemo.service.StudentService;
import org.springframework.stereotype.Service;

/**
* @author 谢凡洋
* @description 针对表【student(学生信息表)】的数据库操作Service实现
* @createDate 2026-04-20 18:26:38
*/
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student>
    implements StudentService {

}




