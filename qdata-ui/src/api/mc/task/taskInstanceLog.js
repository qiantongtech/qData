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
 * Business License: https://docs.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

import request from '@/utils/request'

// Query collection task instance-log list
export function listTaskInstanceLog(query) {
  return request({
    url: '/mc/taskInstanceLog/list',
    method: 'get',
    params: query
  })
}

// Query collection task instance-log details
export function getTaskInstanceLog(taskInstanceId) {
  return request({
    url: '/mc/taskInstanceLog/' + taskInstanceId,
    method: 'get'
  })
}

// Added new collection task instance-log
export function addTaskInstanceLog(data) {
  return request({
    url: '/mc/taskInstanceLog',
    method: 'post',
    data: data
  })
}

// Modify the collection task instance-log
export function updateTaskInstanceLog(data) {
  return request({
    url: '/mc/taskInstanceLog',
    method: 'put',
    data: data
  })
}

// Delete collection task instance-log
export function delTaskInstanceLog(taskInstanceId) {
  return request({
    url: '/mc/taskInstanceLog/' + taskInstanceId,
    method: 'delete'
  })
}
