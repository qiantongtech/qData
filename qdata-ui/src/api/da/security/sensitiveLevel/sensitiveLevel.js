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
