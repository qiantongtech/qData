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

// Query data assets-topic association list
export function listDaAssetThemeRel(query) {
  return request({
    url: '/da/daAssetThemeRel/list',
    method: 'get',
    params: query
  })
}

// Query data assets-topic association details
export function getDaAssetThemeRel(id) {
  return request({
    url: '/da/daAssetThemeRel/' + id,
    method: 'get'
  })
}

// Added new data asset-theme association relationship
export function addDaAssetThemeRel(data) {
  return request({
    url: '/da/daAssetThemeRel',
    method: 'post',
    data: data
  })
}

// Modify the data asset-topic association relationship
export function updateDaAssetThemeRel(data) {
  return request({
    url: '/da/daAssetThemeRel',
    method: 'put',
    data: data
  })
}

// Delete data asset-topic association relationship
export function delDaAssetThemeRel(id) {
  return request({
    url: '/da/daAssetThemeRel/' + id,
    method: 'delete'
  })
}
