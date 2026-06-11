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

// 查询来源系统列表
export function listSourceSystem(query) {
  return request({
    url: '/att/sourceSystem/list',
    method: 'get',
    params: query
  })
}

// 查询所有来源系统列表
export function listValidSourceSystem(query) {
  return request({
    url: '/att/sourceSystem/listValid',
    method: 'get',
    params: query
  })
}

// 查询来源系统详细
export function getSourceSystem(id) {
  return request({
    url: '/att/sourceSystem/' + id,
    method: 'get'
  })
}

// 新增来源系统
export function addSourceSystem(data) {
  return request({
    url: '/att/sourceSystem',
    method: 'post',
    data: data
  })
}

// 修改来源系统
export function updateSourceSystem(data) {
  return request({
    url: '/att/sourceSystem',
    method: 'put',
    data: data
  })
}

// 删除来源系统
export function delSourceSystem(id) {
  return request({
    url: '/att/sourceSystem/' + id,
    method: 'delete'
  })
}
