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

// 查询服务资源门户部门列表
export function listRpDept(query) {
  return request({
    url: '/rp/rpDept/list',
    method: 'get',
    params: query
  })
}

// 查询服务资源门户部门列表
export function listRpDeptList(query) {
  return request({
    url: '/rp/rpDept/deptList',
    method: 'get',
    params: query
  })
}

// 查询服务资源门户部门详细
export function getRpDept(deptId) {
  return request({
    url: '/rp/rpDept/' + deptId,
    method: 'get'
  })
}

// 新增服务资源门户部门
export function addRpDept(data) {
  return request({
    url: '/rp/rpDept',
    method: 'post',
    data: data
  })
}

// 修改服务资源门户部门
export function updateRpDept(data) {
  return request({
    url: '/rp/rpDept',
    method: 'put',
    data: data
  })
}

// 删除服务资源门户部门
export function delRpDept(deptId) {
  return request({
    url: '/rp/rpDept/' + deptId,
    method: 'delete'
  })
}
