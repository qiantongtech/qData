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

// 查询标准信息登记列表
export function listDpDocument(query) {
    return request({
        url: '/dp/document/list',
        method: 'get',
        params: query
    })
}

// 查询标准信息登记详细
export function getDpDocument(ID) {
    return request({
        url: '/dp/document/' + ID,
        method: 'get'
    })
}

// 新增标准信息登记
export function addDpDocument(data) {
    return request({
        url: '/dp/document',
        method: 'post',
        data: data
    })
}

// 修改标准信息登记
export function updateDpDocument(data) {
    return request({
        url: '/dp/document',
        method: 'put',
        data: data
    })
}

// 删除标准信息登记
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
