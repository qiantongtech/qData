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

// 查询数据分类列表
export function listDataCategory(query) {
  return request({
    url: '/dg/dataCategory/list',
    method: 'get',
    params: query
  })
}

export function listDataCategoryAll(query) {
  return request({
    url: '/dg/dataCategory/listAll',
    method: 'get',
    params: query
  })
}


// 查询数据分类详细
export function getDataCategory(id) {
  return request({
    url: '/dg/dataCategory/' + id,
    method: 'get'
  })
}

// 查询数据分类树
export function selectTreeDataCategory(query) {
  return request({
    url: '/dg/dataCategory/selectTree',
    method: 'get',
    params: query
  })
}

// 新增数据分类
export function addDataCategory(data) {
  return request({
    url: '/dg/dataCategory',
    method: 'post',
    data: data
  })
}

// 修改数据分类
export function updateDataCategory(data) {
  return request({
    url: '/dg/dataCategory',
    method: 'put',
    data: data
  })
}

// 删除数据分类
export function delDataCategory(id) {
  return request({
    url: '/dg/dataCategory/' + id,
    method: 'delete'
  })
}

// 批量定级
export function batchDataLevel(data) {
  return request({
    url: '/dg/dataCategory/batchDataLevel',
    method: 'PUT',
    data: data
  })
}
