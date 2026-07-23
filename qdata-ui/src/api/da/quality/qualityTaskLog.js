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

// Query the data quality log list
export function listDppQualityLog(query) {
  return request({
    url: '/dpp/qualityLog/list',
    method: 'get',
    params: query
  })
}

// Query data quality log details
export function getDppQualityLog(id) {
  return request({
    url: '/dpp/qualityLog/' + id,
    method: 'get'
  })
}

// Added data quality log
export function addDppQualityLog(data) {
  return request({
    url: '/dpp/qualityTask',
    method: 'post',
    data: data
  })
}

// Modify data quality log
export function updateDppQualityLog(data) {
  return request({
    url: '/dpp/qualityLog',
    method: 'put',
    data: data
  })
}

// Delete data quality logs
export function delDppQualityLog(id) {
  return request({
    url: '/dpp/qualityLog/' + id,
    method: 'delete'
  })
}
//Send message
export function doSendMessage(id) {
  return request({
    url: '/dpp/qualityLog/sendMessage',
    method: 'POST',
    params: {id}
  })
}
// Log data quality dimension statistics
export function statisticsEvaluateOne(id) {
  return request({
    url: '/dpp/evaluateLog/statisticsEvaluateOne/' + id,
    method: 'get',
  })
}
// Log details Change trend of governance data volume
export function statisticsEvaluateTow(query) {
  console.log("🚀 ~ statisticsEvaluateTow ~ query:", query)
  return request({
    url: '/dpp/evaluateLog/statisticsEvaluateTow',
    method: 'get',
    params: query
  })
}
//Log rule list
export function statisticsEvaluateTable(id) {
  return request({
    url: '/dpp/evaluateLog/statisticsEvaluateTable/' + id,
    method: 'get',
  })
}
//  Wrong data
export function pageErrorData(query) {
  return request({
    url: '/dpp/evaluateLog/pageErrorData',
    method: 'get',
    params: query
  })
}
// Modify interface data and status, all are this interface
export function updateErrorData(data) {
  return request({
    url: 'dpp/evaluateLog/updateErrorData',
    method: 'post',
    data: data
  })
}

