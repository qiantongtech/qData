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

// 查询数据分级列表
export function listDataLevel(query) {
  return request({
    url: '/dg/dataLevel/list',
    method: 'get',
    params: query
  })
}

// 查询所有数据分级下拉列表
export function listAllDataLevel(query) {
  return request({
    url: '/dg/dataLevel/listAll',
    method: 'get',
    params: query
  })
}

// 查询数据分级详细
export function getDataLevel(id) {
  return request({
    url: '/dg/dataLevel/' + id,
    method: 'get'
  })
}

// 新增数据分级
export function addDataLevel(data) {
  return request({
    url: '/dg/dataLevel',
    method: 'post',
    data: data
  })
}

// 修改数据分级
export function updateDataLevel(data) {
  return request({
    url: '/dg/dataLevel',
    method: 'put',
    data: data
  })
}

// 删除数据分级
export function delDataLevel(id) {
  return request({
    url: '/dg/dataLevel/' + id,
    method: 'delete'
  })
}
