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

// 查询业务分类列表
export function listBusinessCategory(query) {
  return request({
    url: '/dm/businessCategory/list',
    method: 'get',
    params: query
  })
}

// 查询业务分类详细
export function getBusinessCategory(id) {
  return request({
    url: '/dm/businessCategory/' + id,
    method: 'get'
  })
}

// 新增业务分类
export function addBusinessCategory(data) {
  return request({
    url: '/dm/businessCategory',
    method: 'post',
    data: data
  })
}

// 修改业务分类
export function updateBusinessCategory(data) {
  return request({
    url: '/dm/businessCategory',
    method: 'put',
    data: data
  })
}

// 删除业务分类
export function delBusinessCategory(id) {
  return request({
    url: '/dm/businessCategory/' + id,
    method: 'delete'
  })
}
