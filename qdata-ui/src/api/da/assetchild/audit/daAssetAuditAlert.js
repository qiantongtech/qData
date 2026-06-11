/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 */

import request from '@/utils/request'

// 查询数据资产-质量预警列表
export function listDaAssetAuditAlert(query) {
  return request({
    url: '/da/daAssetAuditAlert/list',
    method: 'get',
    params: query
  })
}

// 查询数据资产-质量预警详细
export function getDaAssetAuditAlert(id) {
  return request({
    url: '/da/daAssetAuditAlert/' + id,
    method: 'get'
  })
}

// 新增数据资产-质量预警
export function addDaAssetAuditAlert(data) {
  return request({
    url: '/da/daAssetAuditAlert',
    method: 'post',
    data: data
  })
}

// 修改数据资产-质量预警
export function updateDaAssetAuditAlert(data) {
  return request({
    url: '/da/daAssetAuditAlert',
    method: 'put',
    data: data
  })
}

// 删除数据资产-质量预警
export function delDaAssetAuditAlert(id) {
  return request({
    url: '/da/daAssetAuditAlert/' + id,
    method: 'delete'
  })
}
