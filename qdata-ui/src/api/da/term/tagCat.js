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

import request from '@/utils/request.js'

// Query term label category management list
export function listAttTagCat(query) {
    return request({
        url: '/da/termCat/list',
        method: 'get',
        params: query
    })
}

// Query term label category management details
export function getAttTagCat(id) {
    return request({
        url: '/da/termCat/' + id,
        method: 'get'
    })
}

// Added term label category management
export function addAttTagCat(data) {
    return request({
        url: '/da/termCat',
        method: 'post',
        data: data
    })
}

// Modify term label category management
export function updateAttTagCat(data) {
    return request({
        url: '/da/termCat',
        method: 'put',
        data: data
    })
}

// Delete term tag category management
export function delAttTagCat(id) {
    return request({
        url: '/da/termCat/' + id,
        method: 'delete'
    })
}
