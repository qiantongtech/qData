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

// 查询数据分类-类目列表
export function listDataCategoryCat(query) {
  return request({
    url: '/dg/dataCategoryCat/list',
    method: 'get',
    params: query
  })
}

// 查询数据分类-类目详细
export function getDataCategoryCat(id) {
  return request({
    url: '/dg/dataCategoryCat/' + id,
    method: 'get'
  })
}

// 新增数据分类-类目
export function addDataCategoryCat(data) {
  return request({
    url: '/dg/dataCategoryCat',
    method: 'post',
    data: data
  })
}

// 修改数据分类-类目
export function updateDataCategoryCat(data) {
  return request({
    url: '/dg/dataCategoryCat',
    method: 'put',
    data: data
  })
}

// 删除数据分类-类目
export function delDataCategoryCat(id) {
  return request({
    url: '/dg/dataCategoryCat/' + id,
    method: 'delete'
  })
}
