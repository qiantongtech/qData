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
