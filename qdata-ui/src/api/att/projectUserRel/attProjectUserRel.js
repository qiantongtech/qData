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

// Query the list of relationships between projects and users
export function listAttProjectUserRel(query) {
    return request({
        url: '/att/projectUserRel/list',
        method: 'get',
        params: query
    });
}

// Query the detailed relationship between items and users
export function getAttProjectUserRel(id) {
    return request({
        url: '/att/projectUserRel/' + id,
        method: 'get'
    });
}

// Query the detailed relationship between items and users
export function getRoleUser(id) {
    return request({
        url: '/att/projectUserRel/roleUser/' + id,
        method: 'get'
    });
}

// Add new project-user relationship
export function addAttProjectUserRel(data) {
    return request({
        url: '/att/projectUserRel',
        method: 'post',
        data: data
    });
}

// Add new project-user relationship
export function addUserListAndRoleList(data) {
    return request({
        url: '/att/projectUserRel/addUserListAndRoleList',
        method: 'post',
        data: data
    });
}

// Modify the relationship between projects and users
export function updateAttProjectUserRel(data) {
    return request({
        url: '/att/projectUserRel',
        method: 'put',
        data: data
    });
}

// Modify the relationship between projects and users
export function editUserListAndRoleList(data) {
    return request({
        url: '/att/projectUserRel/editUserListAndRoleList',
        method: 'put',
        data: data
    });
}

// Delete the relationship between project and user
export function delAttProjectUserRel(id) {
    return request({
        url: '/att/projectUserRel/' + id,
        method: 'delete'
    });
}

// Query role list
export function listRole(query) {
    return request({
        url: '/att/projectUserRel/role/list',
        method: 'get',
        params: query
    });
}

// Query role details
export function getRole(roleId) {
    return request({
        url: '/att/projectUserRel/role/' + roleId,
        method: 'get'
    });
}

// Add new role
export function addRole(data) {
    return request({
        url: '/att/projectUserRel/role',
        method: 'post',
        data: data
    });
}

// Modify role
export function updateRole(data) {
    return request({
        url: '/att/projectUserRel/role',
        method: 'put',
        data: data
    });
}

// Role data permissions
export function dataScope(data) {
    return request({
        url: '/att/projectUserRel/role/dataScope',
        method: 'put',
        data: data
    });
}

// Character status modification
export function changeRoleStatus(roleId, status) {
    const data = {
        roleId,
        status
    };
    return request({
        url: '/att/projectUserRel/role/changeStatus',
        method: 'put',
        data: data
    });
}

// Delete role
export function delRole(roleId) {
    return request({
        url: '/att/projectUserRel/role/' + roleId,
        method: 'delete'
    });
}

// Query the list of users authorized by the role
export function allocatedUserList(query) {
    return request({
        url: '/att/projectUserRel/role/authUser/allocatedList',
        method: 'get',
        params: query
    });
}

// Query the list of users whose role is not authorized
export function unallocatedUserList(query) {
    return request({
        url: '/att/projectUserRel/role/authUser/unallocatedList',
        method: 'get',
        params: query
    });
}

// Cancel user authorization role
export function authUserCancel(data) {
    return request({
        url: '/att/projectUserRel/role/authUser/cancel',
        method: 'put',
        data: data
    });
}

// Cancel user authorization roles in batches
export function authUserCancelAll(data) {
    return request({
        url: '/att/projectUserRel/role/authUser/cancelAll',
        method: 'put',
        params: data
    });
}

// Authorized user selection
export function authUserSelectAll(data) {
    return request({
        url: '/att/projectUserRel/role/authUser/selectAll',
        method: 'put',
        params: data
    });
}

// Query department tree structure based on role ID
export function deptTreeSelect(roleId) {
    return request({
        url: '/att/projectUserRel/role/deptTree/' + roleId,
        method: 'get'
    });
}
