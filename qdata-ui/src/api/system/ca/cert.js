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

import request from '@/utils/request.js'

// 查询证书管理列表
export function listCert(query) {
  return request({
    url: '/ca/cert/list',
    method: 'get',
    params: query
  })
}

// 查询证书管理详细
export function getCert(id) {
  return request({
    url: '/ca/cert/' + id,
    method: 'get'
  })
}

// 新增证书管理
export function addCert(data) {
  return request({
    url: '/ca/cert',
    method: 'post',
    data: data
  })
}

// 修改证书管理
export function updateCert(data) {
  return request({
    url: '/ca/cert',
    method: 'put',
    data: data
  })
}

// 删除证书管理
export function delCert(id) {
  return request({
    url: '/ca/cert/' + id,
    method: 'delete'
  })
}
