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

// Query the data development category management list
export function listAttDataDevCat(query) {
  return request({
    url: '/att/dataDevCat/list',
    method: 'get',
    params: query
  })
}

// Query data development category management details
export function getAttDataDevCat(id) {
  return request({
    url: '/att/dataDevCat/' + id,
    method: 'get'
  })
}

// Added new data development category management
export function addAttDataDevCat(data) {
  return request({
    url: '/att/dataDevCat',
    method: 'post',
    data: data
  })
}

// Modify data development category management
export function updateAttDataDevCat(data) {
  return request({
    url: '/att/dataDevCat',
    method: 'put',
    data: data
  })
}

// Delete data development category management
export function delAttDataDevCat(id) {
  return request({
    url: '/att/dataDevCat/' + id,
    method: 'delete'
  })
}
