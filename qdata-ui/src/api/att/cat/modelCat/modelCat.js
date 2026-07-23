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

import request from '@/utils/request.js'

// Query the logical model category management list
export function listAttModelCat(query) {
  return request({
    url: '/att/modelCat/list',
    method: 'get',
    params: query
  })
}

// Query logical model category management details
export function getAttModelCat(ID) {
  return request({
    url: '/att/modelCat/' + ID,
    method: 'get'
  })
}

// Added logical model category management
export function addAttModelCat(data) {
  return request({
    url: '/att/modelCat',
    method: 'post',
    data: data
  })
}

// Modify logical model category management
export function updateAttModelCat(data) {
  return request({
    url: '/att/modelCat',
    method: 'put',
    data: data
  })
}

// Delete logical model category management
export function delAttModelCat(ID) {
  return request({
    url: '/att/modelCat/' + ID,
    method: 'delete'
  })
}
