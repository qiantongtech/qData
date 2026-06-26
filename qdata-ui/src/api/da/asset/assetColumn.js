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

// 查询数据资产字段列表
export function listDaAssetColumn(query) {
  return request({
    url: '/da/assetColumn/list',
    method: 'get',
    params: query
  })
}

// 查询数据资产字段详细
export function getDaAssetColumn(id) {
  return request({
    url: '/da/assetColumn/' + id,
    method: 'get'
  })
}

// 新增数据资产字段
export function addDaAssetColumn(data) {
  return request({
    url: '/da/assetColumn',
    method: 'post',
    data: data
  })
}

// 修改数据资产字段
export function updateDaAssetColumn(data) {
  return request({
    url: '/da/assetColumn',
    method: 'put',
    data: data
  })
}

// 删除数据资产字段
export function delDaAssetColumn(id) {
  return request({
    url: '/da/assetColumn/' + id,
    method: 'delete'
  })
}
// 預覽

export function preview(data) {
  return request({
    url: '/da/asset/preview',
    method: 'post',
    data: data
  })
}

export function pageListByIds(query) {
  return request({
    url: '/da/assetColumn/listAssetColumnTerm',
    method: 'get',
    params: query
  });
}
