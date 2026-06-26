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

// 查询服务资源门户岗位列表
export function listRpPost(query) {
  return request({
    url: '/rp/rpPost/list',
    method: 'get',
    params: query
  })
}

// 查询服务资源门户岗位详细
export function getRpPost(postId) {
  return request({
    url: '/rp/rpPost/' + postId,
    method: 'get'
  })
}

// 新增服务资源门户岗位
export function addRpPost(data) {
  return request({
    url: '/rp/rpPost',
    method: 'post',
    data: data
  })
}

// 修改服务资源门户岗位
export function updateRpPost(data) {
  return request({
    url: '/rp/rpPost',
    method: 'put',
    data: data
  })
}

// 删除服务资源门户岗位
export function delRpPost(postId) {
  return request({
    url: '/rp/rpPost/' + postId,
    method: 'delete'
  })
}
