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

// 查询API服务调用日志列表
export function listApiLog(query) {
  return request({
    url: '/ds/apiLog/list',
    method: 'get',
    params: query
  })
}

// 查询API服务调用日志详细
export function getApiLog(ID) {
  return request({
    url: '/ds/apiLog/' + ID,
    method: 'get'
  })
}

// 新增API服务调用日志
export function addApiLog(data) {
  return request({
    url: '/ds/apiLog',
    method: 'post',
    data: data
  })
}

// 修改API服务调用日志
export function updateApiLog(data) {
  return request({
    url: '/ds/apiLog',
    method: 'put',
    data: data
  })
}

// 删除API服务调用日志
export function delApiLog(ID) {
  return request({
    url: '/ds/apiLog/' + ID,
    method: 'delete'
  })
}
