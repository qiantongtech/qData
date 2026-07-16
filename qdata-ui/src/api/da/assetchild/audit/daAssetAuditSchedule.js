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

// Query the asset audit schedule list
export function listDaAssetAuditSchedule(query) {
  return request({
    url: '/da/daAssetAuditSchedule/list',
    method: 'get',
    params: query
  })
}

// Query asset audit schedule details
export function getDaAssetAuditSchedule(id) {
  return request({
    url: '/da/daAssetAuditSchedule/' + id,
    method: 'get'
  })
}

// Added asset audit schedule
export function addDaAssetAuditSchedule(data) {
  return request({
    url: '/da/daAssetAuditSchedule',
    method: 'post',
    data: data
  })
}

// Modify asset audit schedule
export function updateDaAssetAuditSchedule(data) {
  return request({
    url: '/da/daAssetAuditSchedule',
    method: 'put',
    data: data
  })
}

// Delete asset audit schedule
export function delDaAssetAuditSchedule(id) {
  return request({
    url: '/da/daAssetAuditSchedule/' + id,
    method: 'delete'
  })
}
