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
import tech.qiantong.qdata.module.system.domain.SysRoleMenu;

import java.util.List;

/**
 * Role-menu association table data layer
 *
 * @author qdata
 */
public interface SysRoleMenuMapper
{
    /**
     * Query menu usage count
     *
     * @param menuId menu ID
     * @return result
     */
    public int checkMenuExistRole(Long menuId);

    /**
     * Batch query menus by role ID list
     *
     * @param roleIdList role ID list
     * @return result
     */
    public List<SysRoleMenu> getByRoleIdList(@Param("roleIdList") List<Long> roleIdList);

    /**
     * Delete role-menu association by role ID
     *
     * @param roleId role ID
     * @return result
     */
    public int deleteRoleMenuByRoleId(Long roleId);

    /**
     * Batch delete role-menu association information
     *
     * @param ids data IDs to delete
     * @return result
     */
    public int deleteRoleMenu(Long[] ids);

    /**
     * Batch insert role-menu information
     *
     * @param roleMenuList role-menu list
     * @return result
     */
    public int batchRoleMenu(List<SysRoleMenu> roleMenuList);

    /**
     * Batch insert role-menu information with project ID
     *
     * @param roleMenuList role-menu list
     * @return result
     */
    public int batchRoleMenuProjectId(List<SysRoleMenu> roleMenuList);
}
