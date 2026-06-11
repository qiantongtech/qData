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

// 查询标签管理类目管理列表
export function listAttTagCat(query) {
    return request({
        url: '/att/tagCat/list',
        method: 'get',
        params: query
    })
}

// 查询标签管理类目管理详细
export function getAttTagCat(id) {
    return request({
        url: '/att/tagCat/' + id,
        method: 'get'
    })
}

// 新增标签管理类目管理
export function addAttTagCat(data) {
    return request({
        url: '/att/tagCat',
        method: 'post',
        data: data
    })
}

// 修改标签管理类目管理
export function updateAttTagCat(data) {
    return request({
        url: '/att/tagCat',
        method: 'put',
        data: data
    })
}

// 删除标签管理类目管理
export function delAttTagCat(id) {
    return request({
        url: '/att/tagCat/' + id,
        method: 'delete'
    })
}
