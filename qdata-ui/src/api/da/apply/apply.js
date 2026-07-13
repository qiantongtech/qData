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

// Query API service-application list
export function listApply(query) {
  return request({
    url: '/da/apply/list',
    method: 'get',
    params: query
  })
}

// Query API service-application details
export function getApply(id) {
  return request({
    url: '/da/apply/' + id,
    method: 'get'
  })
}

// New API service-application
export function addApply(data) {
  return request({
    url: '/da/apply',
    method: 'post',
    data: data
  })
}

// Modify API service-application
export function updateApply(data) {
  return request({
    url: '/da/apply',
    method: 'put',
    data: data
  })
}

// Delete API Service-Application
export function delApply(id) {
  return request({
    url: '/da/apply/' + id,
    method: 'delete'
  })
}
