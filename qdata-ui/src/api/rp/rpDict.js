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

// Query service resource portal area dictionary list
export function listRpDict(query) {
  return request({
    url: '/rp/rpDict/list',
    method: 'get',
    params: query
  })
}

// Query service resource portal area dictionary details
export function getRpDict(ID) {
  return request({
    url: '/rp/rpDict/' + ID,
    method: 'get'
  })
}

// Added service resource portal area dictionary
export function addRpDict(data) {
  return request({
    url: '/rp/rpDict',
    method: 'post',
    data: data
  })
}

// Modify the service resource portal area dictionary
export function updateRpDict(data) {
  return request({
    url: '/rp/rpDict',
    method: 'put',
    data: data
  })
}

// Delete Service Resource Portal Zone Dictionary
export function delRpDict(ID) {
  return request({
    url: '/rp/rpDict/' + ID,
    method: 'delete'
  })
}

// Query service resource portal area dictionary list
export function dictTreeSelect(query) {
  return request({
    url: '/rp/rpDict/deptTree',
    method: 'get',
    params: query
  })
}
