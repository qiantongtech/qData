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

package tech.qiantong.qdata.module.system.controller.admin.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.qiantong.qdata.common.annotation.Log;
import tech.qiantong.qdata.common.constant.UserConstants;
import tech.qiantong.qdata.common.core.controller.BaseController;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.domain.entity.SysMenu;
import tech.qiantong.qdata.common.enums.BusinessType;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.module.system.service.ISysMenuService;

import java.util.List;

/**
 * Menu Information
 *
 * @author qdata
 */
@RestController
@RequestMapping("/system/menu")
public class SysMenuController extends BaseController
{
    @Autowired
    private ISysMenuService menuService;

    /**
     * Get menu list
     */
    @PreAuthorize("@ss.hasPermi('system:menu:list')")
    @GetMapping("/list")
    public AjaxResult list(SysMenu menu)
    {
        List<SysMenu> menus = menuService.selectMenuList(menu, getUserId());
        return success(menus);
    }

    /**
     * Get detailed information by menu ID
     */
    @PreAuthorize("@ss.hasPermi('system:menu:query')")
    @GetMapping(value = "/{menuId}")
    public AjaxResult getInfo(@PathVariable Long menuId)
    {
        return success(menuService.selectMenuById(menuId));
    }

    /**
     * Get menu dropdown tree list
     */
    @GetMapping("/treeselect")
    public AjaxResult treeselect(SysMenu menu)
    {
        List<SysMenu> menus = menuService.selectMenuList(menu, getUserId());
        return success(menuService.buildMenuTreeSelect(menus));
    }

    /**
     * Get menu dropdown tree list (excluding data development module)
     */
    @GetMapping("/treeselectNoDpp")
    public AjaxResult treeselectNoDpp(SysMenu menu)
    {
        List<SysMenu> menus = menuService.selectMenuList(menu, getUserId());
        return success(menuService.buildMenuTreeNoSelectDpp(menus));
    }

    /**
     * Get menu dropdown tree list (only for data development module)
     */
    @GetMapping("/treeselectDpp")
    public AjaxResult treeselectDpp(SysMenu menu)
    {
        List<SysMenu> menus = menuService.selectMenuList(menu, 1L);
        return success(menuService.buildMenuTreeSelectDpp(menus));
    }

    /**
     * Load role-specific menu list tree
     */
    @GetMapping(value = "/roleMenuTreeselect/{roleId}")
    public AjaxResult roleMenuTreeselect(@PathVariable("roleId") Long roleId)
    {
        List<SysMenu> menus = menuService.selectMenuList(getUserId());
        AjaxResult ajax = AjaxResult.success();
        ajax.put("checkedKeys", menuService.selectMenuListByRoleId(roleId));
        ajax.put("menus", menuService.buildMenuTreeSelect(menus));
        return ajax;
    }

    /**
     * Load role-specific menu list tree (excluding data development module)
     */
    @GetMapping(value = "/roleMenuTreeselectNoDpp/{roleId}")
    public AjaxResult roleMenuTreeselectNoDpp(@PathVariable("roleId") Long roleId)
    {
        List<SysMenu> menus = menuService.selectMenuList(getUserId());
        AjaxResult ajax = AjaxResult.success();
        ajax.put("checkedKeys", menuService.selectMenuListByRoleId(roleId));
        ajax.put("menus", menuService.buildMenuTreeNoSelectDpp(menus));
        return ajax;
    }

    /**
     * Load role-specific menu list tree (only for data development module)
     */
    @GetMapping(value = "/roleMenuTreeselectDpp/{roleId}")
    public AjaxResult roleMenuTreeselectDpp(@PathVariable("roleId") Long roleId)
    {
        SysMenu sysMenu = new SysMenu();
        sysMenu.setPath("dpp");
        List<SysMenu> menus = menuService.selectMenuList(sysMenu,1L);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("checkedKeys", menuService.selectMenuListByRoleId(roleId));
        ajax.put("menus", menuService.buildMenuTreeSelectDpp(menus));
        return ajax;
    }

    /**
     * Add menu
     */
    @PreAuthorize("@ss.hasPermi('system:menu:add')")
    @Log(title = "log.op.title.system.menu", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysMenu menu)
    {
        if (!menuService.checkMenuNameUnique(menu))
        {
            return error("Add menu '" + menu.getMenuName() + "' failed, menu name already exists");
        }
        else if (UserConstants.YES_FRAME.equals(menu.getIsFrame()) && !StringUtils.ishttp(menu.getPath()))
        {
            return error("Add menu '" + menu.getMenuName() + "' failed, address must start with http(s)://");
        }
        menu.setCreateBy(getUsername());
        return toAjax(menuService.insertMenu(menu));
    }

    /**
     * Modify menu
     */
    @PreAuthorize("@ss.hasPermi('system:menu:edit')")
    @Log(title = "log.op.title.system.menu", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysMenu menu)
    {
        if (!menuService.checkMenuNameUnique(menu))
        {
            return error("Modify menu '" + menu.getMenuName() + "' failed, menu name already exists");
        }
        else if (UserConstants.YES_FRAME.equals(menu.getIsFrame()) && !StringUtils.ishttp(menu.getPath()))
        {
            return error("Modify menu '" + menu.getMenuName() + "' failed, address must start with http(s)://");
        }
        else if (menu.getMenuId().equals(menu.getParentId()))
        {
            return error("Modify menu '" + menu.getMenuName() + "' failed, parent menu cannot be itself");
        }
        menu.setUpdateBy(getUsername());
        return toAjax(menuService.updateMenu(menu));
    }

    /**
     * Delete menu
     */
    @PreAuthorize("@ss.hasPermi('system:menu:remove')")
    @Log(title = "log.op.title.system.menu", businessType = BusinessType.DELETE)
    @DeleteMapping("/{menuId}")
    public AjaxResult remove(@PathVariable("menuId") Long menuId)
    {
        if (menuService.hasChildByMenuId(menuId))
        {
            return warn("Sub-menus exist, deletion is not allowed");
        }
        if (menuService.checkMenuExistRole(menuId))
        {
            return warn("Menu is assigned, deletion is not allowed");
        }
        return toAjax(menuService.deleteMenuById(menuId));
    }
}
