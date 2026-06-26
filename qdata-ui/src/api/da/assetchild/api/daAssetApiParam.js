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

// 查询数据资产-外部API-参数列表
export function listDaAssetApiParam(query) {
  return request({
    url: '/da/daAssetApiParam/list',
    method: 'get',
    params: query
  })
}

// 查询数据资产-外部API-参数详细
export function getDaAssetApiParam(id) {
  return request({
    url: '/da/daAssetApiParam/' + id,
    method: 'get'
  })
}

// 新增数据资产-外部API-参数
export function addDaAssetApiParam(data) {
  return request({
    url: '/da/daAssetApiParam',
    method: 'post',
    data: data
  })
}

// 修改数据资产-外部API-参数
export function updateDaAssetApiParam(data) {
  return request({
    url: '/da/daAssetApiParam',
    method: 'put',
    data: data
  })
}

// 删除数据资产-外部API-参数
export function delDaAssetApiParam(id) {
  return request({
    url: '/da/daAssetApiParam/' + id,
    method: 'delete'
  })
}
