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

// Query data quality task list
export function listDppQualityTask(query) {
  return request({
    url: '/dpp/qualityTask/list',
    method: 'get',
    params: query
  })
}

// Query data quality task details
export function getDppQualityTask(id) {
  return request({
    url: '/dpp/qualityTask/' + id,
    method: 'get'
  })
}

// Add new data quality tasks
export function addDppQualityTask(data) {
  return request({
    url: '/dpp/qualityTask',
    method: 'post',
    data: data
  })
}

// Modify data quality tasks
export function updateDppQualityTask(data) {
  return request({
    url: '/dpp/qualityTask',
    method: 'put',
    data: data
  })
}

// Delete data quality tasks
export function delDppQualityTask(id) {
  return request({
    url: '/dpp/qualityTask/' + id,
    method: 'delete'
  })
}


//Check interface
export function verifyInterfaceValue(query) {
  return request({
    url: '/dpp/qualityTaskEvaluate/verifyInterfaceValue',
    method: 'get',
    params: query
  })
}
//Error spot check function
export function validationErrorDataSql(data) {
  return request({
    url: '/dpp/qualityTaskEvaluate/validationErrorDataSql',
    method: 'post',
    data: data
  })
}
// Successful spot check function
export function validationValidDataSql(data) {
  return request({
    url: '/dpp/qualityTaskEvaluate/validationValidDataSql',
    method: 'post',
    data: data
  })
}
//Execute once
export function startDppQualityTask(id) {
  return request({
    url: `/dpp/qualityTask//startDppQualityTask/${id}`,
    method: 'put',
  })
}
// task switch
export function updateDppQualityTaskStatus(query) {
  return request({
    url: '/dpp/qualityTask/updateDppQualityTaskStatus',
    method: 'post',
    data: query
  })
}

// Scheduling cycle

export function updateDaDiscoveryTaskCronExpression(query) {
  return request({
    url: '/dpp/qualityTask/updateDaDiscoveryTaskCronExpression',
    method: 'post',
    data: query
  })
}// Data Quality Query asset quality details

export function getQualityTaskAsset(query) {
  return request({
    url: '/dpp/qualityTask/getQualityTaskAsset',
    method: 'get',
    params: query
  });
}
// Data quality Log data quality dimension statistics

export function statisticsEvaluateAssetOne(query) {
  return request({
    url: 'dpp/evaluateLog/statisticsEvaluateAssetOne',
    method: 'get',
    params: query
  });
}
// View log

export function qualityLogLogDetailCat(query) {
  return request({
    url: '/dpp/qualityLog/logDetailCat',
    method: 'get',
    params: query
  });
}
