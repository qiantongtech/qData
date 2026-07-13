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

// Query menu list
export function listMenu(query) {
    return request({
        url: '/system/menu/list',
        method: 'get',
        params: query
    });
}

// Query menu details
export function getMenu(menuId) {
    return request({
        url: '/system/menu/' + menuId,
        method: 'get'
    });
}

// Query menu drop-down tree structure
export function treeselect() {
    return request({
        url: '/system/menu/treeselect',
        method: 'get'
    });
}

// Query menu drop-down tree structure based on role ID
export function roleMenuTreeselect(roleId) {
    return request({
        url: '/system/menu/roleMenuTreeselect/' + roleId,
        method: 'get'
    });
}

// Query menu drop-down tree structure (limited to data research and development module)
export function treeselectDpp() {
    return request({
        url: '/system/menu/treeselectDpp',
        method: 'get'
    });
}

// Query the menu drop-down tree structure based on the role ID (only in the data research and development module)
export function roleMenuTreeselectDpp(roleId) {
    return request({
        url: '/system/menu/roleMenuTreeselectDpp/' + roleId,
        method: 'get'
    });
}

// Query menu drop-down tree structure (excluding data research and development module)
export function treeselectNoDpp() {
    return request({
        url: '/system/menu/treeselectNoDpp',
        method: 'get'
    });
}

// Query the menu drop-down tree structure based on the role ID (excluding the data research and development module)
export function roleMenuTreeselectNoDpp(roleId) {
    return request({
        url: '/system/menu/roleMenuTreeselectNoDpp/' + roleId,
        method: 'get'
    });
}

// New menu
export function addMenu(data) {
    return request({
        url: '/system/menu',
        method: 'post',
        data: data
    });
}

// Modify menu
export function updateMenu(data) {
    return request({
        url: '/system/menu',
        method: 'put',
        data: data
    });
}

// delete menu
export function delMenu(menuId) {
    return request({
        url: '/system/menu/' + menuId,
        method: 'delete'
    });
}
