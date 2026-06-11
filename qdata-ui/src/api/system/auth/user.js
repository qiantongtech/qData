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

// 查询应用和用户关联列表
export function listUser(query) {
  return request({
    url: '/auth/user/list',
    method: 'get',
    params: query
  })
}

// 查询应用和用户关联详细
export function getUser(clientId) {
  return request({
    url: '/auth/user/' + clientId,
    method: 'get'
  })
}

// 新增应用和用户关联
export function addUser(data) {
  return request({
    url: '/auth/user',
    method: 'post',
    data: data
  })
}

// 修改应用和用户关联
export function updateUser(data) {
  return request({
    url: '/auth/user',
    method: 'put',
    data: data
  })
}

// 删除应用和用户关联
export function delUser(clientId) {
  return request({
    url: '/auth/user/' + clientId,
    method: 'delete'
  })
}
