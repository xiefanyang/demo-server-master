package com.hnyr.sys.rbac.service;

import com.hnyr.sys.data.entity.PageVo;
import com.hnyr.sys.rbac.entity.dto.SysUserDto;
import com.hnyr.sys.rbac.entity.po.SysUser;
import com.hnyr.sys.rbac.entity.vo.SysRoleUserVo;
import com.hnyr.sys.rbac.entity.vo.TokenUserVo;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SysUserService {

    PageVo<SysRoleUserVo> page(Pageable pageable, Map<String, Object> searchMap);

    SysUserDto getById(Long id);

    SysUserDto getByUsername(String username);

    SysUserDto add(SysUserDto sysUserDto, Integer type);

    SysUserDto modify(SysUserDto sysUserDto);


    /**
     * 修改密码【仅修改密码相关】
     *
     * @param user
     */
    void changePassword(SysUserDto user);

    /**
     * 重置用户密码
     *
     * @param user
     * @return
     */
    void resetPassword(SysUserDto user);

    /**
     * 修改用户信息【不修改密码】
     *
     * @param user
     */
    void updateUserinfo(SysUserDto user);

    void updateAvatar(SysUserDto user);

    /**
     * 根据Ids获取用户
     *
     * @param userIds
     * @return
     */
    List<SysUser> getUserListInUserIds(Set<Long> userIds);

    List<TokenUserVo> getByUsernames(Set<String> usernames);


}
