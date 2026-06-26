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

import request from '@/utils/request.js'

// 查询主体管理列表
export function listSubject(query) {
  return request({
    url: '/ca/subject/list',
    method: 'get',
    params: query
  })
}

// 查询主体管理详细
export function getSubject(id) {
  return request({
    url: '/ca/subject/' + id,
    method: 'get'
  })
}

// 新增主体管理
export function addSubject(data) {
  return request({
    url: '/ca/subject',
    method: 'post',
    data: data
  })
}

// 修改主体管理
export function updateSubject(data) {
  return request({
    url: '/ca/subject',
    method: 'put',
    data: data
  })
}

// 删除主体管理
export function delSubject(id) {
  return request({
    url: '/ca/subject/' + id,
    method: 'delete'
  })
}
