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

import request from '@/utils/rpRequest';

// 查询服务资源门户部门列表
export function applyCount(userId) {
    return request({
        url: '/rp/dsApi/applyCount/' + userId,
        method: 'get'
    });
}

export function flyfowApiApply(param) {
    return request({
        url: '/da/apply/flyfowApiApply',
        method: 'post',
        data: param
    });
}
