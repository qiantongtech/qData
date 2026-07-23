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

import request from '@/utils/request.js'

// Query certificate management list
export function listCert(query) {
  return request({
    url: '/ca/cert/list',
    method: 'get',
    params: query
  })
}

// Query certificate management details
export function getCert(id) {
  return request({
    url: '/ca/cert/' + id,
    method: 'get'
  })
}

// Added certificate management
export function addCert(data) {
  return request({
    url: '/ca/cert',
    method: 'post',
    data: data
  })
}

// Modify certificate management
export function updateCert(data) {
  return request({
    url: '/ca/cert',
    method: 'put',
    data: data
  })
}

// Remove certificate management
export function delCert(id) {
  return request({
    url: '/ca/cert/' + id,
    method: 'delete'
  })
}
