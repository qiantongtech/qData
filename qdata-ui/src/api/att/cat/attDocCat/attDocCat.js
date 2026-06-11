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

// 查询数据资产文档类目管理列表
export function listAttDocCat(query) {
  return request({
    url: '/att/AttDocCat/list',
    method: 'get',
    params: query
  })
}

// 查询数据资产文档类目管理详细
export function getAttDocCat(id) {
  return request({
    url: '/att/AttDocCat/' + id,
    method: 'get'
  })
}

// 新增数据资产文档类目管理
export function addAttDocCat(data) {
  return request({
    url: '/att/AttDocCat',
    method: 'post',
    data: data
  })
}

// 修改数据资产文档类目管理
export function updateAttDocCat(data) {
  return request({
    url: '/att/AttDocCat',
    method: 'put',
    data: data
  })
}

// 删除数据资产文档类目管理
export function delAttDocCat(id) {
  return request({
    url: '/att/AttDocCat/' + id,
    method: 'delete'
  })
}
