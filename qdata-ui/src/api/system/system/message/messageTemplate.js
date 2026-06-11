/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
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
