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

// Query the data domain management list
export function listDataDomain(query) {
  return request({
    url: '/dm/dataDomain/list',
    method: 'get',
    params: query
  })
}

// Query data domain management details
export function getDataDomain(id) {
  return request({
    url: '/dm/dataDomain/' + id,
    method: 'get'
  })
}

// Added data domain management
export function addDataDomain(data) {
  return request({
    url: '/dm/dataDomain',
    method: 'post',
    data: data
  })
}

// Modify data domain management
export function updateDataDomain(data) {
  return request({
    url: '/dm/dataDomain',
    method: 'put',
    data: data
  })
}

// Delete data domain management
export function delDataDomain(id) {
  return request({
    url: '/dm/dataDomain/' + id,
    method: 'delete'
  })
}

// Query business classification
export function listDataDomainlist(query) {
  return request({
    url: '/dm/dataDomain/listByCategoryId',
    method: 'get',
    params: query
  })
}

// Batch deletion of data domain management and business hierarchical binding
export function getDeletebyDomainId(query) {
  return request({
    url: '/dm/BusinessDomainRel/deletebyDomainId/' + query.domainId + '/' + query.businessCategoryId,
    method: 'delete'
  })
}
