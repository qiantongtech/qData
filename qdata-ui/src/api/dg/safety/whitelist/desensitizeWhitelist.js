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

// 查询脱敏白名单列表
export function listDesensitizeWhitelist(query) {
  return request({
    url: '/dg/desensitizeWhitelist/list',
    method: 'get',
    params: query
  })
}

// 查询脱敏白名单详细
export function getDesensitizeWhitelist(id) {
  return request({
    url: '/dg/desensitizeWhitelist/' + id,
    method: 'get'
  })
}

// 新增脱敏白名单
export function addDesensitizeWhitelist(data) {
  return request({
    url: '/dg/desensitizeWhitelist',
    method: 'post',
    data: data
  })
}

// 修改脱敏白名单
export function updateDesensitizeWhitelist(data) {
  return request({
    url: '/dg/desensitizeWhitelist',
    method: 'put',
    data: data
  })
}

// 删除脱敏白名单
export function delDesensitizeWhitelist(id) {
  return request({
    url: '/dg/desensitizeWhitelist/' + id,
    method: 'delete'
  })
}
