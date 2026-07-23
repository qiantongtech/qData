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

/** Query data source list */
export function listDppDatasource(query) {
  return request({
    url: '/dpp/datasource/list',
    method: 'get',
    params: query
  })
}

/** Query data source details */
export function getDppDatasource(id) {
  return request({
    url: '/dpp/datasource/' + id,
    method: 'get'
  })
}

/** Add new data source */
export function addDppDatasource(data) {
  return request({
    url: '/dpp/datasource',
    method: 'post',
    data
  })
}

/** Modify data source */
export function updateDppDatasource(data) {
  return request({
    url: '/dpp/datasource',
    method: 'put',
    data
  })
}

/** Delete the data source (check whether the deletion front-end and back-end are referenced by data assets or metadata collection tasks) */
export function delDppDatasource(id) {
  return request({
    url: '/dpp/datasource/' + id,
    method: 'delete'
  })
}

/** Connection test */
export function testDppDatasource(id) {
  return request({
    url: '/dpp/datasource/test/' + id,
    method: 'post'
  })
}

/** Connection test (passing form parameters when not saved) */
export function testDppDatasourceForm(data) {
  return request({
    url: '/dpp/datasource/test',
    method: 'post',
    data
  })
}

/** Enable/disable data source */
export function toggleDppDatasourceStatus(id) {
  return request({
    url: '/dpp/datasource/status/' + id,
    method: 'put'
  })
}
