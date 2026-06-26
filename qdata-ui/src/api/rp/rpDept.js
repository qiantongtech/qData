/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 * You may use qData for commercial purposes, but you may not remove, hide,
 * modify, or replace the qData logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qData as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
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
