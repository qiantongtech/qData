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

// Query application API service association list
export function listClientApiRel(query) {
  return request({
    url: '/att/clientApiRel/list',
    method: 'get',
    params: query
  })
}

// Query application API service association details
export function getClientApiRel(id) {
  return request({
    url: '/att/clientApiRel/' + id,
    method: 'get'
  })
}

// Added application API service association
export function addClientApiRel(data) {
  return request({
    url: '/att/clientApiRel',
    method: 'post',
    data: data
  })
}

// Modify application API service association
export function updateClientApiRel(data) {
  return request({
    url: '/att/clientApiRel',
    method: 'put',
    data: data
  })
}

// Delete application API service association
export function delClientApiRel(id) {
  return request({
    url: '/att/clientApiRel/' + id,
    method: 'delete'
  })
}
