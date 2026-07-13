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

// Query document data management list
export function listDaDoc(query) {
  return request({
    url: '/da/daDoc/list',
    method: 'get',
    params: query
  })
}

// Query document data management details
export function getDaDoc(id) {
  return request({
    url: '/da/daDoc/' + id,
    method: 'get'
  })
}

// Added document data management
export function addDaDoc(data) {
  return request({
    url: '/da/daDoc',
    method: 'post',
    data: data
  })
}

// Modify document data management
export function updateDaDoc(data) {
  return request({
    url: '/da/daDoc',
    method: 'put',
    data: data
  })
}

// Delete document data management
export function delDaDoc(id) {
  return request({
    url: '/da/daDoc/' + id,
    method: 'delete'
  })
}
