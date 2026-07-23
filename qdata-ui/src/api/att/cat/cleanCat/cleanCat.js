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

// Query the cleaning rule category list
export function listAttCleanCat(query) {
  return request({
    url: '/att/cleanCat/list',
    method: 'get',
    params: query
  })
}

// Query cleaning rule category details
export function getAttCleanCat(ID) {
  return request({
    url: '/att/cleanCat/' + ID,
    method: 'get'
  })
}

// Added cleaning rule category
export function addAttCleanCat(data) {
  return request({
    url: '/att/cleanCat',
    method: 'post',
    data: data
  })
}

// Modify cleaning rule category
export function updateAttCleanCat(data) {
  return request({
    url: '/att/cleanCat',
    method: 'put',
    data: data
  })
}

// Delete cleaning rule category
export function delAttCleanCat(ID) {
  return request({
    url: '/att/cleanCat/' + ID,
    method: 'delete'
  })
}
