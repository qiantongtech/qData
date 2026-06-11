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

// 查询服务资源门户区域字典列表
export function listRpDict(query) {
  return request({
    url: '/rp/rpDict/list',
    method: 'get',
    params: query
  })
}

// 查询服务资源门户区域字典详细
export function getRpDict(ID) {
  return request({
    url: '/rp/rpDict/' + ID,
    method: 'get'
  })
}

// 新增服务资源门户区域字典
export function addRpDict(data) {
  return request({
    url: '/rp/rpDict',
    method: 'post',
    data: data
  })
}

// 修改服务资源门户区域字典
export function updateRpDict(data) {
  return request({
    url: '/rp/rpDict',
    method: 'put',
    data: data
  })
}

// 删除服务资源门户区域字典
export function delRpDict(ID) {
  return request({
    url: '/rp/rpDict/' + ID,
    method: 'delete'
  })
}

// 查询服务资源门户区域字典列表
export function dictTreeSelect(query) {
  return request({
    url: '/rp/rpDict/deptTree',
    method: 'get',
    params: query
  })
}
