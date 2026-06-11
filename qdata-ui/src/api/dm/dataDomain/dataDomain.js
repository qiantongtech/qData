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

// 查询数据域管理列表
export function listDataDomain(query) {
  return request({
    url: '/dm/dataDomain/list',
    method: 'get',
    params: query
  })
}

// 查询数据域管理详细
export function getDataDomain(id) {
  return request({
    url: '/dm/dataDomain/' + id,
    method: 'get'
  })
}

// 新增数据域管理
export function addDataDomain(data) {
  return request({
    url: '/dm/dataDomain',
    method: 'post',
    data: data
  })
}

// 修改数据域管理
export function updateDataDomain(data) {
  return request({
    url: '/dm/dataDomain',
    method: 'put',
    data: data
  })
}

// 删除数据域管理
export function delDataDomain(id) {
  return request({
    url: '/dm/dataDomain/' + id,
    method: 'delete'
  })
}

// 查询业务分类
export function listDataDomainlist(query) {
  return request({
    url: '/dm/dataDomain/listByCategoryId',
    method: 'get',
    params: query
  })
}

// 批量删除数据域管理 业务分层绑定的
export function getDeletebyDomainId(query) {
  return request({
    url: '/dm/BusinessDomainRel/deletebyDomainId/' + query.domainId + '/' + query.businessCategoryId,
    method: 'delete'
  })
}
