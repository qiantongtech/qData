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

// 查询主题域管理列表
export function listThemeDomain(query) {
  return request({
    url: '/dm/themeDomain/list',
    method: 'get',
    params: query
  })
}

// 查询主题域管理详细
export function getThemeDomain(id) {
  return request({
    url: '/dm/themeDomain/' + id,
    method: 'get'
  })
}

// 新增主题域管理
export function addThemeDomain(data) {
  return request({
    url: '/dm/themeDomain',
    method: 'post',
    data: data
  })
}

// 修改主题域管理
export function updateThemeDomain(data) {
  return request({
    url: '/dm/themeDomain',
    method: 'put',
    data: data
  })
}

// 删除主题域管理
export function delThemeDomain(id) {
  return request({
    url: '/dm/themeDomain/' + id,
    method: 'delete'
  })
}
