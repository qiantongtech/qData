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

import request from '@/utils/request';

// Query business domain list
export function listDomain(query) {
    return request({
        url: '/mc/domain/list',
        method: 'get',
        params: query
    });
}

// Query business domain details
export function getDomain(id) {
    return request({
        url: '/mc/domain/' + id,
        method: 'get'
    });
}

// Add new business domain
export function addDomain(data) {
    return request({
        url: '/mc/domain',
        method: 'post',
        data: data
    });
}

// Modify business domain
export function updateDomain(data) {
    return request({
        url: '/mc/domain',
        method: 'put',
        data: data
    });
}

// Delete business domain
export function delDomain(id) {
    return request({
        url: '/mc/domain/' + id,
        method: 'delete'
    });
}

// Modify business domain status
export function updateDomainStatus(id, validFlag) {
    return request({
        url: '/mc/domain',
        method: 'put',
        data: { id, validFlag }
    });
}

// Get the deleteable columns of the business domain
export function batchDeleteCheck(id) {
    return request({
        url: '/mc/domain/batchDeleteCheck/' + id,
        method: 'get'
    });
}
