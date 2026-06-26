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

// 查询稽查规则列表
export function listAttAuditRule(query) {
  return request({
    url: '/att/auditRule/list',
    method: 'get',
    params: query
  })
}

// 查询稽查规则详细
export function getAttAuditRule(id) {
  return request({
    url: '/att/auditRule/' + id,
    method: 'get'
  })
}

// 新增稽查规则
export function addAttAuditRule(data) {
  return request({
    url: '/att/auditRule',
    method: 'post',
    data: data
  })
}

// 修改稽查规则
export function updateAttAuditRule(data) {
  return request({
    url: '/att/auditRule',
    method: 'put',
    data: data
  })
}

// 删除稽查规则
export function delAttAuditRule(id) {
  return request({
    url: '/att/auditRule/' + id,
    method: 'delete'
  })
}

// tree
export function treeAttAuditRule(params) {
  return request({
    url: '/att/attAuditRule/tree',
    method: 'get',
    params
  })
}
