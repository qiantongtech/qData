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

// Query the data discovery task log list
export function listDaDiscoveryTaskLog(query) {
  return request({
    url: '/da/discoveryTaskLog/list',
    method: 'get',
    params: query
  })
}
// View
export function logDetailCat(query) {
  return request({
    url: '/da/discoveryTaskLog/logDetailCat',
    method: 'get',
    params: query
  })
}

export function downloadLog(query) {
  return request({
    url: '/da/discoveryTaskLog/downloadLog',
    method: 'get',
    params: query
  })
}


// Query data discovery task log details
export function getDaDiscoveryTaskLog(id) {
  return request({
    url: '/da/discoveryTaskLog/' + id,
    method: 'get'
  })
}

// Added data discovery task log
export function addDaDiscoveryTaskLog(data) {
  return request({
    url: '/da/discoveryTaskLog',
    method: 'post',
    data: data
  })
}

// Modify data discovery task log
export function updateDaDiscoveryTaskLog(data) {
  return request({
    url: '/da/discoveryTaskLog',
    method: 'put',
    data: data
  })
}

// Delete data discovery task logs
export function delDaDiscoveryTaskLog(id) {
  return request({
    url: '/da/discoveryTaskLog/' + id,
    method: 'delete'
  })
}
