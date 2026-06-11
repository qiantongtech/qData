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

// 查询数据开发类目管理列表
export function listAttDataDevCat(query) {
  return request({
    url: '/att/dataDevCat/list',
    method: 'get',
    params: query
  })
}

// 查询数据开发类目管理详细
export function getAttDataDevCat(id) {
  return request({
    url: '/att/dataDevCat/' + id,
    method: 'get'
  })
}

// 新增数据开发类目管理
export function addAttDataDevCat(data) {
  return request({
    url: '/att/dataDevCat',
    method: 'post',
    data: data
  })
}

// 修改数据开发类目管理
export function updateAttDataDevCat(data) {
  return request({
    url: '/att/dataDevCat',
    method: 'put',
    data: data
  })
}

// 删除数据开发类目管理
export function delAttDataDevCat(id) {
  return request({
    url: '/att/dataDevCat/' + id,
    method: 'delete'
  })
}
