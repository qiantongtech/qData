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

import request from '@/utils/rpRequest';

// tree on left
export function catApiList(query) {
    return request({
        url: '/rp/attApi/catApi/list',
        method: 'get',
        params: query
    });
}

// list
export function dsApiList(query) {
    return request({
        url: '/rp/dsApi/list',
        method: 'get',
        params: query
    });
}

// List details
export function dsApiView(id) {
    return request({
        url: '/rp/dsApi/' + id,
        method: 'get'
    });
}

// Check my application statistics in Personal Center
export function applyCount() {
    return request({
        url: '/rp/dsApi/applyCount',
        method: 'get'
    });
}

// List of APIs that can be applied for
export function areaDict(data) {
    return request({
        url: '/rp/rpDict/areaDict',
        method: 'post',
        data: data
    });
}

export function applyAdd(data) {
    return request({
        url: '/da/apply',
        method: 'post',
        data: data
    });
}

export function applyEdit(data) {
    return request({
        url: '/da/apply',
        method: 'put',
        data: data
    });
}

// Query API application service details
export function getDsApiApply(id) {
    return request({
        url: '/da/apply/' + id,
        method: 'get'
    });
}

// Delete API application service
export function applyDel(id) {
    return request({
        url: '/da/apply/' + id,
        method: 'delete'
    });
}
