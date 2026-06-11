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

// 查询数据集成任务列表
export function listDppEtlTask(query) {
    return request({
        url: '/dpp/etlTask/getDppEtlTaskPage',
        method: 'get',
        params: query
    })
}

// 查询数据集成任务详细
export function getDppEtlTask(id) {
    return request({
        url: '/dpp/etlTask/' + id,
        method: 'get'
    })
}

// 新增数据集成任务
export function addDppEtlTask(data) {
    return request({
        url: '/dpp/etlTask',
        method: 'post',
        data: data
    })
}

// 修改数据集成任务
export function updateDppEtlTask(data) {
    return request({
        url: '/dpp/etlTask',
        method: 'put',
        data: data
    })
}

// 删除数据集成任务
export function delDppEtlTask(id) {
    console.log("🚀 ~ delDppEtlTask ~ id:", id)
    return request({
        url: '/dpp/etlTask/' + id,
        method: 'delete'
    })
}
// 表列表
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
// 表字段
export function getColumnByAssetId(data) {
    return request({
        url: `/da/dataSource/columnsAsAssetColumnList`,
        method: 'post',
        data: data
    });
}
// 表字段
// export function getColumnByAssetId(query) {
//   return request({
//     url: '/da/daAssetColumn/getColumnByAssetId',
//     method: 'get',
//     params: query
//   })
// }
// code获取
export function getNodeUniqueKey(query) {
    return request({
        url: '/dpp/etlTask/getNodeUniqueKey',
        method: 'get',
        params: query
    })
}
// code获取
export function getCleaningRuleTree(query) {
    return request({
        url: '/att/cleanRule/getCleaningRuleTree',
        method: 'get',
        params: query
    })
}
// code获取
export function createTaskTempTable(data) {
    return request({
        url: '/da/dataSource/createTaskTempTable',
        method: 'post',
        data: data
    })
}
// 新增接口 dag
export function createProcessDefinition(data) {
    return request({
        url: '/dpp/etlTask/createProcessDefinitionEx',
        method: 'post',
        data: data
    })
}
// 新增接口 数据集成dag
export function createProcessDefinitions(data) {
    return request({
        // url: '/dpp/etlTask/createProcessDefinitionEx',
        url: '/dpp/etlTask/createEtlTask',
        method: 'post',
        data: data
    })
}
// 新增接口 数据集成dag
export function createEtlTaskFrontPostposition(data) {
    return request({
        url: '/dpp/etlTask/createEtlTaskFrontPostposition',
        method: 'post',
        data: data
    })
}
// 上线下线
export function updateReleaseTask(data) {
    return request({
        url: '/dpp/etlTask/updateReleaseTask',
        method: 'post',
        data: data
    })
}
// 上线下线 調度
export function updateReleaseSchedule(data) {
    return request({
        url: '/dpp/etlTask/updateReleaseSchedule',
        method: 'post',
        data: data
    })
}
// 上线下线 任務
export function updateReleaseJobTask(data) {
    return request({
        url: '/dpp/etlTask/updateReleaseJobTask',
        method: 'post',
        data: data
    })
}

// 详情
export function etlTask(id) {
    return request({
        url: '/dpp/etlTask/updateQuery/' + id,
        method: 'get',

    })
}

// 修改
export function updateProcessDefinition(data) {
    return request({
        url: '/dpp/etlTask/updateProcessDefinition',
        method: 'post',
        data: data
    })
}
// 数据集成修改
export function updateProcessDefinitions(data, query) {
    return request({
        // url: '/dpp/etlTask/updateProcessDefinition',
        url: '/dpp/etlTask/updateEtlTask',
        method: 'post',
        data: data,
        params: query
    })
}
// 修改调度
export function releaseTaskCrontab(data) {
    return request({
        url: '/dpp/etlTask/releaseTaskCrontab',
        method: 'post',
        data: data
    })
}

// 查询作业任务 树形
export function getDppEtlTaskListTree(query) {
    return request({
        url: '/dpp/etlTask/getDppEtlTaskListTree',
        method: 'get',
        params: query
    })
}

// 解析exel
export function getExcelColumn(data) {
    return request({
        url: '/common/getExcelColumn ',
        method: 'post',
        data: data
    })
}
//

// 表code获取
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

// 数据研发 执行一次
export function startDppEtlTask(data) {
    return request({
        url: '/dpp/etlTask/startDppEtlTask/' + data,
        method: 'put',
    })
}

// 新增任务
export function createEtlTaskFront(data) {
    return request({
        url: '/dpp/etlTask/createEtlTaskFront',
        method: 'post',
        data: data
    })
}

// 使用模板
export function dppEtlSqlTemp(query) {
    return request({
        url: '/dpp/etlSqlTemp/list',
        method: 'get',
        params: query
    })
}

// 获取实例id
export function getRunTaskInstance(query) {
    return request({
        url: '/dpp/etlTaskInstance/getRunTaskInstance',
        method: 'get',
        params: query
    })
}

// 获取控制台日志
export function getLogByTaskInstanceId(query) {
    return request({
        url: '/dpp/etlTaskInstance/getLogByTaskInstanceId',
        method: 'get',
        params: query
    })
}



// api输入组件-从结果JSON中自动分析
export function getResponseColumnReqVO(data) {
    return request({
        url: '/common/getResponseColumnReqVO',
        method: 'post',
        data: data
    })
}
// 日志获取节点详情
export function getTaskInfo(query) {
    return request({
        url: '/dpp/etlTaskInstance/getTaskInfo/' + query,
        method: 'get',
    })
}
// 克隆任务
export function copyCreateEtl(data) {
    return request({
        url: '/dpp/etlTask/copyCreateEtl',
        method: 'post',
        data: data
    })
}
// 执行命令
export function execute(taskInstanceId, executeType) {
    return request({
        url: `/dpp/etlExecutors/execute/${taskInstanceId}/${executeType}`,
        method: 'post'
    })
}
