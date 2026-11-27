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

// Query collection range list
export function listTaskScope(query) {
  return request({
    url: '/mc/taskScope/list',
    method: 'get',
    params: query
  })
}

// Query the collection range details
export function getTaskScope(id) {
  return request({
    url: '/mc/taskScope/' + id,
    method: 'get'
  })
}

// Add new collection range
export function addTaskScope(data) {
  return request({
    url: '/mc/taskScope',
    method: 'post',
    data: data
  })
}

// Modify collection range
export function updateTaskScope(data) {
  return request({
    url: '/mc/taskScope',
    method: 'put',
    data: data
  })
}

// Delete collection range
export function delTaskScope(id) {
  return request({
    url: '/mc/taskScope/' + id,
    method: 'delete'
  })
}
