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
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
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
