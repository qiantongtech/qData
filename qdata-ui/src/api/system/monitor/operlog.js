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

import request from '@/utils/request.js'

// 查询操作日志列表
export function list(query) {
  return request({
    url: '/monitor/operlog/list',
    method: 'get',
    params: query
  })
}

// 删除操作日志
export function delOperlog(operId) {
  return request({
    url: '/monitor/operlog/' + operId,
    method: 'delete'
  })
}

// 清空操作日志
export function cleanOperlog() {
  return request({
    url: '/monitor/operlog/clean',
    method: 'delete'
  })
}
