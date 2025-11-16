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
import {encrypt} from "@/utils/aesEncrypt";

// 数据库信息api

// 获取表名
export function getTables(params) {
  return request({
    url: '/data/dts/metadata/getTables',
    method: 'get',
    params
  })
}

// 获取schema
export function getTableSchema(params) {
  return request({
    url: '/data/dts/metadata/getDBSchema',
    method: 'get',
    params
  })
}

// 获取字段
export function getColumns(params) {
  return request({
    url: '/data/dts/metadata/getColumns',
    method: 'get',
    params
  })
}

// 根据sql获取字段
export function getColumnsByQuerySql(data) {
  data = JSON.parse(JSON.stringify(data))
  data.querySql = encrypt(data.querySql)
  return request({
    url: '/data/dts/metadata/getColumnsByQuerySql2',
    method: 'post',
    data
  })
}

// 根据datasourceID、tablename创建表【目标端】
export function createTable(params) {
  return request({
    url: '/data/dts/metadata/createTable',
    method: 'post',
    params
  })
}
// 判断字段是否存在，存在，即更新值，否则添加字段
export function updateColumnsValue(query) {
  return request({
    url: '/data/dts/metadata/updateColumnsValue',
    method: 'post',
    data: query
  })
}

export function listCountByDbInfo(data) {
  return request({
    url: '/data/dts/metadata/listCountByDbInfo',
    method: 'post',
    data
  })
}
