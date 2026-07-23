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

// Query data discovery task list
export function listDaDiscoveryTask(query) {
    return request({
        url: '/da/discoveryTask/getDaDiscoveryTaskListPage',
        method: 'get',
        params: query
    });
}

// Query data discovery task details
export function getDaDiscoveryTask(id) {
    return request({
        url: '/da/discoveryTask/' + id,
        method: 'get'
    });
}

// Add new data discovery task
export function addDaDiscoveryTask(data) {
    return request({
        url: '/da/discoveryTask',
        method: 'post',
        data: data
    });
}

// Modify data discovery tasks
export function updateDaDiscoveryTask(data) {
    return request({
        url: '/da/discoveryTask',
        method: 'put',
        data: data
    });
}
// Modify data discovery tasks
export function updateDaDiscoveryTaskStatus(data) {
    return request({
        url: '/da/discoveryTask/updateDaDiscoveryTaskStatus',
        method: 'post',
        data: data
    });
}
// Modify data discovery tasks
export function updateDaDiscoveryTaskCronExpression(data) {
    return request({
        url: '/da/discoveryTask/updateDaDiscoveryTaskCronExpression',
        method: 'post',
        data: data
    });
}

// Delete data discovery tasks
export function delDaDiscoveryTask(id) {
    return request({
        url: '/da/discoveryTask/' + id,
        method: 'delete'
    });
}
// Query scheduling log list
export function listJobLog(query) {
    return request({
        url: '/da/discoveryTask/jobLog/list',
        method: 'get',
        params: query
    });
}

// Data discovery Execute once
export function startDppEtlTask(data) {
    return request({
        url: '/da/discoveryTask/startDaDiscoveryTask/' + data,
        method: 'put',
    })
}
