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

// 查询消息列表
export function listMessage(query) {
  return request({
    url: '/system/message/list',
    method: 'get',
    params: query
  })
}

// 查询消息详细
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

// 新增消息
export function addMessage(data) {
  return request({
    url: '/system/message',
    method: 'post',
    data: data
  })
}

// 修改消息
export function updateMessage(data) {
  return request({
    url: '/system/message',
    method: 'put',
    data: data
  })
}

// 删除消息
export function delMessage(id) {
  return request({
    url: '/system/message/' + id,
    method: 'delete'
  })
}

// 已读消息
export function read(id) {
  return request({
    url: '/system/message/read?id=' + id,
    method: 'post',
  })
}

// 全部已读
export function readAll() {
  return request({
    url: '/system/message/readAll',
    method: 'post'
  })
}
