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

// Query the data asset document category management list
export function listAttDocCat(query) {
  return request({
    url: '/att/AttDocCat/list',
    method: 'get',
    params: query
  })
}

// Query data asset document category management details
export function getAttDocCat(id) {
  return request({
    url: '/att/AttDocCat/' + id,
    method: 'get'
  })
}

// Added new data asset document category management
export function addAttDocCat(data) {
  return request({
    url: '/att/AttDocCat',
    method: 'post',
    data: data
  })
}

// Modify data asset document category management
export function updateAttDocCat(data) {
  return request({
    url: '/att/AttDocCat',
    method: 'put',
    data: data
  })
}

// Delete data asset document category management
export function delAttDocCat(id) {
  return request({
    url: '/att/AttDocCat/' + id,
    method: 'delete'
  })
}
