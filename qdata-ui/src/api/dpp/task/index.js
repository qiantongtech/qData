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

import request from '@/utils/request'

// Query the data integration task list
export function listDppEtlTask(query) {
    return request({
        url: '/dpp/etlTask/getDppEtlTaskPage',
        method: 'get',
        params: query
    })
}

// Query data integration task details
export function getDppEtlTask(id) {
    return request({
        url: '/dpp/etlTask/' + id,
        method: 'get'
    })
}

// Add new data integration task
export function addDppEtlTask(data) {
    return request({
        url: '/dpp/etlTask',
        method: 'post',
        data: data
    })
}

// Modify data integration tasks
export function updateDppEtlTask(data) {
    return request({
        url: '/dpp/etlTask',
        method: 'put',
        data: data
    })
}

// Delete data integration tasks
export function delDppEtlTask(id) {
    console.log("🚀 ~ delDppEtlTask ~ id:", id)
    return request({
        url: '/dpp/etlTask/' + id,
        method: 'delete'
    })
}
// table list
// export function getTablesByDataSourceId(query) {
//   return request({
//     url: '/da/daAsset/getTablesByDataSourceId',
//     method: 'get',
//     params: query
//   })
// }
export function getTablesByDataSourceId(ID) {
    console.log("🚀 ~ tableList ~ ID:", ID)
    return request({
        url: '/da/dataSource/tableList/' + ID.datasourceId,
        method: 'get'
    });
}
// table fields
export function getColumnByAssetId(data) {
    return request({
        url: `/da/dataSource/columnsAsAssetColumnList`,
        method: 'post',
        data: data
    });
}
// table fields
// export function getColumnByAssetId(query) {
//   return request({
//     url: '/da/daAssetColumn/getColumnByAssetId',
//     method: 'get',
//     params: query
//   })
// }
// code acquisition
export function getNodeUniqueKey(query) {
    return request({
        url: '/dpp/etlTask/getNodeUniqueKey',
        method: 'get',
        params: query
    })
}
// code acquisition
export function getCleaningRuleTree(query) {
    return request({
        url: '/att/cleanRule/getCleaningRuleTree',
        method: 'get',
        params: query
    })
}
// code acquisition
export function createTaskTempTable(data) {
    return request({
        url: '/da/dataSource/createTaskTempTable',
        method: 'post',
        data: data
    })
}
// Add interface dag
export function createProcessDefinition(data) {
    return request({
        url: '/dpp/etlTask/createProcessDefinitionEx',
        method: 'post',
        data: data
    })
}
// New interface data integration dag
export function createProcessDefinitions(data) {
    return request({
        // url: '/dpp/etlTask/createProcessDefinitionEx',
        url: '/dpp/etlTask/createEtlTask',
        method: 'post',
        data: data
    })
}
// New interface data integration dag
export function createEtlTaskFrontPostposition(data) {
    return request({
        url: '/dpp/etlTask/createEtlTaskFrontPostposition',
        method: 'post',
        data: data
    })
}
// Online and offline
export function updateReleaseTask(data) {
    return request({
        url: '/dpp/etlTask/updateReleaseTask',
        method: 'post',
        data: data
    })
}
// Online and offline scheduling
export function updateReleaseSchedule(data) {
    return request({
        url: '/dpp/etlTask/updateReleaseSchedule',
        method: 'post',
        data: data
    })
}
// Online and offline tasks
export function updateReleaseJobTask(data) {
    return request({
        url: '/dpp/etlTask/updateReleaseJobTask',
        method: 'post',
        data: data
    })
}

// Details
export function etlTask(id) {
    return request({
        url: '/dpp/etlTask/updateQuery/' + id,
        method: 'get',

    })
}

// Modify
export function updateProcessDefinition(data) {
    return request({
        url: '/dpp/etlTask/updateProcessDefinition',
        method: 'post',
        data: data
    })
}
// Data integration modifications
export function updateProcessDefinitions(data, query) {
    return request({
        // url: '/dpp/etlTask/updateProcessDefinition',
        url: '/dpp/etlTask/updateEtlTask',
        method: 'post',
        data: data,
        params: query
    })
}
// Modify schedule
export function releaseTaskCrontab(data) {
    return request({
        url: '/dpp/etlTask/releaseTaskCrontab',
        method: 'post',
        data: data
    })
}

// Query job task tree
export function getDppEtlTaskListTree(query) {
    return request({
        url: '/dpp/etlTask/getDppEtlTaskListTree',
        method: 'get',
        params: query
    })
}

// Parse exel
export function getExcelColumn(data) {
    return request({
        url: '/common/getExcelColumn ',
        method: 'post',
        data: data
    })
}
//

// Table code acquisition
export function createTaskTempTableByExcel(data) {
    return request({
        url: '/da/dataSource/createTaskTempTableByExcel',
        method: 'post',
        data: data
    })
}

export function createTaskTempTableByExcel2(data) {
    return request({
        url: 'da/dataSource/createTaskTempTable/2',
        method: 'post',
        data: data
    })
}

export function getDaDatasourceList(query) {
    return request({
        url: '/da/dataSource/getDaDatasourceList',
        method: 'get',
        params: query
    })
}
// jiexi csv
export function getCsvColumn(data) {
    return request({
        url: '/common/getCsvColumn',
        method: 'post',
        data: data
    })
}

// Data R&D Execute once
export function startDppEtlTask(data) {
    return request({
        url: '/dpp/etlTask/startDppEtlTask/' + data,
        method: 'put',
    })
}

// Add new task
export function createEtlTaskFront(data) {
    return request({
        url: '/dpp/etlTask/createEtlTaskFront',
        method: 'post',
        data: data
    })
}

// Use templates
export function dppEtlSqlTemp(query) {
    return request({
        url: '/dpp/etlSqlTemp/list',
        method: 'get',
        params: query
    })
}

// Get instance id
export function getRunTaskInstance(query) {
    return request({
        url: '/dpp/etlTaskInstance/getRunTaskInstance',
        method: 'get',
        params: query
    })
}

// Get console log
export function getLogByTaskInstanceId(query) {
    return request({
        url: '/dpp/etlTaskInstance/getLogByTaskInstanceId',
        method: 'get',
        params: query
    })
}

// api input component - automatic parsing from resulting JSON
export function getResponseColumnReqVO(data) {
    return request({
        url: '/common/getResponseColumnReqVO',
        method: 'post',
        data: data
    })
}
// Log gets node details
export function getTaskInfo(query) {
    return request({
        url: '/dpp/etlTaskInstance/getTaskInfo/' + query,
        method: 'get',
    })
}
// Clone task
export function copyCreateEtl(data) {
    return request({
        url: '/dpp/etlTask/copyCreateEtl',
        method: 'post',
        data: data
    })
}
// execute command
export function execute(taskInstanceId, executeType) {
    return request({
        url: `/dpp/etlExecutors/execute/${taskInstanceId}/${executeType}`,
        method: 'post'
    })
}

// Get data integration task statistics
export function getEtlTaskStatistics(query) {
    return request({
        url: '/dpp/etlTask/statistics',
        method: 'get',
        params: query
    })
}

// Get data integration task instance statistics
export function getEtlTaskInstanceStatistics(query) {
    return request({
        url: '/dpp/etlTaskInstance/statistics',
        method: 'get',
        params: query
    })
}

// Get data integration task instance list
export function getEtlTaskInstanceList(query) {
    return request({
        url: '/dpp/etlTaskInstance/list',
        method: 'get',
        params: query
    })
}
