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

// Query collection task list
export function listTask(query) {
  return request({
    url: '/mc/task/list',
    method: 'get',
    params: query
  })
}

// Query collection task details
export function getTask(id) {
  return request({
    url: '/mc/task/' + id,
    method: 'get'
  })
}

// Add new collection tasks
export function addTask(data) {
  return request({
    url: '/mc/task',
    method: 'post',
    data: data
  })
}

// Modify collection tasks
export function updateTask(data) {
  return request({
    url: '/mc/task',
    method: 'put',
    data: data
  })
}

// Delete collection task
export function delTask(id) {
  return request({
    url: '/mc/task/' + id,
    method: 'delete'
  })
}

// Get collection range
export function getRealtimeMcTaskScopeList(id) {
  return request({
    url: '/mc/task/getRealtimeMcTaskScopeList/' + id,
    method: 'get'
  })
}

// Task online and offline
export function updateReleaseJobTask(data) {
  return request({
    url: 'mc/task/updateReleaseJobTask',
    method: 'post',
    data
  })
}

// Scheduling online and offline
export function updateReleaseSchedule(data) {
  return request({
    url: 'mc/task/updateReleaseSchedule',
    method: 'post',
    data
  })
}

//Execute once
export function runJobOnce(data) {
  return request({
    url: `/mc/task/runJobOnce`,
    method: 'post',
    data
  })
}

// Get source system tree
export function sourceSystemTree(query) {
  return request({
    url: '/mc/task/sourceSystemTree',
    method: 'get',
    params: query
  })
}
// Delete collection tasks in batches
export function batchDeleteCheck(id) {
  return request({
    url: '/mc/task/batchDeleteCheck/' + id,
    method: 'get'
  });
}
