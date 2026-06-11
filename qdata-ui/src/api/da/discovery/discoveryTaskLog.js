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

// 查询数据发现任务日志列表
export function listDaDiscoveryTaskLog(query) {
  return request({
    url: '/da/discoveryTaskLog/list',
    method: 'get',
    params: query
  })
}
// 查看
export function logDetailCat(query) {
  return request({
    url: '/da/discoveryTaskLog/logDetailCat',
    method: 'get',
    params: query
  })
}

export function downloadLog(query) {
  return request({
    url: '/da/discoveryTaskLog/downloadLog',
    method: 'get',
    params: query
  })
}


// 查询数据发现任务日志详细
export function getDaDiscoveryTaskLog(id) {
  return request({
    url: '/da/discoveryTaskLog/' + id,
    method: 'get'
  })
}

// 新增数据发现任务日志
export function addDaDiscoveryTaskLog(data) {
  return request({
    url: '/da/discoveryTaskLog',
    method: 'post',
    data: data
  })
}

// 修改数据发现任务日志
export function updateDaDiscoveryTaskLog(data) {
  return request({
    url: '/da/discoveryTaskLog',
    method: 'put',
    data: data
  })
}

// 删除数据发现任务日志
export function delDaDiscoveryTaskLog(id) {
  return request({
    url: '/da/discoveryTaskLog/' + id,
    method: 'delete'
  })
}
