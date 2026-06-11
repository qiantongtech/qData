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

// 查询标准信息分类管理列表
export function listAttDocumentCat(query) {
  return request({
    url: '/att/documentCat/list',
    method: 'get',
    params: query
  })
}

// 查询标准信息分类管理详细
export function getAttDocumentCat(id) {
  return request({
    url: '/att/documentCat/' + id,
    method: 'get'
  })
}

// 新增标准信息分类管理
export function addAttDocumentCat(data) {
  return request({
    url: '/att/documentCat',
    method: 'post',
    data: data
  })
}

// 修改标准信息分类管理
export function updateAttDocumentCat(data) {
  return request({
    url: '/att/documentCat',
    method: 'put',
    data: data
  })
}

// 删除标准信息分类管理
export function delAttDocumentCat(id) {
  return request({
    url: '/att/documentCat/' + id,
    method: 'delete'
  })
}
