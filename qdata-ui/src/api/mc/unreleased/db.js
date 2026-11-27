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

// Query library metadata list
export function listDb(query) {
    return request({
        url: '/mc/db/list',
        method: 'get',
        params: query
    });
}

// Query library metadata details
export function getDb(id) {
    return request({
        url: '/mc/db/' + id,
        method: 'get'
    });
}

// Add new library metadata
export function addDb(data) {
    return request({
        url: '/mc/db',
        method: 'post',
        data: data
    });
}

// Modify library metadata
export function updateDb(data) {
    return request({
        url: '/mc/db',
        method: 'put',
        data: data
    });
}

// Delete library metadata
export function delDb(id) {
    return request({
        url: '/mc/unreleased/db/' + id,
        method: 'delete'
    });
}

// Modify library metadata status
export function updateDbStatus(data) {
    return request({
        url: '/mc/unreleased/db/toggle',
        method: 'post',
        data
    });
}

// Get the deleteable columns of library metadata
export function batchDeleteCheck(id) {
    return request({
        url: '/mc/unreleased/db/batchDeleteCheck/' + id,
        method: 'get'
    });
}

