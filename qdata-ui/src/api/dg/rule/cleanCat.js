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

// Query the cleaning rule category management list
export function listCleanCat(query) {
  return request({
    url: '/dg/cleanCat/list',
    method: 'get',
    params: query
  })
}

// Query cleaning rule category management details
export function getCleanCat(id) {
  return request({
    url: '/dg/cleanCat/' + id,
    method: 'get'
  })
}

// Added cleaning rule category management
export function addCleanCat(data) {
  return request({
    url: '/dg/cleanCat',
    method: 'post',
    data: data
  })
}

// Modify cleaning rule category management
export function updateCleanCat(data) {
  return request({
    url: '/dg/cleanCat',
    method: 'put',
    data: data
  })
}

// Delete cleaning rule category management
export function delCleanCat(id) {
  return request({
    url: '/dg/cleanCat/' + id,
    method: 'delete'
  })
}
// Verification before batch deletion of cleaning rule category management
export function batchDeleteCheck(ids) {
  return request({
    url: '/dg/cleanCat/batchDeleteCheck/' + ids,
    method: 'get'
  })
}
