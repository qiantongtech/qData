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

// Query data asset quality result record list
export function listDaAssetAuditRule(query) {
  return request({
    url: '/da/daAssetAuditRule/list',
    method: 'get',
    params: query
  })
}

// Query data asset quality result record details
export function getDaAssetAuditRule(id) {
  return request({
    url: '/da/daAssetAuditRule/' + id,
    method: 'get'
  })
}

// Added new data asset quality result record
export function addDaAssetAuditRule(data) {
  return request({
    url: '/da/daAssetAuditRule',
    method: 'post',
    data: data
  })
}

// Modify data asset quality result records
export function updateDaAssetAuditRule(data) {
  return request({
    url: '/da/daAssetAuditRule',
    method: 'put',
    data: data
  })
}

// Delete data asset quality result records
export function delDaAssetAuditRule(id) {
  return request({
    url: '/da/daAssetAuditRule/' + id,
    method: 'delete'
  })
}
