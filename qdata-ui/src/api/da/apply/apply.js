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

import request from '@/utils/request.js'

// 查询API服务-申请列表
export function listApply(query) {
  return request({
    url: '/da/apply/list',
    method: 'get',
    params: query
  })
}

// 查询API服务-申请详细
export function getApply(id) {
  return request({
    url: '/da/apply/' + id,
    method: 'get'
  })
}

// 新增API服务-申请
export function addApply(data) {
  return request({
    url: '/da/apply',
    method: 'post',
    data: data
  })
}

// 修改API服务-申请
export function updateApply(data) {
  return request({
    url: '/da/apply',
    method: 'put',
    data: data
  })
}

// 删除API服务-申请
export function delApply(id) {
  return request({
    url: '/da/apply/' + id,
    method: 'delete'
  })
}
