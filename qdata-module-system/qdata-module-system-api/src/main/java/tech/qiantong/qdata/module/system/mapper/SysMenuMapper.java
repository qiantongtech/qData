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
import tech.qiantong.qdata.common.core.domain.entity.SysMenu;

import java.util.List;

/**
 * Menu table data layer
 *
 * @author qdata
 */
public interface SysMenuMapper
{
    /**
     * Query system menu list
     *
     * @param menu menu information
     * @return menu list
     */
    public List<SysMenu> selectMenuList(SysMenu menu);

    /**
     * Query all menu permissions
     *
     * @return permission list
     */
    public List<String> selectMenuPerms();

    /**
     * Query system menu list by user
     *
     * @param menu menu information
     * @return menu list
     */
    public List<SysMenu> selectMenuListByUserId(SysMenu menu);

    /**
     * Query permissions by role ID
     *
     * @param roleId role ID
     * @return permission list
     */
    public List<String> selectMenuPermsByRoleId(Long roleId);

    /**
     * Query permissions by user ID
     *
     * @param userId user ID
     * @return permission list
     */
    public List<String> selectMenuPermsByUserId(Long userId);

    /**
     * Query menu tree by user ID
     *
     * @return menu list
     */
    public List<SysMenu> selectMenuTreeAll();

    /**
     * Query menu tree by user ID
     *
     * @param userId user ID
     * @return menu list
     */
    public List<SysMenu> selectMenuTreeByUserId(Long userId);

    /**
     * Query menu tree by user ID and project ID
     *
     * @param userId user ID
     * @param projectId project ID
     * @return menu list
     */
    public List<SysMenu> selectMenuTreeByUserIdAndProjectId(@Param("userId") Long userId,@Param("projectId") Long projectId);

    /**
     * Query menu tree information by role ID
     *
     * @param roleId role ID
     * @param menuCheckStrictly whether menu tree selection items are associatively displayed
     * @return selected menu list
     */
    public List<Long> selectMenuListByRoleId(@Param("roleId") Long roleId, @Param("menuCheckStrictly") boolean menuCheckStrictly);

    /**
     * Query menu information by menu ID
     *
     * @param menuId menu ID
     * @return menu information
     */
    public SysMenu selectMenuById(Long menuId);

    /**
     * Check if menu has child nodes
     *
     * @param menuId menu ID
     * @return result
     */
    public int hasChildByMenuId(Long menuId);

    /**
     * Insert menu information
     *
     * @param menu menu information
     * @return result
     */
    public int insertMenu(SysMenu menu);

    /**
     * Update menu information
     *
     * @param menu menu information
     * @return result
     */
    public int updateMenu(SysMenu menu);

    /**
     * Delete menu management information
     *
     * @param menuId menu ID
     * @return result
     */
    public int deleteMenuById(Long menuId);

    /**
     * Validate whether menu name is unique
     *
     * @param menuName menu name
     * @param parentId parent menu ID
     * @return result
     */
    public SysMenu checkMenuNameUnique(@Param("menuName") String menuName, @Param("parentId") Long parentId);
}
