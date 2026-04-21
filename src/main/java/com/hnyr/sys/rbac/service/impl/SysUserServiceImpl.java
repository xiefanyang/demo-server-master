package com.hnyr.sys.rbac.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.hnyr.sys.data.entity.PageVo;
import com.hnyr.sys.rbac.dao.SysUserDao;
import com.hnyr.sys.rbac.entity.dto.SysUserDto;
import com.hnyr.sys.rbac.entity.po.SysUser;
import com.hnyr.sys.rbac.entity.vo.SysRoleUserVo;
import com.hnyr.sys.rbac.entity.vo.SysUserVo;
import com.hnyr.sys.rbac.entity.vo.TokenUserVo;
import com.hnyr.sys.rbac.service.SysUserService;
import com.hnyr.sys.utils.AssertUtil;
import com.hnyr.sys.utils.DataConvertor;
import com.hnyr.sys.utils.SmUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class SysUserServiceImpl implements SysUserService {
    @Resource
    SysUserDao sysUserDao;

    @Override
    public PageVo<SysRoleUserVo> page(Pageable pageable, Map<String, Object> searchMap) {
        Page<SysUserDto> page =
                DataConvertor.pageConvert(sysUserDao.page(pageable, searchMap), pageable, SysUserDto.class);
        List<SysUserVo> vos1 = DataConvertor.listConvert(page.getContent(), SysUserVo.class);
        PageVo<SysUserVo> accountVos =
                new PageVo<SysUserVo>(vos1, page.getTotalElements(), pageable.getPageNumber(), pageable.getPageSize());

        List<Long> userIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(accountVos.getContent())) {
            for (SysUserVo a : accountVos.getContent()) {
                userIds.add(a.getId());
            }
            List<Map<String, Object>> vos = sysUserDao.getSysRoleUsersInUserIds(userIds);
            List<SysRoleUserVo> urs = DataConvertor.listConvert(accountVos.getContent(), SysRoleUserVo.class);
            if (!CollectionUtils.isEmpty(vos)) {
                for (SysRoleUserVo vo : urs) {
                    List<Long> roles = new ArrayList<>();
                    try {
                        vos.stream().filter(s -> vo.getId().toString().equals(s.get("id").toString())).forEach(s -> {
                            roles.add(Long.valueOf(s.get("roleId").toString()));
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    vo.setRoleIds(roles);
                }
            }
            return new PageVo<SysRoleUserVo>(urs, accountVos.getTotalElements(), pageable.getPageNumber(),
                    pageable.getPageSize());
        }
        return new PageVo<SysRoleUserVo>(new ArrayList<>(), 0, pageable.getPageNumber(), pageable.getPageSize());
    }

    @Override
    public SysUserDto getById(Long id) {
        SysUser sysUser = sysUserDao.getById(id);
        return BeanUtil.copyProperties(sysUser, SysUserDto.class);
    }

    @Override
    public SysUserDto getByUsername(String username) {
        SysUser po = sysUserDao.getByUsername(username, null);
        SysUserDto dto = new SysUserDto();
        BeanUtil.copyProperties(po, dto);
        return dto;
    }


    @Override
    public SysUserDto add(SysUserDto dto, Integer type) {
        if (Objects.nonNull(type)) {
            dto.setType(type);
        }
        dto.setPassword(getDefaultPassword(dto));
        SysUser po = sysUserDao.add(dto);
        BeanUtil.copyProperties(po, dto);
        return dto;
    }

    private String getDefaultPassword(SysUserDto dto) {
        AssertUtil.isTrue(dto.getUsername().length() >= 6, "用户名至少6位");
        return StringUtils.right(dto.getUsername(), 6);
    }

    @Override
    public SysUserDto modify(SysUserDto dto) {
        SysUser po = sysUserDao.modify(dto);
        BeanUtil.copyProperties(po, dto);
        return dto;
    }


    @Override
    public void changePassword(SysUserDto user) {
        SysUser po = sysUserDao.getById(user.getId());
        po.setPassword(user.getPassword());
        po.setModifier(user.getModifier());
        po.setUpdatePasswordTime(DateTime.now().getMillis());
        sysUserDao.update(po);
    }

    @Override
    public void updateUserinfo(SysUserDto user) {
        SysUser po = sysUserDao.getById(user.getId());
        sysUserDao.update(po);
    }

    @Override
    public void updateAvatar(SysUserDto user) {
        SysUser po = sysUserDao.getById(user.getId());
        po.setModifier(user.getModifier());
        po.setAvatar(user.getAvatar());
        sysUserDao.update(po);
    }


    @Override
    public void resetPassword(SysUserDto user) {
        if (user.getId() != null) {
            SysUser po = sysUserDao.getById(user.getId());
            if (po != null) {
                //重置密码
                String password = getDefaultPasswordByLastLength(po.getUsername(), 6);
                //国密sm3加密password （相当于md5）
                po.setPassword(SmUtils.sm3Enc(password));
                po.setUpdatePasswordTime(0L);
                po.setModifier(user.getModifier());
                if (po.getAvatar() == null) {
                    po.setAvatar("");
                }
                sysUserDao.save(po);
            }
        }
    }

    private String getDefaultPasswordByLastLength(String str, int length) {
        AssertUtil.isTrue(str.length() >= length, "字符长度小于" + length);
        return StringUtils.right(str, 6);
    }

    @Override
    public List<SysUser> getUserListInUserIds(Set<Long> userIds) {
        return null;
    }

    @Override
    public List<TokenUserVo> getByUsernames(Set<String> usernames) {
        return DataConvertor.listConvert(sysUserDao.getByUsernames(usernames), TokenUserVo.class);
    }
}
