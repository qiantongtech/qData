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

// 查询用户类型列表
export function listUserType(query) {
  return request({
    url: '/example/userType/list',
    method: 'get',
    params: query
  })
}

// 查询用户类型详细
export function getUserType(id) {
  return request({
    url: '/example/userType/' + id,
    method: 'get'
  })
}

// 新增用户类型
export function addUserType(data) {
  return request({
    url: '/example/userType',
    method: 'post',
    data: data
  })
}

// 修改用户类型
export function updateUserType(data) {
  return request({
    url: '/example/userType',
    method: 'put',
    data: data
  })
}

// 删除用户类型
export function delUserType(id) {
  return request({
    url: '/example/userType/' + id,
    method: 'delete'
  })
}
