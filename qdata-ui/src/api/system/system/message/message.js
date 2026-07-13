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

// Query message list
export function listMessage(query) {
  return request({
    url: '/system/message/list',
    method: 'get',
    params: query
  })
}

// Query message details
export function getMessage(id) {
  return request({
    url: '/system/message/' + id,
    method: 'get'
  })
}

export function getNum(query) {
  return request({
    url: '/system/message/getNum',
    method: 'get',
    params: query
  })
}

// Add new message
export function addMessage(data) {
  return request({
    url: '/system/message',
    method: 'post',
    data: data
  })
}

// Modify message
export function updateMessage(data) {
  return request({
    url: '/system/message',
    method: 'put',
    data: data
  })
}

// Delete message
export function delMessage(id) {
  return request({
    url: '/system/message/' + id,
    method: 'delete'
  })
}

// read message
export function read(id) {
  return request({
    url: '/system/message/read?id=' + id,
    method: 'post',
  })
}

// All read
export function readAll() {
  return request({
    url: '/system/message/readAll',
    method: 'post'
  })
}
