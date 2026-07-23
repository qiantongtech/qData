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

// Query the data warehouse hierarchical management list
export function listDataLayer(query) {
  return request({
    url: '/dm/dataLayer/list',
    method: 'get',
    params: query
  })
}

// Query the details of hierarchical management of data warehouse
export function getDataLayer(id) {
  return request({
    url: '/dm/dataLayer/' + id,
    method: 'get'
  })
}

// Query the data warehouse hierarchical management tree
export function treeDataLayer() {
  return request({
    url: '/dm/dataLayer/tree',
    method: 'get'
  })
}

// Added new data warehouse hierarchical management
export function addDataLayer(data) {
  return request({
    url: '/dm/dataLayer',
    method: 'post',
    data: data
  })
}

// Modify data warehouse hierarchical management
export function updateDataLayer(data) {
  return request({
    url: '/dm/dataLayer',
    method: 'put',
    data: data
  })
}

// Delete data warehouse hierarchical management
export function delDataLayer(id) {
  return request({
    url: '/dm/dataLayer/' + id,
    method: 'delete'
  })
}
