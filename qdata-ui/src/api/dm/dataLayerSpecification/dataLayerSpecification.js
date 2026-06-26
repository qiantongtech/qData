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

// 查询数仓分层-规范管理列表
export function listDataLayerSpecification(query) {
  return request({
    url: '/dm/dataLayerSpecification/list',
    method: 'get',
    params: query
  })
}

// 查询数仓分层-规范管理详细
export function getDataLayerSpecification(id) {
  return request({
    url: '/dm/dataLayerSpecification/' + id,
    method: 'get'
  })
}

// 新增数仓分层-规范管理
export function addDataLayerSpecification(data) {
  return request({
    url: '/dm/dataLayerSpecification',
    method: 'post',
    data: data
  })
}

// 修改数仓分层-规范管理
export function updateDataLayerSpecification(data) {
  return request({
    url: '/dm/dataLayerSpecification',
    method: 'put',
    data: data
  })
}

// 删除数仓分层-规范管理
export function delDataLayerSpecification(id) {
  return request({
    url: '/dm/dataLayerSpecification/' + id,
    method: 'delete'
  })
}
