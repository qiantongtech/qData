/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 * You may use qData for commercial purposes, but you may not remove, hide,
 * modify, or replace the qData logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qData as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qdata.module.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.constant.UserConstants;
import tech.qiantong.qdata.common.core.domain.entity.SysRole;
import tech.qiantong.qdata.common.core.domain.entity.SysUser;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.SecurityUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.spring.SpringUtils;
import tech.qiantong.qdata.module.system.domain.SysRoleDept;
import tech.qiantong.qdata.module.system.domain.SysRoleMenu;
import tech.qiantong.qdata.module.system.domain.SysUserRole;
import tech.qiantong.qdata.module.system.mapper.SysRoleDeptMapper;
import tech.qiantong.qdata.module.system.mapper.SysRoleMapper;
import tech.qiantong.qdata.module.system.mapper.SysRoleMenuMapper;
import tech.qiantong.qdata.module.system.mapper.SysUserRoleMapper;
import tech.qiantong.qdata.module.system.service.ISysRoleService;

import java.util.*;

/**
 * Role Business Layer Processing
 *
 * @author qdata
 */
@Service
public class SysRoleServiceImpl implements ISysRoleService
{
    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysRoleMenuMapper roleMenuMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private SysRoleDeptMapper roleDeptMapper;

    /**
     * Query role data with pagination based on conditions
     *
     * @param role Role information
     * @return Role data collection information
     */
    @Override
//    @DataScope(deptAlias = "d")
    public List<SysRole> selectRoleList(SysRole role)
    {
        List<SysRole> sysRoleList = roleMapper.selectRoleList(role);
        return sysRoleList;
    }

    /**
     * Query roles by user ID
     *
     * @param userId User ID
     * @return Role list
     */
    @Override
    public List<SysRole> selectRolesByUserId(Long userId)
    {
        List<SysRole> userRoles = roleMapper.selectRolePermissionByUserId(userId);
        List<SysRole> roles = selectRoleAll();
        for (SysRole role : roles)
        {
            for (SysRole userRole : userRoles)
            {
                if (role.getRoleId().longValue() == userRole.getRoleId().longValue())
                {
                    role.setFlag(true);
                    break;
                }
            }
        }
        return roles;
    }

    /**
     * Query permissions by user ID
     *
     * @param userId User ID
     * @return Permission list
     */
    @Override
    public Set<String> selectRolePermissionByUserId(Long userId)
    {
        List<SysRole> perms = roleMapper.selectRolePermissionByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (SysRole perm : perms)
        {
            if (StringUtils.isNotNull(perm))
            {
                permsSet.addAll(Arrays.asList(perm.getRoleKey().trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * Query all roles
     *
     * @return Role list
     */
    @Override
    public List<SysRole> selectRoleAll()
    {
        return SpringUtils.getAopProxy(this).selectRoleList(new SysRole());
    }

    /**
     * Get role selection list by user ID
     *
     * @param userId User ID
     * @return Selected role ID list
     */
    @Override
    public List<Long> selectRoleListByUserId(Long userId)
    {
        return roleMapper.selectRoleListByUserId(userId);
    }

    /**
     * Query role by role ID
     *
     * @param roleId Role ID
     * @return Role object information
     */
    @Override
    public SysRole selectRoleById(Long roleId)
    {
        return roleMapper.selectRoleById(roleId);
    }

    /**
     * Check if role name is unique
     *
     * @param role Role information
     * @return True if unique, False otherwise
     */
    @Override
    public boolean checkRoleNameUnique(SysRole role)
    {
        Long roleId = StringUtils.isNull(role.getRoleId()) ? -1L : role.getRoleId();
        SysRole info = new SysRole();
        if (role.getProjectId() != null){
            info = roleMapper.checkRoleNameUniqueAndProjectId(role);
        }else {
            info = roleMapper.checkRoleNameUnique(role.getRoleName());
        }
        if (StringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * Check if role permission key is unique
     *
     * @param role Role information
     * @return True if unique, False otherwise
     */
    @Override
    public boolean checkRoleKeyUnique(SysRole role)
    {
        Long roleId = StringUtils.isNull(role.getRoleId()) ? -1L : role.getRoleId();
        SysRole info = new SysRole();
        if (role.getProjectId() != null){
            info = roleMapper.checkRoleKeyUniqueAndProjectId(role);
        }else {
            info = roleMapper.checkRoleKeyUnique(role.getRoleKey());
        }
        if (StringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * Check if role is allowed to be operated
     *
     * @param role Role information
     */
    @Override
    public void checkRoleAllowed(SysRole role)
    {
        if (StringUtils.isNotNull(role.getRoleId()) && role.isAdmin())
        {
            throw new ServiceException("Operation on super admin role is not allowed");
        }
    }

    /**
     * Check if role has data permission
     *
     * @param roleIds Role IDs
     */
    @Override
    public void checkRoleDataScope(Long... roleIds)
    {
        if (!SysUser.isAdmin(SecurityUtils.getUserId()))
        {
            for (Long roleId : roleIds)
            {
                SysRole role = new SysRole();
                role.setRoleId(roleId);
                List<SysRole> roles = SpringUtils.getAopProxy(this).selectRoleList(role);
                if (StringUtils.isEmpty(roles))
                {
                    throw new ServiceException("No permission to access role data!");
                }
            }
        }
    }

    /**
     * Query role usage count by role ID
     *
     * @param roleId Role ID
     * @return Count
     */
    @Override
    public int countUserRoleByRoleId(Long roleId)
    {
        return userRoleMapper.countUserRoleByRoleId(roleId);
    }

    /**
     * Insert role information
     *
     * @param role Role information
     * @return Result
     */
    @Override
    @Transactional
    public int insertRole(SysRole role)
    {
        // Insert role information
        roleMapper.insertRole(role);
        return insertRoleMenu(role);
    }

    /**
     * Update role information
     *
     * @param role Role information
     * @return Result
     */
    @Override
    @Transactional
    public int updateRole(SysRole role)
    {
        // Update role information
        roleMapper.updateRole(role);
        // Delete role-menu association
        roleMenuMapper.deleteRoleMenuByRoleId(role.getRoleId());
        return insertRoleMenu(role);
    }

    /**
     * Update role status
     *
     * @param role Role information
     * @return Result
     */
    @Override
    public int updateRoleStatus(SysRole role)
    {
        return roleMapper.updateRole(role);
    }

    /**
     * Update data permission information
     *
     * @param role Role information
     * @return Result
     */
    @Override
    @Transactional
    public int authDataScope(SysRole role)
    {
        // Update role information
        roleMapper.updateRole(role);
        // Delete role-dept association
        roleDeptMapper.deleteRoleDeptByRoleId(role.getRoleId());
        // Insert role-dept association (data permission)
        return insertRoleDept(role);
    }

    /**
     * Insert role-menu information
     *
     * @param role Role object
     */
    public int insertRoleMenu(SysRole role)
    {
        int rows = 1;
        // Insert user-role management
        List<SysRoleMenu> list = new ArrayList<SysRoleMenu>();
        for (Long menuId : role.getMenuIds())
        {
            SysRoleMenu rm = new SysRoleMenu();
            rm.setRoleId(role.getRoleId());
            rm.setProjectId(role.getProjectId());
            rm.setMenuId(menuId);
            list.add(rm);
        }
        if (list.size() > 0)
        {
            if (role.getProjectId() != null){
                rows = roleMenuMapper.batchRoleMenuProjectId(list);
            }else {
                rows = roleMenuMapper.batchRoleMenu(list);
            }
        }
        return rows;
    }

    /**
     * Insert role-dept information (data permission)
     *
     * @param role Role object
     */
    public int insertRoleDept(SysRole role)
    {
        int rows = 1;
        // Insert role-dept (data permission) management
        List<SysRoleDept> list = new ArrayList<SysRoleDept>();
        for (Long deptId : role.getDeptIds())
        {
            SysRoleDept rd = new SysRoleDept();
            rd.setRoleId(role.getRoleId());
            rd.setDeptId(deptId);
            list.add(rd);
        }
        if (list.size() > 0)
        {
            rows = roleDeptMapper.batchRoleDept(list);
        }
        return rows;
    }

    /**
     * Delete role by role ID
     *
     * @param roleId Role ID
     * @return Result
     */
    @Override
    @Transactional
    public int deleteRoleById(Long roleId)
    {
        // Delete role-menu association
        roleMenuMapper.deleteRoleMenuByRoleId(roleId);
        // Delete role-dept association
        roleDeptMapper.deleteRoleDeptByRoleId(roleId);
        return roleMapper.deleteRoleById(roleId);
    }

    /**
     * Batch delete role information
     *
     * @param roleIds Role IDs to delete
     * @return Result
     */
    @Override
    @Transactional
    public int deleteRoleByIds(Long[] roleIds)
    {
        for (Long roleId : roleIds)
        {
            checkRoleAllowed(new SysRole(roleId));
            checkRoleDataScope(roleId);
            SysRole role = selectRoleById(roleId);
            if (countUserRoleByRoleId(roleId) > 0)
            {
                throw new ServiceException(String.format("%1$s is assigned and cannot be deleted", role.getRoleName()));
            }
        }
        // Delete role-menu association
        roleMenuMapper.deleteRoleMenu(roleIds);
        // Delete role-dept association
        roleDeptMapper.deleteRoleDept(roleIds);
        return roleMapper.deleteRoleByIds(roleIds);
    }

    /**
     * Cancel user-role authorization
     *
     * @param userRole User-role association information
     * @return Result
     */
    @Override
    public int deleteAuthUser(SysUserRole userRole)
    {
        return userRoleMapper.deleteUserRoleInfo(userRole);
    }

    /**
     * Batch cancel user-role authorization
     *
     * @param roleId Role ID
     * @param userIds User IDs to cancel authorization
     * @return Result
     */
    @Override
    public int deleteAuthUsers(Long roleId, Long[] userIds)
    {
        return userRoleMapper.deleteUserRoleInfos(roleId, userIds);
    }

    /**
     * Batch select and authorize user roles
     *
     * @param roleId Role ID
     * @param userIds User data IDs to authorize
     * @return Result
     */
    @Override
    public int insertAuthUsers(Long roleId, Long[] userIds)
    {
        // Insert user-role management
        List<SysUserRole> list = new ArrayList<SysUserRole>();
        for (Long userId : userIds)
        {
            SysUserRole ur = new SysUserRole();
            ur.setUserId(userId);
            ur.setRoleId(roleId);
            list.add(ur);
        }
        return userRoleMapper.batchUserRole(list);
    }
}
