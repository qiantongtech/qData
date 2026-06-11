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

// 查询数据发现字段列表
export function listDaDiscoveryColumn(query) {
  return request({
    url: '/da/discoveryColumn/list',
    method: 'get',
    params: query
  })
}

// 查询数据发现字段详细
export function getDaDiscoveryColumn(id) {
  return request({
    url: '/da/discoveryColumn/' + id,
    method: 'get'
  })
}

// 新增数据发现字段
export function addDaDiscoveryColumn(data) {
  return request({
    url: '/da/discoveryColumn',
    method: 'post',
    data: data
  })
}

// 修改数据发现字段
export function updateDaDiscoveryColumn(data) {
  return request({
    url: '/da/discoveryColumn',
    method: 'put',
    data: data
  })
}

// 删除数据发现字段
export function delDaDiscoveryColumn(id) {
  return request({
    url: '/da/discoveryColumn/' + id,
    method: 'delete'
  })
}
