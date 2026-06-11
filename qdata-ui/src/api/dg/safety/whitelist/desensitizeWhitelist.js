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

// 查询脱敏白名单列表
export function listDesensitizeWhitelist(query) {
  return request({
    url: '/dg/desensitizeWhitelist/list',
    method: 'get',
    params: query
  })
}

// 查询脱敏白名单详细
export function getDesensitizeWhitelist(id) {
  return request({
    url: '/dg/desensitizeWhitelist/' + id,
    method: 'get'
  })
}

// 新增脱敏白名单
export function addDesensitizeWhitelist(data) {
  return request({
    url: '/dg/desensitizeWhitelist',
    method: 'post',
    data: data
  })
}

// 修改脱敏白名单
export function updateDesensitizeWhitelist(data) {
  return request({
    url: '/dg/desensitizeWhitelist',
    method: 'put',
    data: data
  })
}

// 删除脱敏白名单
export function delDesensitizeWhitelist(id) {
  return request({
    url: '/dg/desensitizeWhitelist/' + id,
    method: 'delete'
  })
}
