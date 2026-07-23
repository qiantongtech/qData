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

// Query data discovery field list
export function listDaDiscoveryColumn(query) {
  return request({
    url: '/da/discoveryColumn/list',
    method: 'get',
    params: query
  })
}

// Query data discovery field details
export function getDaDiscoveryColumn(id) {
  return request({
    url: '/da/discoveryColumn/' + id,
    method: 'get'
  })
}

// Add new data discovery fields
export function addDaDiscoveryColumn(data) {
  return request({
    url: '/da/discoveryColumn',
    method: 'post',
    data: data
  })
}

// Modify data discovery fields
export function updateDaDiscoveryColumn(data) {
  return request({
    url: '/da/discoveryColumn',
    method: 'put',
    data: data
  })
}

// Remove data discovery fields
export function delDaDiscoveryColumn(id) {
  return request({
    url: '/da/discoveryColumn/' + id,
    method: 'delete'
  })
}
