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
