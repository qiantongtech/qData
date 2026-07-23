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
import { parseStrEmpty } from '@/utils/anivia.js';

// Query user list
export function listUser(query) {
    return request({
        url: '/system/user/list',
        method: 'get',
        params: query
    });
}

// Query user details
export function getUser(userId) {
    return request({
        url: '/system/user/' + parseStrEmpty(userId),
        method: 'get'
    });
}

// Add new user
export function addUser(data) {
    return request({
        url: '/system/user',
        method: 'post',
        data: data
    });
}

// Modify user
export function updateUser(data) {
    return request({
        url: '/system/user',
        method: 'put',
        data: data
    });
}

// Delete user
export function delUser(userId) {
    return request({
        url: '/system/user/' + userId,
        method: 'delete'
    });
}

// User password reset
export function resetUserPwd(userId, password) {
    const data = {
        userId,
        password
    };
    return request({
        url: '/system/user/resetPwd',
        method: 'put',
        data: data
    });
}

// User status modification
export function changeUserStatus(userId, status) {
    const data = {
        userId,
        status
    };
    return request({
        url: '/system/user/changeStatus',
        method: 'put',
        data: data
    });
}

// Query user personal information
export function getUserProfile() {
    return request({
        url: '/system/user/profile',
        method: 'get'
    });
}

// Modify user personal information
export function updateUserProfile(data) {
    return request({
        url: '/system/user/profile',
        method: 'put',
        data: data
    });
}

// User password reset
export function updateUserPwd(oldPassword, newPassword) {
    const data = {
        oldPassword,
        newPassword
    };
    return request({
        url: '/system/user/profile/updatePwd',
        method: 'put',
        params: data
    });
}

// User avatar upload
export function uploadAvatar(data) {
    return request({
        url: '/system/user/profile/avatar',
        method: 'post',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        data: data
    });
}
// User avatar upload
export function avatarSystemContent(data) {
    return request({
        url: '/system/user/profile/avatarSystemContent',
        method: 'post',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        data: data
    });
}

// Query authorization roles
export function getAuthRole(userId) {
    return request({
        url: '/system/user/authRole/' + userId,
        method: 'get'
    });
}

// Save authorization role
export function updateAuthRole(data) {
    return request({
        url: '/system/user/authRole',
        method: 'put',
        params: data
    });
}

// Query department drop-down tree structure
export function deptTreeSelect() {
    return request({
        url: '/system/user/deptTree',
        method: 'get'
    });
}

export function deptTreeSelectNoPermi() {
    return request({
        url: '/system/user/noPermi/deptTree',
        method: 'get'
    });
}

//Department user drop-down tree structure
export function deptUserTree() {
    return request({
        url: '/system/user/userDeptTree',
        method: 'get'
    });
}
