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

// Query business classification list
export function listBusinessCategory(query) {
  return request({
    url: '/dm/businessCategory/list',
    method: 'get',
    params: query
  })
}

// Query business classification details
export function getBusinessCategory(id) {
  return request({
    url: '/dm/businessCategory/' + id,
    method: 'get'
  })
}

// Add new business category
export function addBusinessCategory(data) {
  return request({
    url: '/dm/businessCategory',
    method: 'post',
    data: data
  })
}

// Modify business classification
export function updateBusinessCategory(data) {
  return request({
    url: '/dm/businessCategory',
    method: 'put',
    data: data
  })
}

// Delete business category
export function delBusinessCategory(id) {
  return request({
    url: '/dm/businessCategory/' + id,
    method: 'delete'
  })
}
