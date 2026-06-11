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

// 查询逻辑模型类目管理列表
export function listAttModelCat(query) {
  return request({
    url: '/att/modelCat/list',
    method: 'get',
    params: query
  })
}

// 查询逻辑模型类目管理详细
export function getAttModelCat(ID) {
  return request({
    url: '/att/modelCat/' + ID,
    method: 'get'
  })
}

// 新增逻辑模型类目管理
export function addAttModelCat(data) {
  return request({
    url: '/att/modelCat',
    method: 'post',
    data: data
  })
}

// 修改逻辑模型类目管理
export function updateAttModelCat(data) {
  return request({
    url: '/att/modelCat',
    method: 'put',
    data: data
  })
}

// 删除逻辑模型类目管理
export function delAttModelCat(ID) {
  return request({
    url: '/att/modelCat/' + ID,
    method: 'delete'
  })
}
