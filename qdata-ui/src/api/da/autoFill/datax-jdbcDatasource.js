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

// datax plugin api

export function list(params) {
  return request({
    url: '/data/dts/jobJdbcDatasource',
    method: 'get',
    params
  })
}

export function fetched(params) {
  return request({
    url: '/data/dts/jobJdbcDatasource/' + params,
    method: 'get'
  })
}
export function getJDBCDataSource(id) {
  return request({
    url: '/data/dts/jobJdbcDatasource/' + id,
    method: 'get'
  })
}

export function getDbTablesize(id) {
  return request({
    url: '/data/dts/jobJdbcDatasource/' + id + '/tablesize',
    method: 'get'
  })
}
export function updated(data) {
  return request({
    url: '/data/dts/jobJdbcDatasource',
    method: 'put',
    data
  })
}

export function created(data) {
  return request({
    url: '/data/dts/jobJdbcDatasource',
    method: 'post',
    data
  })
}

export function deleted(data) {
  return request({
    url: '/data/dts/jobJdbcDatasource',
    method: 'delete',
    params: data
  })
}

export function test(data) {
  return request({
    url: '/data/dts/jobJdbcDatasource/test',
    method: 'post',
    data
  })
}

export function getDataSourceList(params) {
  return request({
    url: '/data/dts/jobJdbcDatasource/all',
    method: 'get',
    params
  })
}
