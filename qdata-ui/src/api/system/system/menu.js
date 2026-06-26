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

import request from '@/utils/request.js';

// 查询菜单列表
export function listMenu(query) {
    return request({
        url: '/system/menu/list',
        method: 'get',
        params: query
    });
}

// 查询菜单详细
export function getMenu(menuId) {
    return request({
        url: '/system/menu/' + menuId,
        method: 'get'
    });
}

// 查询菜单下拉树结构
export function treeselect() {
    return request({
        url: '/system/menu/treeselect',
        method: 'get'
    });
}

// 根据角色ID查询菜单下拉树结构
export function roleMenuTreeselect(roleId) {
    return request({
        url: '/system/menu/roleMenuTreeselect/' + roleId,
        method: 'get'
    });
}

// 查询菜单下拉树结构(只限于数据研发模块)
export function treeselectDpp() {
    return request({
        url: '/system/menu/treeselectDpp',
        method: 'get'
    });
}

// 根据角色ID查询菜单下拉树结构(只限于数据研发模块)
export function roleMenuTreeselectDpp(roleId) {
    return request({
        url: '/system/menu/roleMenuTreeselectDpp/' + roleId,
        method: 'get'
    });
}

// 查询菜单下拉树结构(排除数据研发模块)
export function treeselectNoDpp() {
    return request({
        url: '/system/menu/treeselectNoDpp',
        method: 'get'
    });
}

// 根据角色ID查询菜单下拉树结构(排除数据研发模块)
export function roleMenuTreeselectNoDpp(roleId) {
    return request({
        url: '/system/menu/roleMenuTreeselectNoDpp/' + roleId,
        method: 'get'
    });
}

// 新增菜单
export function addMenu(data) {
    return request({
        url: '/system/menu',
        method: 'post',
        data: data
    });
}

// 修改菜单
export function updateMenu(data) {
    return request({
        url: '/system/menu',
        method: 'put',
        data: data
    });
}

// 删除菜单
export function delMenu(menuId) {
    return request({
        url: '/system/menu/' + menuId,
        method: 'delete'
    });
}
