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

/** 查询数据源列表 */
export function listDppDatasource(query) {
  return request({
    url: '/dpp/datasource/list',
    method: 'get',
    params: query
  })
}

/** 查询数据源详情 */
export function getDppDatasource(id) {
  return request({
    url: '/dpp/datasource/' + id,
    method: 'get'
  })
}

/** 新增数据源 */
export function addDppDatasource(data) {
  return request({
    url: '/dpp/datasource',
    method: 'post',
    data
  })
}

/** 修改数据源 */
export function updateDppDatasource(data) {
  return request({
    url: '/dpp/datasource',
    method: 'put',
    data
  })
}

/** 删除数据源（删除前后端校验是否被数据资产或元数据采集任务引用） */
export function delDppDatasource(id) {
  return request({
    url: '/dpp/datasource/' + id,
    method: 'delete'
  })
}

/** 连接测试 */
export function testDppDatasource(id) {
  return request({
    url: '/dpp/datasource/test/' + id,
    method: 'post'
  })
}

/** 连接测试（未保存时传表单参数） */
export function testDppDatasourceForm(data) {
  return request({
    url: '/dpp/datasource/test',
    method: 'post',
    data
  })
}

/** 启用/禁用数据源 */
export function toggleDppDatasourceStatus(id) {
  return request({
    url: '/dpp/datasource/status/' + id,
    method: 'put'
  })
}
