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

// Query the data asset category management list
export function listAttAssetCat(query) {
  return request({
    url: '/att/assetCat/list',
    method: 'get',
    params: query
  })
}

// Query data asset category management details
export function getAttAssetCat(id) {
  return request({
    url: '/att/assetCat/' + id,
    method: 'get'
  })
}

// Added new data asset category management
export function addAttAssetCat(data) {
  return request({
    url: '/att/assetCat',
    method: 'post',
    data: data
  })
}

// Modify data asset category management
export function updateAttAssetCat(data) {
  return request({
    url: '/att/assetCat',
    method: 'put',
    data: data
  })
}

// Delete data asset category management
export function delAttAssetCat(id) {
  return request({
    url: '/att/assetCat/' + id,
    method: 'delete'
  })
}
