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

// Query data classification-category list
export function listDataCategoryCat(query) {
  return request({
    url: '/dg/dataCategoryCat/list',
    method: 'get',
    params: query
  })
}

// Query data classification-category details
export function getDataCategoryCat(id) {
  return request({
    url: '/dg/dataCategoryCat/' + id,
    method: 'get'
  })
}

// Add new data classification-category
export function addDataCategoryCat(data) {
  return request({
    url: '/dg/dataCategoryCat',
    method: 'post',
    data: data
  })
}

// Modify data classification-category
export function updateDataCategoryCat(data) {
  return request({
    url: '/dg/dataCategoryCat',
    method: 'put',
    data: data
  })
}

// Delete data classification-category
export function delDataCategoryCat(id) {
  return request({
    url: '/dg/dataCategoryCat/' + id,
    method: 'delete'
  })
}
