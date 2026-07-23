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

// Query service resource portal user list
export function listRpUser(query) {
  return request({
    url: '/rp/rpUser/list',
    method: 'get',
    params: query
  })
}

// Query service resource portal user details
export function getRpUser(userId) {
  return request({
    url: '/rp/rpUser/' + parseStrEmpty(userId),
    method: 'get'
  })
}

// Add new service resource portal user
export function addRpUser(data) {
  return request({
    url: '/rp/rpUser',
    method: 'post',
    data: data
  })
}

// Modify Service Resource Portal User
export function updateRpUser(data) {
  return request({
    url: '/rp/rpUser',
    method: 'put',
    data: data
  })
}

// Delete Service Resource Portal User
export function delRpUser(userId) {
  return request({
    url: '/rp/rpUser/' + userId,
    method: 'delete'
  })
}

// Query department drop-down tree structure
export function deptTreeSelect() {
  return request({
    url: '/rp/rpUser/deptTree',
    method: 'get'
  });
}
