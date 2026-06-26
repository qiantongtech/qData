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

import request from '@/utils/request.js'

// 查询数据元类目管理列表
export function listAttDataElemCat(query) {
  return request({
    url: '/att/dataElemCat/list',
    method: 'get',
    params: query
  })
}

// 查询数据元类目管理详细
export function getAttDataElemCat(id) {
  return request({
    url: '/att/dataElemCat/' + id,
    method: 'get'
  })
}

// 新增数据元类目管理
export function addAttDataElemCat(data) {
  return request({
    url: '/att/dataElemCat',
    method: 'post',
    data: data
  })
}

// 修改数据元类目管理
export function updateAttDataElemCat(data) {
  return request({
    url: '/att/dataElemCat',
    method: 'put',
    data: data
  })
}

// 删除数据元类目管理
export function delAttDataElemCat(id) {
  return request({
    url: '/att/dataElemCat/' + id,
    method: 'delete'
  })
}
