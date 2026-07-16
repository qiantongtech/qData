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

// Query the list of cleaning rules
export function listAttCleanRule(query) {
  return request({
    url: '/att/cleanRule/list',
    method: 'get',
    params: query
  })
}

// Query detailed cleaning rules
export function getAttCleanRule(id) {
  return request({
    url: '/att/cleanRule/' + id,
    method: 'get'
  })
}

// Add cleaning rules
export function addAttCleanRule(data) {
  return request({
    url: '/att/cleanRule',
    method: 'post',
    data: data
  })
}

// Modify cleaning rules
export function updateAttCleanRule(data) {
  return request({
    url: '/att/cleanRule',
    method: 'put',
    data: data
  })
}

// Delete cleaning rules
export function delAttCleanRule(id) {
  return request({
    url: '/att/cleanRule/' + id,
    method: 'delete'
  })
}

// tree
export function treeAttCleanRule(params) {
  return request({
    url: '/att/cleanRule/tree',
    method: 'get',
    params
  })
}

// Cleaning rules used in data integration
export function listAll(params) {
  return request({
    url: '/att/cleanRule/listAll',
    method: 'get',
    params
  })
}
