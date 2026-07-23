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

// Query desensitization whitelist list
export function listDesensitizeWhitelist(query) {
  return request({
    url: '/dg/desensitizeWhitelist/list',
    method: 'get',
    params: query
  })
}

// Check the desensitization whitelist details
export function getDesensitizeWhitelist(id) {
  return request({
    url: '/dg/desensitizeWhitelist/' + id,
    method: 'get'
  })
}

// Added desensitization whitelist
export function addDesensitizeWhitelist(data) {
  return request({
    url: '/dg/desensitizeWhitelist',
    method: 'post',
    data: data
  })
}

// Modify desensitization whitelist
export function updateDesensitizeWhitelist(data) {
  return request({
    url: '/dg/desensitizeWhitelist',
    method: 'put',
    data: data
  })
}

// Delete desensitization whitelist
export function delDesensitizeWhitelist(id) {
  return request({
    url: '/dg/desensitizeWhitelist/' + id,
    method: 'delete'
  })
}
