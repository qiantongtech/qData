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
