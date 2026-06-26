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

// 查询脱敏规则列表
export function listDesensitizeRules(query) {
  return request({
    url: '/dg/desensitizeRules/list',
    method: 'get',
    params: query
  })
}

// 查询脱敏规则详细
export function getDesensitizeRules(id) {
  return request({
    url: '/dg/desensitizeRules/' + id,
    method: 'get'
  })
}

// 新增脱敏规则
export function addDesensitizeRules(data) {
  return request({
    url: '/dg/desensitizeRules',
    method: 'post',
    data: data
  })
}

// 修改脱敏规则
export function updateDesensitizeRules(data) {
  return request({
    url: '/dg/desensitizeRules',
    method: 'put',
    data: data
  })
}

// 删除脱敏规则
export function delDesensitizeRules(id) {
  return request({
    url: '/dg/desensitizeRules/' + id,
    method: 'delete'
  })
}
