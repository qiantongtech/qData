/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please contact the official team for authorization.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

import request from '@/utils/request.js';
import { parseStrEmpty } from '@/utils/anivia.js';

// 查询用户列表
export function listUser(query) {
    return request({
        url: '/system/user/list',
        method: 'get',
        params: query
    });
}

// 查询用户详细
export function getUser(userId) {
    return request({
        url: '/system/user/' + parseStrEmpty(userId),
        method: 'get'
    });
}

// 新增用户
export function addUser(data) {
    return request({
        url: '/system/user',
        method: 'post',
        data: data
    });
}

// 修改用户
export function updateUser(data) {
    return request({
        url: '/system/user',
        method: 'put',
        data: data
    });
}

// 删除用户
export function delUser(userId) {
    return request({
        url: '/system/user/' + userId,
        method: 'delete'
    });
}

// 用户密码重置
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

// 用户状态修改
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

// 查询用户个人信息
export function getUserProfile() {
    return request({
        url: '/system/user/profile',
        method: 'get'
    });
}

// 修改用户个人信息
export function updateUserProfile(data) {
    return request({
        url: '/system/user/profile',
        method: 'put',
        data: data
    });
}

// 用户密码重置
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

// 用户头像上传
export function uploadAvatar(data) {
    return request({
        url: '/system/user/profile/avatar',
        method: 'post',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        data: data
    });
}
// 用户头像上传
export function avatarSystemContent(data) {
    return request({
        url: '/system/user/profile/avatarSystemContent',
        method: 'post',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        data: data
    });
}

// 查询授权角色
export function getAuthRole(userId) {
    return request({
        url: '/system/user/authRole/' + userId,
        method: 'get'
    });
}

// 保存授权角色
export function updateAuthRole(data) {
    return request({
        url: '/system/user/authRole',
        method: 'put',
        params: data
    });
}

// 查询部门下拉树结构
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

//部门用户下拉树结构
export function deptUserTree() {
    return request({
        url: '/system/user/userDeptTree',
        method: 'get'
    });
}
