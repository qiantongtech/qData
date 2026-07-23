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

import request from '@/utils/request'

// Query standard information registration list
export function listDpDocument(query) {
    return request({
        url: '/dp/document/list',
        method: 'get',
        params: query
    })
}

// Query standard information registration details
export function getDpDocument(ID) {
    return request({
        url: '/dp/document/' + ID,
        method: 'get'
    })
}

// Add new standard information registration
export function addDpDocument(data) {
    return request({
        url: '/dp/document',
        method: 'post',
        data: data
    })
}

// Modify standard information registration
export function updateDpDocument(data) {
    return request({
        url: '/dp/document',
        method: 'put',
        data: data
    })
}

// Delete standard information registration
export function delDpDocument(ID) {
    return request({
        url: '/dp/document/' + ID,
        method: 'delete'
    })
}

export function listAttDocumentCat(query) {
    return request({
        url: 'att/documentCat/getAttDocumentCatList',
        method: 'get',
        params: query
    })
}
