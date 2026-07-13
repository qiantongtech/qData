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

// Query the list of data asset operation records
export function listDaAssetOperateLog(query) {
  return request({
    url: '/da/assetOperateLog/list',
    method: 'get',
    params: query
  })
}

// Query detailed data asset operation records
export function getDaAssetOperateLog(id) {
  return request({
    url: '/da/assetOperateLog/' + id,
    method: 'get'
  })
}

// Add new data asset operation records
export function addDaAssetOperateLog(data) {
  return request({
    url: '/da/assetOperateLog',
    method: 'post',
    data: data
  })
}

// Modify data asset operation records
export function updateDaAssetOperateLog(data) {
  return request({
    url: '/da/assetOperateLog',
    method: 'put',
    data: data
  })
}

// Delete data asset operation records
export function delDaAssetOperateLog(id) {
  return request({
    url: '/da/assetOperateLog/' + id,
    method: 'delete'
  })
}
// rollback
export function rollBack(id) {
  return request({
    url: `/da/assetOperateLog/rollBack/${id}`,
    method: 'post'
  })
}


// Modify records
export function getDaAssetList(query) {
  return request({
    url: '/da/assetOperateLog/queryDaAssetOperateLogPage',
    method: 'get',
    params: query
  })
}
// // rollback
// export function rollBack(id) {
//   return request({
//     url: `/da/assetOperateLog/rollBack/${id}`,
//     method: 'get',
//   })
// }
