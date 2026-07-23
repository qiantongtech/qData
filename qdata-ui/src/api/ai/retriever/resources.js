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

// Query the retrieved resource list
export function listRetriever(query) {
  return request({
    url: '/app/retriever/list',
    method: 'get',
    params: query
  })
}

export function listByMessage(query) {
  return request({
    url: '/app/retriever/listByMessage',
    method: 'get',
    params: query
  })
}

// Query the retrieved resource details
export function getRetriever(id) {
  return request({
    url: '/app/retriever/' + id,
    method: 'get'
  })
}

// Add new retrieved resources
export function addRetriever(data) {
  return request({
    url: '/app/retriever',
    method: 'post',
    data: data
  })
}

// Modify retrieved resources
export function updateRetriever(data) {
  return request({
    url: '/app/retriever',
    method: 'put',
    data: data
  })
}

// Delete retrieved resources
export function delRetriever(id) {
  return request({
    url: '/app/retriever/' + id,
    method: 'delete'
  })
}
