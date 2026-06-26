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

// 查询消息模板列表
export function listMessageTemplate(query) {
  return request({
    url: '/system/messageTemplate/list',
    method: 'get',
    params: query
  })
}

// 查询消息模板详细
export function getMessageTemplate(id) {
  return request({
    url: '/system/messageTemplate/' + id,
    method: 'get'
  })
}

// 新增消息模板
export function addMessageTemplate(data) {
  return request({
    url: '/system/messageTemplate',
    method: 'post',
    data: data
  })
}

// 修改消息模板
export function updateMessageTemplate(data) {
  return request({
    url: '/system/messageTemplate',
    method: 'put',
    data: data
  })
}

// 删除消息模板
export function delMessageTemplate(id) {
  return request({
    url: '/system/messageTemplate/' + id,
    method: 'delete'
  })
}
