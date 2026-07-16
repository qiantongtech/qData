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

// Query data warehouse layering-standard management list
export function listDataLayerSpecification(query) {
  return request({
    url: '/dm/dataLayerSpecification/list',
    method: 'get',
    params: query
  })
}

// Query data warehouse layering-standard management details
export function getDataLayerSpecification(id) {
  return request({
    url: '/dm/dataLayerSpecification/' + id,
    method: 'get'
  })
}

// Newly added data warehouse layering - standardized management
export function addDataLayerSpecification(data) {
  return request({
    url: '/dm/dataLayerSpecification',
    method: 'post',
    data: data
  })
}

// Modify data warehouse layering-standardized management
export function updateDataLayerSpecification(data) {
  return request({
    url: '/dm/dataLayerSpecification',
    method: 'put',
    data: data
  })
}

// Delete data warehouse layering-standardized management
export function delDataLayerSpecification(id) {
  return request({
    url: '/dm/dataLayerSpecification/' + id,
    method: 'delete'
  })
}
