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

// 查询脱敏规则列表
export function listDesensitizeRules(query) {
  return request({
    url: '/dg/desensitizeRules/list',
    method: 'get',
    params: query
  })
}

// 查询脱敏规则详细
export function getDesensitizeRules(id) {
  return request({
    url: '/dg/desensitizeRules/' + id,
    method: 'get'
  })
}

// 新增脱敏规则
export function addDesensitizeRules(data) {
  return request({
    url: '/dg/desensitizeRules',
    method: 'post',
    data: data
  })
}

// 修改脱敏规则
export function updateDesensitizeRules(data) {
  return request({
    url: '/dg/desensitizeRules',
    method: 'put',
    data: data
  })
}

// 删除脱敏规则
export function delDesensitizeRules(id) {
  return request({
    url: '/dg/desensitizeRules/' + id,
    method: 'delete'
  })
}
