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

// 查询数据质量类目列表
export function listAttQualityCat(query) {
  return request({
    url: '/att/qualityCat/list',
    method: 'get',
    params: query
  })
}

// 查询数据质量类目详细
export function getAttQualityCat(id) {
  return request({
    url: '/att/qualityCat/' + id,
    method: 'get'
  })
}

// 新增数据质量类目
export function addAttQualityCat(data) {
  return request({
    url: '/att/qualityCat',
    method: 'post',
    data: data
  })
}

// 修改数据质量类目
export function updateAttQualityCat(data) {
  return request({
    url: '/att/qualityCat',
    method: 'put',
    data: data
  })
}

// 删除数据质量类目
export function delAttQualityCat(id) {
  return request({
    url: '/att/qualityCat/' + id,
    method: 'delete'
  })
}
