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

// Query the list of audit rules
export function listDgRule(query) {
  return request({
    url: '/dg/auditRule/list',
    method: 'get',
    params: query
  })
}

// Query detailed audit rules
export function getDgRule(id) {
  return request({
    url: '/dg/auditRule/' + id,
    method: 'get'
  })
}

// Add new audit rules
export function addDgRule(data) {
  return request({
    url: '/dg/auditRule',
    method: 'post',
    data: data
  })
}

// Modify audit rules
export function updateDgRule(data) {
  return request({
    url: '/dg/auditRule',
    method: 'put',
    data: data
  })
}

// Delete audit rules
export function delDgRule(id) {
  return request({
    url: '/dg/auditRule/' + id,
    method: 'delete'
  })
}

// tree
export function treeDgRule(params) {
  return request({
    url: '/dg/auditRule/tree',
    method: 'get',
    params
  })
}
