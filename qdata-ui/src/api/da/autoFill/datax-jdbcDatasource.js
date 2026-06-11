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

// datax插件api

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
