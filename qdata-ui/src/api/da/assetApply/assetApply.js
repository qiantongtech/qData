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

// Query data asset application list
export function listDaAssetApply(query) {
  return request({
    url: '/da/assetApply/list',
    method: 'get',
    params: query
  })
}

// Query data asset application details
export function getDaAssetApply(id) {
  return request({
    url: '/da/assetApply/' + id,
    method: 'get'
  })
}

// New data asset application
export function addDaAssetApply(data) {
  return request({
    url: '/da/assetApply',
    method: 'post',
    data: data
  })
}

// Modify data asset application
export function updateDaAssetApply(data) {
  return request({
    url: '/da/assetApply',
    method: 'put',
    data: data
  })
}

// Delete data asset request
export function delDaAssetApply(id) {
  return request({
    url: '/da/assetApply/' + id,
    method: 'delete'
  })
}
// Request
export function queryServiceForwarding(data) {
  return request({
    url: '/da/api/queryServiceForwarding',
    method: 'post',
    data: data
  })
}
