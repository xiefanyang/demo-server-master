package com.hnyr.sys.rbac.dao;

import com.hnyr.sys.data.Conditions;
import com.hnyr.sys.rbac.dao.repository.SysDataPurviewDefineRepository;
import com.hnyr.sys.rbac.entity.dto.SysDataPurviewDefineDto;
import com.hnyr.sys.rbac.entity.po.SysDataPurviewDefine;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName: SysDataPurviewDefineDao
 * @Description:
 * @Author: demo
 * @CreateDate: 2023/10/9 17:32
 * @Version: 1.0
 */
@Component
public class SysDataPurviewDefineDao {
    @Resource
    private SysDataPurviewDefineRepository repository;
    @Resource
    private JdbcTemplate jdbcTemplate;

    public List getDataContent(String content) {
        return jdbcTemplate.queryForList(content);
    }

    public SysDataPurviewDefine getById(Long id) {
        return repository.findOne(id);
    }

    public void save(SysDataPurviewDefine po) {
        if (po.getDeleted() == null) {
            po.setDeleted(false);
        }
        repository.save(po);
    }

    public List<SysDataPurviewDefine> getAll(Boolean enable) {
        Conditions conditions = Conditions.empty().and("deleted").is(false);
        if (enable != null) {
            conditions.and("enable").is(enable);
        }
        return repository.findAll(conditions);
    }

    public List<SysDataPurviewDefineDto> getAllSimple() {
        String sql = "select id,name,type from t_sys_data_purview_define where is_deleted = 0 and enable = 1 ";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(SysDataPurviewDefineDto.class));
    }
}
