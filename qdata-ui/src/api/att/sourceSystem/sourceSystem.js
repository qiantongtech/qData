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

// Query source system list
export function listSourceSystem(query) {
  return request({
    url: '/att/sourceSystem/list',
    method: 'get',
    params: query
  })
}

// Query the list of all source systems
export function listValidSourceSystem(query) {
  return request({
    url: '/att/sourceSystem/listValid',
    method: 'get',
    params: query
  })
}

// Query source system details
export function getSourceSystem(id) {
  return request({
    url: '/att/sourceSystem/' + id,
    method: 'get'
  })
}

// Add source system
export function addSourceSystem(data) {
  return request({
    url: '/att/sourceSystem',
    method: 'post',
    data: data
  })
}

// Modify source system
export function updateSourceSystem(data) {
  return request({
    url: '/att/sourceSystem',
    method: 'put',
    data: data
  })
}

// Delete source system
export function delSourceSystem(id) {
  return request({
    url: '/att/sourceSystem/' + id,
    method: 'delete'
  })
}
