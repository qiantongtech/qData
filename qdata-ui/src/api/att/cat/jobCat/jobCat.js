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

// 查询作业类目管理列表
export function listAttJobCat(query) {
    return request({
        url: '/att/jobCat/list',
        method: 'get',
        params: query
    });
}

// 查询作业类目管理详细
export function getAttJobCat(id) {
    return request({
        url: '/att/jobCat/' + id,
        method: 'get'
    });
}

// 新增作业类目管理
export function addAttJobCat(data) {
    return request({
        url: '/att/jobCat',
        method: 'post',
        data: data
    });
}

// 修改作业类目管理
export function updateAttJobCat(data) {
    return request({
        url: '/att/jobCat',
        method: 'put',
        data: data
    });
}

// 删除作业类目管理
export function delAttJobCat(id) {
    return request({
        url: '/att/jobCat/' + id,
        method: 'delete'
    });
}
