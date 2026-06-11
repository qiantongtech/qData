/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
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
