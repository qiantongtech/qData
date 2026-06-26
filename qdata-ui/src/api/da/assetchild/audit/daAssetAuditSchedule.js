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

// 查询资产稽查调度列表
export function listDaAssetAuditSchedule(query) {
  return request({
    url: '/da/daAssetAuditSchedule/list',
    method: 'get',
    params: query
  })
}

// 查询资产稽查调度详细
export function getDaAssetAuditSchedule(id) {
  return request({
    url: '/da/daAssetAuditSchedule/' + id,
    method: 'get'
  })
}

// 新增资产稽查调度
export function addDaAssetAuditSchedule(data) {
  return request({
    url: '/da/daAssetAuditSchedule',
    method: 'post',
    data: data
  })
}

// 修改资产稽查调度
export function updateDaAssetAuditSchedule(data) {
  return request({
    url: '/da/daAssetAuditSchedule',
    method: 'put',
    data: data
  })
}

// 删除资产稽查调度
export function delDaAssetAuditSchedule(id) {
  return request({
    url: '/da/daAssetAuditSchedule/' + id,
    method: 'delete'
  })
}
