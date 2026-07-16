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

package tech.qiantong.qdata.module.system.mapper;

import org.apache.ibatis.annotations.Param;
import tech.qiantong.qdata.common.core.domain.entity.SysRole;

import java.util.List;

/**
 * Role table data layer
 *
 * @author qdata
 */
public interface SysRoleMapper
{
    /**
     * Paginate query role data by conditions
     *
     * @param role role information
     * @return role data collection
     */
    public List<SysRole> selectRoleList(SysRole role);

    /**
     * Query roles by user ID
     *
     * @param userId user ID
     * @return role list
     */
    public List<SysRole> selectRolePermissionByUserId(Long userId);

    /**
     * Query all roles
     *
     * @return role list
     */
    public List<SysRole> selectRoleAll();

    /**
     * Query all roles filtered by project ID
     *
     * @return role list
     */
    public List<SysRole> selectRoleAllByProjectId(@Param("projectId") Long projectId);

    /**
     * Get role selection list by user ID
     *
     * @param userId user ID
     * @return selected role ID list
     */
    public List<Long> selectRoleListByUserId(Long userId);

    /**
     * Query role by role ID
     *
     * @param roleId role ID
     * @return role object information
     */
    public SysRole selectRoleById(Long roleId);

    /**
     * Query roles by username
     *
     * @param userName username
     * @return role list
     */
    public List<SysRole> selectRolesByUserName(String userName);

    /**
     * Validate whether role name is unique
     *
     * @param roleName role name
     * @return role information
     */
    public SysRole checkRoleNameUnique(String roleName);

    /**
     * Validate whether role name is unique with project ID
     *
     * @param role role
     * @return role information
     */
    public SysRole checkRoleNameUniqueAndProjectId(SysRole role);

    /**
     * Validate whether role key is unique
     *
     * @param roleKey role key
     * @return role information
     */
    public SysRole checkRoleKeyUnique(String roleKey);

    /**
     * Validate whether role key is unique with project ID
     *
     * @param role role
     * @return role information
     */
    public SysRole checkRoleKeyUniqueAndProjectId(SysRole role);

    /**
     * Update role information
     *
     * @param role role information
     * @return result
     */
    public int updateRole(SysRole role);

    /**
     * Insert role information
     *
     * @param role role information
     * @return result
     */
    public int insertRole(SysRole role);

    /**
     * Batch insert role information
     *
     * @param sysRoleList role information list
     * @return result
     */
    public int insertRoleList(List<SysRole> sysRoleList);

    /**
     * Delete role by role ID
     *
     * @param roleId role ID
     * @return result
     */
    public int deleteRoleById(Long roleId);

    /**
     * Batch delete role information
     *
     * @param roleIds role IDs to delete
     * @return result
     */
    public int deleteRoleByIds(Long[] roleIds);
}
