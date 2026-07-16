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

// Query the data integration task category management list
export function listAttTaskCat(query) {
  return request({
    url: '/att/taskCat/list',
    method: 'get',
    params: query
  })
}

// Query data integration task category management details
export function getAttTaskCat(id) {
  return request({
    url: '/att/taskCat/' + id,
    method: 'get'
  })
}

// Added data integration task category management
export function addAttTaskCat(data) {
  return request({
    url: '/att/taskCat',
    method: 'post',
    data: data
  })
}

// Modify data integration task category management
export function updateAttTaskCat(data) {
  return request({
    url: '/att/taskCat',
    method: 'put',
    data: data
  })
}

// Delete data integration task category management
export function delAttTaskCat(id) {
  return request({
    url: '/att/taskCat/' + id,
    method: 'delete'
  })
}
