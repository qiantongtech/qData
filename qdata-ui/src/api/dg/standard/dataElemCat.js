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

// Query the standard data element category management list
export function listElemCat(query) {
    return request({
        url: '/dg/dataElemCat/list',
        method: 'get',
        params: query
    })
}

// Query standard data element category management details
export function getElemCat(id) {
    return request({
        url: '/dg/dataElemCat/' + id,
        method: 'get'
    })
}

// Added standard data element category management
export function addElemCat(data) {
    return request({
        url: '/dg/dataElemCat',
        method: 'post',
        data: data
    })
}

// Modify standard data element category management
export function updateElemCat(data) {
    return request({
        url: '/dg/dataElemCat',
        method: 'put',
        data: data
    })
}

// Delete standard data element category management
export function delElemCat(id) {
    return request({
        url: '/dg/dataElemCat/' + id,
        method: 'delete'
    })
}
// Data element classification Batch deletion verification
export function batchDeleteCheck(ids) {
    return request({
        url: '/dg/dataElemCat/batchDeleteCheck/' + ids,
        method: 'get'
    })
}