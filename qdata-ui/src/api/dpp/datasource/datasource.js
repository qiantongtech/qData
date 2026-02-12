/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *
 * More information: https://qdata.qiantong.tech/business.html
 *
 * ============================================================================
 *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

import request from '@/utils/request';

/**
 * 查询数据源列表
 * @param {Object} query - 查询参数
 */
export function listDatasource(query) {
  return request({
    url: '/dpp/datasource/list',
    method: 'get',
    params: query
  });
}

/**
 * 查询数据源详情
 * @param {string|number} id - 数据源ID
 */
export function getDatasource(id) {
  return request({
    url: '/dpp/datasource/' + id,
    method: 'get'
  });
}

/**
 * 新增数据源
 * @param {Object} data - 数据源表单
 */
export function addDatasource(data) {
  return request({
    url: '/dpp/datasource',
    method: 'post',
    data
  });
}

/**
 * 修改数据源
 * @param {Object} data - 数据源表单
 */
export function updateDatasource(data) {
  return request({
    url: '/dpp/datasource',
    method: 'put',
    data
  });
}

/**
 * 删除数据源（删除前后端校验引用）
 * @param {string|number} id - 数据源ID
 */
export function removeDatasource(id) {
  return request({
    url: '/dpp/datasource/' + id,
    method: 'delete'
  });
}

/**
 * 连接测试
 * @param {string|number} id - 数据源ID
 */
export function testDatasource(id) {
  return request({
    url: '/dpp/datasource/test/' + id,
    method: 'get'
  });
}

/**
 * 启用/禁用数据源
 * @param {string|number} id - 数据源ID
 * @param {string} status - enabled | disabled
 */
export function toggleDatasourceStatus(id, status) {
  return request({
    url: '/dpp/datasource/status',
    method: 'put',
    data: { id, status }
  });
}
