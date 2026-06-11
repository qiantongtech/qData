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

// 查询数据资产-地理空间服务列表
export function listDaAssetGis(query) {
  return request({
    url: '/da/daAssetGis/list',
    method: 'get',
    params: query
  })
}

// 查询数据资产-地理空间服务详细
export function getDaAssetGis(id) {
  return request({
    url: '/da/daAssetGis/' + id,
    method: 'get'
  })
}

// 新增数据资产-地理空间服务
export function addDaAssetGis(data) {
  return request({
    url: '/da/daAssetGis',
    method: 'post',
    data: data
  })
}

// 修改数据资产-地理空间服务
export function updateDaAssetGis(data) {
  return request({
    url: '/da/daAssetGis',
    method: 'put',
    data: data
  })
}

// 删除数据资产-地理空间服务
export function delDaAssetGis(id) {
  return request({
    url: '/da/daAssetGis/' + id,
    method: 'delete'
  })
}
