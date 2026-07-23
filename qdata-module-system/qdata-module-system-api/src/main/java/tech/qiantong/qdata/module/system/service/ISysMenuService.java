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

import tech.qiantong.qdata.common.core.domain.TreeSelect;
import tech.qiantong.qdata.common.core.domain.entity.SysMenu;
import tech.qiantong.qdata.module.system.domain.vo.RouterVo;

import java.util.List;
import java.util.Set;

/**
 * Menu service layer
 *
 * @author qdata
 */
public interface ISysMenuService
{
    /**
     * Query system menu list by user
     *
     * @param userId user ID
     * @return menu list
     */
    public List<SysMenu> selectMenuList(Long userId);

    /**
     * Query system menu list by user
     *
     * @param menu menu information
     * @param userId user ID
     * @return menu list
     */
    public List<SysMenu> selectMenuList(SysMenu menu, Long userId);

    /**
     * Query permissions by user ID
     *
     * @param userId user ID
     * @return permission list
     */
    public Set<String> selectMenuPermsByUserId(Long userId);

    /**
     * Query permissions by role ID
     *
     * @param roleId role ID
     * @return permission list
     */
    public Set<String> selectMenuPermsByRoleId(Long roleId);

    /**
     * Query menu tree information by user ID
     *
     * @param userId user ID
     * @return menu list
     */
    public List<SysMenu> selectMenuTreeByUserId(Long userId);

    /**
     * Query menu tree information by user ID and project ID
     *
     * @param userId user ID
     * @param projectId project ID
     * @return menu list
     */
    public List<SysMenu> selectMenuTreeByUserIdAndProjectId(Long userId,Long projectId);

    /**
     * Query menu tree information by role ID
     *
     * @param roleId role ID
     * @return selected menu list
     */
    public List<Long> selectMenuListByRoleId(Long roleId);

    /**
     * Build menus required for frontend routing
     *
     * @param menus menu list
     * @return route list
     */
    public List<RouterVo> buildMenus(List<SysMenu> menus);

    /**
     * Build tree structure required by frontend
     *
     * @param menus menu list
     * @return tree structure list
     */
    public List<SysMenu> buildMenuTree(List<SysMenu> menus);

    /**
     * Build dropdown tree structure required by frontend
     *
     * @param menus menu list
     * @return dropdown tree structure list
     */
    public List<TreeSelect> buildMenuTreeSelect(List<SysMenu> menus);

    /**
     * Query menu information by ID
     *
     * @param menuId menu ID
     * @return menu information
     */
    public SysMenu selectMenuById(Long menuId);

    /**
     * Check if menu has child nodes
     *
     * @param menuId menu ID
     * @return result true exists, false does not exist
     */
    public boolean hasChildByMenuId(Long menuId);

    /**
     * Check if menu has associated roles
     *
     * @param menuId menu ID
     * @return result true exists, false does not exist
     */
    public boolean checkMenuExistRole(Long menuId);

    /**
     * Insert and save menu information
     *
     * @param menu menu information
     * @return result
     */
    public int insertMenu(SysMenu menu);

    /**
     * Update and save menu information
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
     * Check if menu name is unique
     *
     * @param menu menu information
     * @return result
     */
    public boolean checkMenuNameUnique(SysMenu menu);

    /**
     * Build dropdown tree structure required by frontend (data development module only)
     *
     * @param menus menu list
     * @return dropdown tree structure list
     */
    public List<TreeSelect> buildMenuTreeSelectDpp(List<SysMenu> menus);

    /**
     * Get menu dropdown tree list (excluding data development module)
     */
    List<TreeSelect> buildMenuTreeNoSelectDpp(List<SysMenu> menus);
}
