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
 * Business License: https://docs.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

import request from '@/utils/request.js';

// Query sensitivity level list
export function listDgSensitiveLevel(query) {
    return request({
        url: '/dg/sensitiveLevel/list',
        method: 'get',
        params: query
    });
}

// Query sensitivity level details
export function getDgSensitiveLevel(id) {
    return request({
        url: '/dg/sensitiveLevel/' + id,
        method: 'get'
    });
}

// Add sensitivity level
export function addDgSensitiveLevel(data) {
    return request({
        url: '/dg/sensitiveLevel',
        method: 'post',
        data: data
    });
}

// Modify status online/offline
export function updateStatus(id, status) {
    return request({
        url: `/dg/sensitiveLevel/updateStatus/${id}/${status}`,
        method: 'post'
    });
}

// Modify sensitivity level
export function updateDgSensitiveLevel(data) {
    return request({
        url: '/dg/sensitiveLevel',
        method: 'put',
        data: data
    });
}

// Delete sensitivity level
export function delDgSensitiveLevel(id) {
    return request({
        url: '/dg/sensitiveLevel/' + id,
        method: 'delete'
    });
}
