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

// Query table metadata list
export function listTable(query) {
    return request({
        url: '/mc/table/list',
        method: 'get',
        params: query
    });
}

// Query table metadata details
export function getTable(id) {
    return request({
        url: '/mc/table/' + id,
        method: 'get'
    });
}

// Add table metadata
export function addTable(data) {
    return request({
        url: '/mc/table',
        method: 'post',
        data: data
    });
}

// Modify table metadata
export function updateTable(data) {
    return request({
        url: '/mc/table',
        method: 'put',
        data: data
    });
}

// Delete table metadata
export function delTable(id) {
    return request({
        url: '/mc/table/' + id,
        method: 'delete'
    });
}

// Modify table metadata status
export function updateTableStatus(data) {
    return request({
        url: '/mc/table/toggle',
        method: 'post',
        data
    });
}

// Staging table metadata
export function draftTable(data) {
    return request({
        url: '/mc/table/draft',
        method: 'post',
        data: data
    });
}

// Get the deleteable columns of table metadata
export function batchDeleteCheck(id) {
    return request({
        url: '/mc/table/batchDeleteCheck/' + id,
        method: 'get'
    });
}

