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

// 查询应用管理列表
export function listClient(query) {
  return request({
    url: '/auth/client/list',
    method: 'get',
    params: query
  })
}

// 查询应用管理详细
export function getClient(id) {
  return request({
    url: '/auth/client/' + id,
    method: 'get'
  })
}

// 新增应用管理
export function addClient(data) {
  return request({
    url: '/auth/client',
    method: 'post',
    data: data
  })
}

// 修改应用管理
export function updateClient(data) {
  return request({
    url: '/auth/client',
    method: 'put',
    data: data
  })
}

// 删除应用管理
export function delClient(id) {
  return request({
    url: '/auth/client/' + id,
    method: 'delete'
  })
}
