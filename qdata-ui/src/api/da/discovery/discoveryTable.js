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

// 查询数据发现库信息列表
export function listDaDiscoveryTable(query) {
  return request({
    url: '/da/discoveryTable/list',
    method: 'get',
    params: query
  })
}

// 查询数据发现库信息列表
export function getDaDiscoveryTableList(query) {
  return request({
    url: '/da/discoveryTable/getDaDiscoveryTableList',
    method: 'get',
    params: query
  })
}

// 查询数据发现库信息详细
export function getDaDiscoveryTable(id) {
  return request({
    url: '/da/discoveryTable/' + id,
    method: 'get'
  })
}

// 新增数据发现库信息
export function addDaDiscoveryTable(data) {
  return request({
    url: '/da/discoveryTable',
    method: 'post',
    data: data
  })
}
// 新增数据发现库信息
export function commitOrRevokeDiscoveryInfo(data) {
  return request({
    url: '/da/discoveryTable/commitOrRevokeDiscoveryInfo',
    method: 'post',
    data: data
  })
}

// 修改数据发现库信息
export function updateDaDiscoveryTable(data) {
  return request({
    url: '/da/discoveryTable',
    method: 'put',
    data: data
  })
}

// 删除数据发现库信息
export function delDaDiscoveryTable(id) {
  return request({
    url: '/da/discoveryTable/' + id,
    method: 'delete'
  })
}
// 表字段
export function getDaDiscoveryColumnList(params) {
  return request({
    url: '/da/discoveryColumn/getDaDiscoveryColumnList',
    method: 'get',
    params: params
  })
}

export function preview(data) {
  return request({
    url: '/da/discoveryTable/preview',
    method: 'post',
    data: data
  })
}
