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
