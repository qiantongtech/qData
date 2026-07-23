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

// Query project list
export function listAttProject(query) {
    return request({
        url: '/att/project/list',
        method: 'get',
        params: query
    });
}

// Query the list of projects to which the current user belongs
export function currentUser() {
    return request({
        url: '/att/project/currentUser/list',
        method: 'get'
    });
}

// Query the list of projects to which the current user belongs
export function noProjectUser(query, options = {}) {
    return request({
        url: '/att/project/noProjectUser/list',
        method: 'post',
        params: query,
        headers: buildHeaders(options)
    });
}

// Query project details
export function getAttProject(id) {
    return request({
        url: '/att/project/' + id,
        method: 'get'
    });
}

// Get whether the current user has user addition and project administrator
export function addUserAndProject(id, options = {}) {
    return request({
        url: '/att/project/addUserAndProject/' + id,
        method: 'get',
        headers: buildHeaders(options)
    });
}

// Modify status
export function editProjectStatus(id, status) {
    return request({
        url: `/att/project/editProjectStatus/${id}/${status}`,
        method: 'get'
    });
}

// Add new items
export function addAttProject(data) {
    return request({
        url: '/att/project',
        method: 'post',
        data: data
    });
}

// Modify project
export function updateAttProject(data) {
    return request({
        url: '/att/project',
        method: 'put',
        data: data
    });
}

// Delete project
export function delAttProject(id) {
    return request({
        url: '/att/project/' + id,
        method: 'delete'
    });
}
