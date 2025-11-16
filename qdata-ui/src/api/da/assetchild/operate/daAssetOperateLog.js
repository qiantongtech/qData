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

// 查询数据资产操作记录列表
export function listDaAssetOperateLog(query) {
  return request({
    url: '/da/assetOperateLog/list',
    method: 'get',
    params: query
  })
}

// 查询数据资产操作记录详细
export function getDaAssetOperateLog(id) {
  return request({
    url: '/da/assetOperateLog/' + id,
    method: 'get'
  })
}

// 新增数据资产操作记录
export function addDaAssetOperateLog(data) {
  return request({
    url: '/da/assetOperateLog',
    method: 'post',
    data: data
  })
}

// 修改数据资产操作记录
export function updateDaAssetOperateLog(data) {
  return request({
    url: '/da/assetOperateLog',
    method: 'put',
    data: data
  })
}

// 删除数据资产操作记录
export function delDaAssetOperateLog(id) {
  return request({
    url: '/da/assetOperateLog/' + id,
    method: 'delete'
  })
}
// 回滚
export function rollBack(id) {
  return request({
    url: `/da/assetOperateLog/rollBack/${id}`,
    method: 'post'
  })
}


// 修改记录
export function getDaAssetList(query) {
  return request({
    url: '/da/assetOperateLog/queryDaAssetOperateLogPage',
    method: 'get',
    params: query
  })
}
// // 回滚
// export function rollBack(id) {
//   return request({
//     url: `/da/assetOperateLog/rollBack/${id}`,
//     method: 'get',
//   })
// }
