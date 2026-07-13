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

// Query data classification list
export function listDataCategory(query) {
  return request({
    url: '/dg/dataCategory/list',
    method: 'get',
    params: query
  })
}

export function listDataCategoryAll(query) {
  return request({
    url: '/dg/dataCategory/listAll',
    method: 'get',
    params: query
  })
}


// Query data classification details
export function getDataCategory(id) {
  return request({
    url: '/dg/dataCategory/' + id,
    method: 'get'
  })
}

// Query data classification tree
export function selectTreeDataCategory(query) {
  return request({
    url: '/dg/dataCategory/selectTree',
    method: 'get',
    params: query
  })
}

// Add new data classification
export function addDataCategory(data) {
  return request({
    url: '/dg/dataCategory',
    method: 'post',
    data: data
  })
}

// Modify data classification
export function updateDataCategory(data) {
  return request({
    url: '/dg/dataCategory',
    method: 'put',
    data: data
  })
}

// Delete data classification
export function delDataCategory(id) {
  return request({
    url: '/dg/dataCategory/' + id,
    method: 'delete'
  })
}

// Batch grading
export function batchDataLevel(data) {
  return request({
    url: '/dg/dataCategory/batchDataLevel',
    method: 'PUT',
    data: data
  })
}
