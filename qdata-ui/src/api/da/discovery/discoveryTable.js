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

// Query the data discovery library information list
export function listDaDiscoveryTable(query) {
  return request({
    url: '/da/discoveryTable/list',
    method: 'get',
    params: query
  })
}

// Query the data discovery library information list
export function getDaDiscoveryTableList(query) {
  return request({
    url: '/da/discoveryTable/getDaDiscoveryTableList',
    method: 'get',
    params: query
  })
}

// Query data discovery database information details
export function getDaDiscoveryTable(id) {
  return request({
    url: '/da/discoveryTable/' + id,
    method: 'get'
  })
}

// Add new data discovery library information
export function addDaDiscoveryTable(data) {
  return request({
    url: '/da/discoveryTable',
    method: 'post',
    data: data
  })
}
// Add new data discovery library information
export function commitOrRevokeDiscoveryInfo(data) {
  return request({
    url: '/da/discoveryTable/commitOrRevokeDiscoveryInfo',
    method: 'post',
    data: data
  })
}

// Modify data discovery library information
export function updateDaDiscoveryTable(data) {
  return request({
    url: '/da/discoveryTable',
    method: 'put',
    data: data
  })
}

// Delete data discovery library information
export function delDaDiscoveryTable(id) {
  return request({
    url: '/da/discoveryTable/' + id,
    method: 'delete'
  })
}
// table fields
export function getDaDiscoveryColumnList(params) {
  return request({
    url: '/da/discoveryColumn/getDaDiscoveryColumnList',
    method: 'get',
    params: params
  })
}

export function preview(data) {
  return request({
    url: '/da/discoveryTable/preview',
    method: 'post',
    data: data
  })
}
