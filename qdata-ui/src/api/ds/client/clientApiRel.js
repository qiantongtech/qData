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

// 查询应用API服务关联列表
export function listClientApiRel(query) {
  return request({
    url: '/att/clientApiRel/list',
    method: 'get',
    params: query
  })
}

// 查询应用API服务关联详细
export function getClientApiRel(id) {
  return request({
    url: '/att/clientApiRel/' + id,
    method: 'get'
  })
}

// 新增应用API服务关联
export function addClientApiRel(data) {
  return request({
    url: '/att/clientApiRel',
    method: 'post',
    data: data
  })
}

// 修改应用API服务关联
export function updateClientApiRel(data) {
  return request({
    url: '/att/clientApiRel',
    method: 'put',
    data: data
  })
}

// 删除应用API服务关联
export function delClientApiRel(id) {
  return request({
    url: '/att/clientApiRel/' + id,
    method: 'delete'
  })
}
