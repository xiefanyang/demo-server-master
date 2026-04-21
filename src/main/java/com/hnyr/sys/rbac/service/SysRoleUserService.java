package com.hnyr.sys.rbac.service;

import com.hnyr.sys.rbac.entity.dto.SysRoleDto;
import com.hnyr.sys.rbac.entity.dto.SysRoleUserDto;
import com.hnyr.sys.rbac.entity.po.SysRoleUser;
import com.hnyr.sys.rbac.vo.SysRoleUserVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SysRoleUserService {

    /**
     * 仅绑定角色用户关系 不更新用户信息
     *
     * @param userIds  用户id集合
     * @param roleCode 角色代码
     * @param enable   true 添加 false 冻结
     */
    void bindRoleUserRelationByRoleCode(List<Long> userIds, String roleCode, Boolean enable);

    /**
     * 仅绑定角色用户关系 不更新用户信息
     *
     * @param userIds 用户id集合
     * @param roleId  角色id
     * @param enable  true 添加 false 冻结
     * @return
     */
    void bindRoleUserRelationByRoleId(List<Long> userIds, Long roleId, Boolean enable);

    List<String> getRoleCodeByUserId(Long userId);

    List<SysRoleDto> getRoleListByRoleCodes(Set<String> roleCodes);

    Boolean changeGrant(Long userId, Long roleId, Boolean enable);

    List<Long> getUserIdsByRoleId(Long roleId);

    List<Long> getRoleIdsByUserId(Long userId);

    /**
     * 或者指定用户ID和角色ID的用户
     *
     * @param userId
     * @param roleId
     * @return
     */
    SysRoleUser get(Long userId, Long roleId);

    /**
     * 用户授权
     *
     * @param userId
     * @param roleId
     * @param enable
     * @return
     */
    SysRoleUser bindByUserAndRoleId(Long userId, Long roleId, Boolean enable);

    /**
     * 批量用户授权
     *
     * @param uid     当前操作人
     * @param userIds
     * @param roleId
     * @param enable
     * @return
     */
    Map<String, Object> batchBindRoleUser(Long uid, List userIds, Long roleId, Boolean enable);

    /**
     * 用户获取可授权角色范围
     *
     * @param userId 当前用户
     * @return
     */
    List<SysRoleDto> getRolesByUserId(Long userId);

    List<SysRoleDto> getRolesByUserIdForGrant(Long userId);

    /**
     * 根据角色获取已授权的用户
     *
     * @param pageable
     * @param searchMap
     * @return
     */
    Page<SysRoleUserVo> getRoleUsersByRoleId(Pageable pageable, Long userId, Map<String, Object> searchMap);

}
