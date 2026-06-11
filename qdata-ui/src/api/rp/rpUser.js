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
