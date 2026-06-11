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

// 查询示例部门列表
export function listDept(query) {
  return request({
    url: '/example/dept/list',
    method: 'get',
    params: query
  })
}

// 查询示例部门详细
export function getDept(id) {
  return request({
    url: '/example/dept/' + id,
    method: 'get'
  })
}

// 新增示例部门
export function addDept(data) {
  return request({
    url: '/example/dept',
    method: 'post',
    data: data
  })
}

// 修改示例部门
export function updateDept(data) {
  return request({
    url: '/example/dept',
    method: 'put',
    data: data
  })
}

// 删除示例部门
export function delDept(id) {
  return request({
    url: '/example/dept/' + id,
    method: 'delete'
  })
}
