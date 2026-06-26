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
