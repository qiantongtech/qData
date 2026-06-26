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

// 查询应用管理列表
export function listClient(query) {
  return request({
    url: '/att/client/list',
    method: 'get',
    params: query
  })
}

// 查询应用管理详细
export function getClient(id) {
  return request({
    url: '/att/client/' + id,
    method: 'get'
  })
}

// 新增应用管理
export function addClient(data) {
  return request({
    url: '/att/client',
    method: 'post',
    data: data
  })
}

// 修改应用管理
export function updateClient(data) {
  return request({
    url: '/att/client',
    method: 'put',
    data: data
  })
}

// 删除应用管理
export function delClient(id) {
  return request({
    url: '/att/client/' + id,
    method: 'delete'
  })
}

// 重置应用秘钥
export function resetSecret(id) {
  return request({
    url: '/att/client/reset/secret',
    params: {
      id: id
    },
    method: 'post'
  })
}
