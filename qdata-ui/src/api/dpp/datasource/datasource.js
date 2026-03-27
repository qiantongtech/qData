/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *
 * 数据源管理 API
 * 当前可切换为 datasource.mock.js 进行前端联调，对接后端时使用本文件 request 调用。
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
