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
import tech.qiantong.qdata.module.system.domain.SysUserRole;

import java.util.List;

/**
 * User-role association table data layer
 *
 * @author qdata
 */
public interface SysUserRoleMapper
{
    /**
     * Delete user-role association by user ID
     *
     * @param userId user ID
     * @return result
     */
    public int deleteUserRoleByUserId(Long userId);

    /**
     * Batch delete user-role associations
     *
     * @param sysUserRoleList user ID and role ID list
     * @return result
     */
    public int deleteUserRoleList(@Param("sysUserRoleList") List<SysUserRole> sysUserRoleList);

    /**
     * Query user-role information by user ID list
     *
     * @param userIdList user ID list
     * @return result
     */
    public List<SysUserRole> getByUserIdList(@Param("userIdList") List<Long> userIdList);

    /**
     * Batch delete user-role associations by IDs
     *
     * @param ids data IDs to delete
     * @return result
     */
    public int deleteUserRole(Long[] ids);

    /**
     * Count role usage by role ID
     *
     * @param roleId role ID
     * @return result
     */
    public int countUserRoleByRoleId(Long roleId);

    /**
     * Query user-role by role ID
     *
     * @param roleId role ID
     * @return result
     */
    public List<SysUserRole> getUserRoleByRoleId(Long roleId);

    /**
     * Batch insert user-role information
     *
     * @param userRoleList user-role list
     * @return result
     */
    public int batchUserRole(List<SysUserRole> userRoleList);

    /**
     * Delete user-role association information
     *
     * @param userRole user-role association information
     * @return result
     */
    public int deleteUserRoleInfo(SysUserRole userRole);

    /**
     * Batch revoke user-role authorization
     *
     * @param roleId  role ID
     * @param userIds user data IDs to delete
     * @return result
     */
    public int deleteUserRoleInfos(@Param("roleId") Long roleId, @Param("userIds") Long[] userIds);
}
