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

// 查询数据质量任务列表
export function listDppQualityTask(query) {
  return request({
    url: '/dpp/qualityTask/list',
    method: 'get',
    params: query
  })
}

// 查询数据质量任务详细
export function getDppQualityTask(id) {
  return request({
    url: '/dpp/qualityTask/' + id,
    method: 'get'
  })
}

// 新增数据质量任务
export function addDppQualityTask(data) {
  return request({
    url: '/dpp/qualityTask',
    method: 'post',
    data: data
  })
}

// 修改数据质量任务
export function updateDppQualityTask(data) {
  return request({
    url: '/dpp/qualityTask',
    method: 'put',
    data: data
  })
}

// 删除数据质量任务
export function delDppQualityTask(id) {
  return request({
    url: '/dpp/qualityTask/' + id,
    method: 'delete'
  })
}


//检验接口
export function verifyInterfaceValue(query) {
  return request({
    url: '/dpp/qualityTaskEvaluate/verifyInterfaceValue',
    method: 'get',
    params: query
  })
}
//错误抽查功能
export function validationErrorDataSql(data) {
  return request({
    url: '/dpp/qualityTaskEvaluate/validationErrorDataSql',
    method: 'post',
    data: data
  })
}
// 成功抽查功能
export function validationValidDataSql(data) {
  return request({
    url: '/dpp/qualityTaskEvaluate/validationValidDataSql',
    method: 'post',
    data: data
  })
}
//执行一次
export function startDppQualityTask(id) {
  return request({
    url: `/dpp/qualityTask//startDppQualityTask/${id}`,
    method: 'put',
  })
}
// 任务开关
export function updateDppQualityTaskStatus(query) {
  return request({
    url: '/dpp/qualityTask/updateDppQualityTaskStatus',
    method: 'post',
    data: query
  })
}

// 调度周期

export function updateDaDiscoveryTaskCronExpression(query) {
  return request({
    url: '/dpp/qualityTask/updateDaDiscoveryTaskCronExpression',
    method: 'post',
    data: query
  })
}// 数据质量 查询资产质量详情

export function getQualityTaskAsset(query) {
  return request({
    url: '/dpp/qualityTask/getQualityTaskAsset',
    method: 'get',
    params: query
  });
}
// 数据质量 日志数据质量维度统计

export function statisticsEvaluateAssetOne(query) {
  return request({
    url: 'dpp/evaluateLog/statisticsEvaluateAssetOne',
    method: 'get',
    params: query
  });
}
// 查看日志

export function qualityLogLogDetailCat(query) {
  return request({
    url: '/dpp/qualityLog/logDetailCat',
    method: 'get',
    params: query
  });
}
