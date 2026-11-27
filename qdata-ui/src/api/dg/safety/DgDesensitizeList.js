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
 * Business License: https://docs.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

import request from '@/utils/request'

// Query desensitization list list
export function listDgDesensitizeList(query) {
  return request({
    url: '/dg/DgDesensitizeList/list',
    method: 'get',
    params: query
  })
}

// Query the desensitization list list associated with the desensitization rule (pass the ruleId more than the desensitization list list)
export function listDgDesensitizeListByRuleId(query) {
  return request({
    url: '/dg/DgDesensitizeList/listByRuleId',
    method: 'get',
    params: query
  })
}

// Check the desensitization list details
export function getDgDesensitizeList(id) {
  return request({
    url: '/dg/DgDesensitizeList/' + id,
    method: 'get'
  })
}

// Added desensitization list
export function addDgDesensitizeList(data) {
  return request({
    url: '/dg/DgDesensitizeList',
    method: 'post',
    data: data
  })
}

// Modify desensitization list
export function updateDgDesensitizeList(data) {
  return request({
    url: '/dg/DgDesensitizeList',
    method: 'put',
    data: data
  })
}

// Delete desensitization list
export function delDgDesensitizeList(ids) {
  const idStr = Array.isArray(ids) ? ids.join(',') : ids
  return request({
    url: '/dg/DgDesensitizeList/' + idStr,
    method: 'delete'
  })
}
