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

// Query the list of relationships between tags and assets
export function listAttTagAssetRel(query) {
  return request({
    url: '/att/tagAssetRel/list',
    method: 'get',
    params: query
  })
}

// Query the detailed relationship between tags and assets
export function getAttTagAssetRel(ID) {
  return request({
    url: '/att/tagAssetRel/' + ID,
    method: 'get'
  })
}

// Add new tags and asset relationships
export function addAttTagAssetRel(data) {
  return request({
    url: '/att/tagAssetRel',
    method: 'post',
    data: data
  })
}

// Modify the relationship between tags and assets
export function updateAttTagAssetRel(data) {
  return request({
    url: '/att/tagAssetRel',
    method: 'put',
    data: data
  })
}

// Delete the relationship between tags and assets
export function delAttTagAssetRel(ID) {
  return request({
    url: '/att/tagAssetRel/' + ID,
    method: 'delete'
  })
}

export function delByTagIdAndAesstId(query) {
  return request({
    url: '/att/tagAssetRel/delByTagIdAndAesstId',
    method: 'delete',
    params: query
  })
}

