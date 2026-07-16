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

package tech.qiantong.qdata.module.system.service;

import tech.qiantong.qdata.common.core.domain.entity.SysRole;
import tech.qiantong.qdata.module.system.domain.SysUserRole;

import java.util.List;
import java.util.Set;

/**
 * Role service layer
 *
 * @author qdata
 */
public interface ISysRoleService
{
    /**
     * Query role data by conditions with pagination
     *
     * @param role role information
     * @return role data collection
     */
    public List<SysRole> selectRoleList(SysRole role);

    /**
     * Query role list by user ID
     *
     * @param userId user ID
     * @return role list
     */
    public List<SysRole> selectRolesByUserId(Long userId);

    /**
     * Query role permissions by user ID
     *
     * @param userId user ID
     * @return permission list
     */
    public Set<String> selectRolePermissionByUserId(Long userId);

    /**
     * Query all roles
     *
     * @return role list
     */
    public List<SysRole> selectRoleAll();

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
     * Check if role name is unique
     *
     * @param role role information
     * @return result
     */
    public boolean checkRoleNameUnique(SysRole role);

    /**
     * Check if role permission key is unique
     *
     * @param role role information
     * @return result
     */
    public boolean checkRoleKeyUnique(SysRole role);

    /**
     * Check if role is allowed to operate
     *
     * @param role role information
     */
    public void checkRoleAllowed(SysRole role);

    /**
     * Check if role has data scope permission
     *
     * @param roleIds role IDs
     */
    public void checkRoleDataScope(Long... roleIds);

    /**
     * Query role usage count by role ID
     *
     * @param roleId role ID
     * @return result
     */
    public int countUserRoleByRoleId(Long roleId);

    /**
     * Insert and save role information
     *
     * @param role role information
     * @return result
     */
    public int insertRole(SysRole role);

    /**
     * Update and save role information
     *
     * @param role role information
     * @return result
     */
    public int updateRole(SysRole role);

    /**
     * Update role status
     *
     * @param role role information
     * @return result
     */
    public int updateRoleStatus(SysRole role);

    /**
     * Update data scope information
     *
     * @param role role information
     * @return result
     */
    public int authDataScope(SysRole role);

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

    /**
     * Cancel user role authorization
     *
     * @param userRole user-role association information
     * @return result
     */
    public int deleteAuthUser(SysUserRole userRole);

    /**
     * Batch cancel user role authorization
     *
     * @param roleId role ID
     * @param userIds user data IDs to cancel authorization
     * @return result
     */
    public int deleteAuthUsers(Long roleId, Long[] userIds);

    /**
     * Batch select and authorize user roles
     *
     * @param roleId role ID
     * @param userIds user data IDs to authorize
     * @return result
     */
    public int insertAuthUsers(Long roleId, Long[] userIds);
}
