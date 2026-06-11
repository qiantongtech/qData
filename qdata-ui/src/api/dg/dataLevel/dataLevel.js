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

// 查询数据分级列表
export function listDataLevel(query) {
  return request({
    url: '/dg/dataLevel/list',
    method: 'get',
    params: query
  })
}

// 查询所有数据分级下拉列表
export function listAllDataLevel(query) {
  return request({
    url: '/dg/dataLevel/listAll',
    method: 'get',
    params: query
  })
}

// 查询数据分级详细
export function getDataLevel(id) {
  return request({
    url: '/dg/dataLevel/' + id,
    method: 'get'
  })
}

// 新增数据分级
export function addDataLevel(data) {
  return request({
    url: '/dg/dataLevel',
    method: 'post',
    data: data
  })
}

// 修改数据分级
export function updateDataLevel(data) {
  return request({
    url: '/dg/dataLevel',
    method: 'put',
    data: data
  })
}

// 删除数据分级
export function delDataLevel(id) {
  return request({
    url: '/dg/dataLevel/' + id,
    method: 'delete'
  })
}
