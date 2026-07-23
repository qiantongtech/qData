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

// Query the list of data quality categories
export function listAttQualityCat(query) {
  return request({
    url: '/att/qualityCat/list',
    method: 'get',
    params: query
  })
}

// Query data quality category details
export function getAttQualityCat(id) {
  return request({
    url: '/att/qualityCat/' + id,
    method: 'get'
  })
}

// Added new data quality category
export function addAttQualityCat(data) {
  return request({
    url: '/att/qualityCat',
    method: 'post',
    data: data
  })
}

// Modify data quality category
export function updateAttQualityCat(data) {
  return request({
    url: '/att/qualityCat',
    method: 'put',
    data: data
  })
}

// Delete data quality category
export function delAttQualityCat(id) {
  return request({
    url: '/att/qualityCat/' + id,
    method: 'delete'
  })
}
