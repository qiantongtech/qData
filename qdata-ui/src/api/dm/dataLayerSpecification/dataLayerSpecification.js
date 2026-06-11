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
