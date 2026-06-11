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
