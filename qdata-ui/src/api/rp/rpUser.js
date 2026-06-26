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
import {parseStrEmpty} from "@/utils/anivia.js";

// 查询服务资源门户用户列表
export function listRpUser(query) {
  return request({
    url: '/rp/rpUser/list',
    method: 'get',
    params: query
  })
}

// 查询服务资源门户用户详细
export function getRpUser(userId) {
  return request({
    url: '/rp/rpUser/' + parseStrEmpty(userId),
    method: 'get'
  })
}

// 新增服务资源门户用户
export function addRpUser(data) {
  return request({
    url: '/rp/rpUser',
    method: 'post',
    data: data
  })
}

// 修改服务资源门户用户
export function updateRpUser(data) {
  return request({
    url: '/rp/rpUser',
    method: 'put',
    data: data
  })
}

// 删除服务资源门户用户
export function delRpUser(userId) {
  return request({
    url: '/rp/rpUser/' + userId,
    method: 'delete'
  })
}

// 查询部门下拉树结构
export function deptTreeSelect() {
  return request({
    url: '/rp/rpUser/deptTree',
    method: 'get'
  });
}
