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

// Query the subject domain management list
export function listThemeDomain(query) {
  return request({
    url: '/dm/themeDomain/list',
    method: 'get',
    params: query
  })
}

// Query subject domain management details
export function getThemeDomain(id) {
  return request({
    url: '/dm/themeDomain/' + id,
    method: 'get'
  })
}

// Added subject domain management
export function addThemeDomain(data) {
  return request({
    url: '/dm/themeDomain',
    method: 'post',
    data: data
  })
}

// Modify subject area management
export function updateThemeDomain(data) {
  return request({
    url: '/dm/themeDomain',
    method: 'put',
    data: data
  })
}

// Delete subject domain management
export function delThemeDomain(id) {
  return request({
    url: '/dm/themeDomain/' + id,
    method: 'delete'
  })
}
