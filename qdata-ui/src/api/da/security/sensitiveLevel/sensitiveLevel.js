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

import request from '@/utils/request.js';

// 查询敏感等级列表
export function listDaSensitiveLevel(query) {
    return request({
        url: '/da/sensitiveLevel/list',
        method: 'get',
        params: query
    });
}

// 查询敏感等级详细
export function getDaSensitiveLevel(id) {
    return request({
        url: '/da/sensitiveLevel/' + id,
        method: 'get'
    });
}

// 新增敏感等级
export function addDaSensitiveLevel(data) {
    return request({
        url: '/da/sensitiveLevel',
        method: 'post',
        data: data
    });
}

// 修改状态 上线/下线
export function updateStatus(id, status) {
    return request({
        url: `/da/sensitiveLevel/updateStatus/${id}/${status}`,
        method: 'post'
    });
}

// 修改敏感等级
export function updateDaSensitiveLevel(data) {
    return request({
        url: '/da/sensitiveLevel',
        method: 'put',
        data: data
    });
}

// 删除敏感等级
export function delDaSensitiveLevel(id) {
    return request({
        url: '/da/sensitiveLevel/' + id,
        method: 'delete'
    });
}
