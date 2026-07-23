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

// Query data assets-video data list
export function listDaAssetVideo(query) {
  return request({
    url: '/da/daAssetVideo/list',
    method: 'get',
    params: query
  })
}

// Query data assets-video data details
export function getDaAssetVideo(id) {
  return request({
    url: '/da/daAssetVideo/' + id,
    method: 'get'
  })
}

// New data asset-video data
export function addDaAssetVideo(data) {
  return request({
    url: '/da/daAssetVideo',
    method: 'post',
    data: data
  })
}

// Modify data assets-video data
export function updateDaAssetVideo(data) {
  return request({
    url: '/da/daAssetVideo',
    method: 'put',
    data: data
  })
}

// Delete data asset - video data
export function delDaAssetVideo(id) {
  return request({
    url: '/da/daAssetVideo/' + id,
    method: 'delete'
  })
}
