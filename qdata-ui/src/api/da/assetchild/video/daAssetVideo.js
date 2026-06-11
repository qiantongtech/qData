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

// 查询数据资产-视频数据列表
export function listDaAssetVideo(query) {
  return request({
    url: '/da/daAssetVideo/list',
    method: 'get',
    params: query
  })
}

// 查询数据资产-视频数据详细
export function getDaAssetVideo(id) {
  return request({
    url: '/da/daAssetVideo/' + id,
    method: 'get'
  })
}

// 新增数据资产-视频数据
export function addDaAssetVideo(data) {
  return request({
    url: '/da/daAssetVideo',
    method: 'post',
    data: data
  })
}

// 修改数据资产-视频数据
export function updateDaAssetVideo(data) {
  return request({
    url: '/da/daAssetVideo',
    method: 'put',
    data: data
  })
}

// 删除数据资产-视频数据
export function delDaAssetVideo(id) {
  return request({
    url: '/da/daAssetVideo/' + id,
    method: 'delete'
  })
}
