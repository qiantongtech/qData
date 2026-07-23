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

// Query data asset operation application list
export function listDaAssetOperateApply(query) {
  return request({
    url: '/da/daAssetOperateApply/list',
    method: 'get',
    params: query
  })
}

// Query data asset operation application details
export function getDaAssetOperateApply(id) {
  return request({
    url: '/da/daAssetOperateApply/' + id,
    method: 'get'
  })
}

// Add new data asset operation application
export function addDaAssetOperateApply(data) {
  return request({
    url: '/da/daAssetOperateApply',
    method: 'post',
    data: data
  })
}

// Modify data asset operation application
export function updateDaAssetOperateApply(data) {
  return request({
    url: '/da/daAssetOperateApply',
    method: 'put',
    data: data
  })
}

// Delete data asset operation application
export function delDaAssetOperateApply(id) {
  return request({
    url: '/da/daAssetOperateApply/' + id,
    method: 'delete'
  })
}
