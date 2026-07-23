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

// Query the list of data integration task instances
export function listDppEtlTaskInstance(query) {
  return request({
    url: '/dpp/etlTaskInstance/list',
    method: 'get',
    params: query
  })
}

// Query data integration task instance details
export function getDppEtlTaskInstance(id) {
  return request({
    url: '/dpp/etlTaskInstance/' + id,
    method: 'get'
  })
}

// Added data integration task instance
export function addDppEtlTaskInstance(data) {
  return request({
    url: '/dpp/etlTaskInstance',
    method: 'post',
    data: data
  })
}

// Modify data integration task instance
export function updateDppEtlTaskInstance(data) {
  return request({
    url: '/dpp/etlTaskInstance',
    method: 'put',
    data: data
  })
}

// Delete a data integration task instance
export function delDppEtlTaskInstance(id) {
  return request({
    url: '/dpp/etlTaskInstance/' + id,
    method: 'delete'
  })
}


// Query the list of data integration task instances
export function listDppEtlTreeList(query) {
  return request({
    url: '/dpp/etlTaskInstance/treeList',
    method: 'get',
    params: query
  })
}
// Get a list of subtasks
export function subNodeList(query) {
  return request({
    url: '/dpp/etlTaskInstance/subNodeList',
    method: 'get',
    params: query
  })
}

