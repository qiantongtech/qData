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
 * For brand customization, please contact the official team for authorization.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

import request from '@/utils/request';

// 查询数据发现任务列表
export function listDaDiscoveryTask(query) {
    return request({
        url: '/da/discoveryTask/getDaDiscoveryTaskListPage',
        method: 'get',
        params: query
    });
}

// 查询数据发现任务详细
export function getDaDiscoveryTask(id) {
    return request({
        url: '/da/discoveryTask/' + id,
        method: 'get'
    });
}

// 新增数据发现任务
export function addDaDiscoveryTask(data) {
    return request({
        url: '/da/discoveryTask',
        method: 'post',
        data: data
    });
}

// 修改数据发现任务
export function updateDaDiscoveryTask(data) {
    return request({
        url: '/da/discoveryTask',
        method: 'put',
        data: data
    });
}
// 修改数据发现任务
export function updateDaDiscoveryTaskStatus(data) {
    return request({
        url: '/da/discoveryTask/updateDaDiscoveryTaskStatus',
        method: 'post',
        data: data
    });
}
// 修改数据发现任务
export function updateDaDiscoveryTaskCronExpression(data) {
    return request({
        url: '/da/discoveryTask/updateDaDiscoveryTaskCronExpression',
        method: 'post',
        data: data
    });
}

// 删除数据发现任务
export function delDaDiscoveryTask(id) {
    return request({
        url: '/da/discoveryTask/' + id,
        method: 'delete'
    });
}
// 查询调度日志列表
export function listJobLog(query) {
    return request({
        url: '/da/discoveryTask/jobLog/list',
        method: 'get',
        params: query
    });
}

// 数据发现 执行一次
export function startDppEtlTask(data) {
    return request({
        url: '/da/discoveryTask/startDaDiscoveryTask/' + data,
        method: 'put',
    })
}
