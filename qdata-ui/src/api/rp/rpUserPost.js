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

// Query the service resource portal user and position association list
export function listRpUserPost(query) {
  return request({
    url: '/rp/rpUserPost/list',
    method: 'get',
    params: query
  })
}

// Query the details of the relationship between service resource portal users and positions
export function getRpUserPost(userId) {
  return request({
    url: '/rp/rpUserPost/' + userId,
    method: 'get'
  })
}

// Added service resource portal user and position association
export function addRpUserPost(data) {
  return request({
    url: '/rp/rpUserPost',
    method: 'post',
    data: data
  })
}

// Modify the relationship between service resource portal users and positions
export function updateRpUserPost(data) {
  return request({
    url: '/rp/rpUserPost',
    method: 'put',
    data: data
  })
}

// Delete service resource portal user and position association
export function delRpUserPost(userId) {
  return request({
    url: '/rp/rpUserPost/' + userId,
    method: 'delete'
  })
}
