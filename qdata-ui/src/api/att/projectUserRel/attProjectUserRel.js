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

import request from '@/utils/request';

const buildHeaders = (options = {}) => ({
    hideErrorMessage: options.hideErrorMessage === true
});

// Query the list of relationships between projects and users
export function listAttProjectUserRel(query, options = {}) {
    return request({
        url: '/att/projectUserRel/list',
        method: 'get',
        params: query,
        headers: buildHeaders(options)
    });
}

// Query the detailed relationship between items and users
export function getAttProjectUserRel(id, options = {}) {
    return request({
        url: '/att/projectUserRel/' + id,
        method: 'get',
        headers: buildHeaders(options)
    });
}

// Query the detailed relationship between items and users
export function getRoleUser(id, options = {}) {
    return request({
        url: '/att/projectUserRel/roleUser/' + id,
        method: 'get',
        headers: buildHeaders(options)
    });
}

// Add new project-user relationship
export function addAttProjectUserRel(data, options = {}) {
    return request({
        url: '/att/projectUserRel',
        method: 'post',
        data: data,
        headers: buildHeaders(options)
    });
}

// Add new project-user relationship
export function addUserListAndRoleList(data, options = {}) {
    return request({
        url: '/att/projectUserRel/addUserListAndRoleList',
        method: 'post',
        data: data,
        headers: buildHeaders(options)
    });
}

// Modify the relationship between projects and users
export function updateAttProjectUserRel(data, options = {}) {
    return request({
        url: '/att/projectUserRel',
        method: 'put',
        data: data,
        headers: buildHeaders(options)
    });
}

// Modify the relationship between projects and users
export function editUserListAndRoleList(data, options = {}) {
    return request({
        url: '/att/projectUserRel/editUserListAndRoleList',
        method: 'put',
        data: data,
        headers: buildHeaders(options)
    });
}

// Delete the relationship between project and user
export function delAttProjectUserRel(id, options = {}) {
    return request({
        url: '/att/projectUserRel/' + id,
        method: 'delete',
        headers: buildHeaders(options)
    });
}

// Query role list
export function listRole(query, options = {}) {
    return request({
        url: '/att/projectUserRel/role/list',
        method: 'get',
        params: query,
        headers: buildHeaders(options)
    });
}

// Query role details
export function getRole(roleId, options = {}) {
    return request({
        url: '/att/projectUserRel/role/' + roleId,
        method: 'get',
        headers: buildHeaders(options)
    });
}

// Add new role
export function addRole(data, options = {}) {
    return request({
        url: '/att/projectUserRel/role',
        method: 'post',
        data: data,
        headers: buildHeaders(options)
    });
}

// Modify role
export function updateRole(data, options = {}) {
    return request({
        url: '/att/projectUserRel/role',
        method: 'put',
        data: data,
        headers: buildHeaders(options)
    });
}

// Role data permissions
export function dataScope(data, options = {}) {
    return request({
        url: '/att/projectUserRel/role/dataScope',
        method: 'put',
        data: data,
        headers: buildHeaders(options)
    });
}

// Character status modification
export function changeRoleStatus(roleId, status, options = {}) {
    const data = {
        roleId,
        status
    };
    return request({
        url: '/att/projectUserRel/role/changeStatus',
        method: 'put',
        data: data,
        headers: buildHeaders(options)
    });
}

// Delete role
export function delRole(roleId, options = {}) {
    return request({
        url: '/att/projectUserRel/role/' + roleId,
        method: 'delete',
        headers: buildHeaders(options)
    });
}

// Query the list of users authorized by the role
export function allocatedUserList(query, options = {}) {
    return request({
        url: '/att/projectUserRel/role/authUser/allocatedList',
        method: 'get',
        params: query,
        headers: buildHeaders(options)
    });
}

// Query the list of users whose role is not authorized
export function unallocatedUserList(query, options = {}) {
    return request({
        url: '/att/projectUserRel/role/authUser/unallocatedList',
        method: 'get',
        params: query,
        headers: buildHeaders(options)
    });
}

// Cancel user authorization role
export function authUserCancel(data, options = {}) {
    return request({
        url: '/att/projectUserRel/role/authUser/cancel',
        method: 'put',
        data: data,
        headers: buildHeaders(options)
    });
}

// Cancel user authorization roles in batches
export function authUserCancelAll(data, options = {}) {
    return request({
        url: '/att/projectUserRel/role/authUser/cancelAll',
        method: 'put',
        params: data,
        headers: buildHeaders(options)
    });
}

// Authorized user selection
export function authUserSelectAll(data, options = {}) {
    return request({
        url: '/att/projectUserRel/role/authUser/selectAll',
        method: 'put',
        params: data,
        headers: buildHeaders(options)
    });
}

// Query department tree structure based on role ID
export function deptTreeSelect(roleId, options = {}) {
    return request({
        url: '/att/projectUserRel/role/deptTree/' + roleId,
        method: 'get',
        headers: buildHeaders(options)
    });
}
