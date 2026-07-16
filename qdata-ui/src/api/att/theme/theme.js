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

import request from '@/utils/request';

// Query topic list
export function listAttTheme(query) {
    return request({
        url: '/att/theme/list',
        method: 'get',
        params: query
    });
}

// Query topic details
export function getAttTheme(id) {
    return request({
        url: '/att/theme/' + id,
        method: 'get'
    });
}

// Add topic
export function addAttTheme(data) {
    return request({
        url: '/att/theme',
        method: 'post',
        data: data
    });
}

// Modify theme
export function updateAttTheme(data) {
    return request({
        url: '/att/theme',
        method: 'put',
        data: data
    });
}

// Delete topic
export function delAttTheme(id) {
    return request({
        url: '/att/theme/' + id,
        method: 'delete'
    });
}
// Get the query interface of the topic
export function getThemeList(query) {
    return request({
        url: '/att/theme/getAttThemeListByReqVO',
        method: 'get',
        params: query
    });
}
