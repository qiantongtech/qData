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

// 查询数仓分层管理列表
export function listDataLayer(query) {
  return request({
    url: '/dm/dataLayer/list',
    method: 'get',
    params: query
  })
}

// 查询数仓分层管理详细
export function getDataLayer(id) {
  return request({
    url: '/dm/dataLayer/' + id,
    method: 'get'
  })
}

// 查询数仓分层管理树
export function treeDataLayer() {
  return request({
    url: '/dm/dataLayer/tree',
    method: 'get'
  })
}

// 新增数仓分层管理
export function addDataLayer(data) {
  return request({
    url: '/dm/dataLayer',
    method: 'post',
    data: data
  })
}

// 修改数仓分层管理
export function updateDataLayer(data) {
  return request({
    url: '/dm/dataLayer',
    method: 'put',
    data: data
  })
}

// 删除数仓分层管理
export function delDataLayer(id) {
  return request({
    url: '/dm/dataLayer/' + id,
    method: 'delete'
  })
}
