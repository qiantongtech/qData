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

import request from '@/utils/request';

// 查询主题列表
export function listAttTheme(query) {
    return request({
        url: '/att/theme/list',
        method: 'get',
        params: query
    });
}

// 查询主题详细
export function getAttTheme(id) {
    return request({
        url: '/att/theme/' + id,
        method: 'get'
    });
}

// 新增主题
export function addAttTheme(data) {
    return request({
        url: '/att/theme',
        method: 'post',
        data: data
    });
}

// 修改主题
export function updateAttTheme(data) {
    return request({
        url: '/att/theme',
        method: 'put',
        data: data
    });
}

// 删除主题
export function delAttTheme(id) {
    return request({
        url: '/att/theme/' + id,
        method: 'delete'
    });
}
// 获取主题的查询接口
export function getThemeList(query) {
    return request({
        url: '/att/theme/getAttThemeListByReqVO',
        method: 'get',
        params: query
    });
}
