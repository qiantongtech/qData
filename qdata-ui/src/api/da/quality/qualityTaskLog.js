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

// 查询数据质量日志列表
export function listDppQualityLog(query) {
  return request({
    url: '/dpp/qualityLog/list',
    method: 'get',
    params: query
  })
}

// 查询数据质量日志详细
export function getDppQualityLog(id) {
  return request({
    url: '/dpp/qualityLog/' + id,
    method: 'get'
  })
}

// 新增数据质量日志
export function addDppQualityLog(data) {
  return request({
    url: '/dpp/qualityTask',
    method: 'post',
    data: data
  })
}

// 修改数据质量日志
export function updateDppQualityLog(data) {
  return request({
    url: '/dpp/qualityLog',
    method: 'put',
    data: data
  })
}

// 删除数据质量日志
export function delDppQualityLog(id) {
  return request({
    url: '/dpp/qualityLog/' + id,
    method: 'delete'
  })
}
//发送消息
export function doSendMessage(id) {
  return request({
    url: '/dpp/qualityLog/sendMessage',
    method: 'POST',
    params: {id}
  })
}
// 日志数据质量维度统计
export function statisticsEvaluateOne(id) {
  return request({
    url: '/dpp/evaluateLog/statisticsEvaluateOne/' + id,
    method: 'get',
  })
}
// 日志详情   治理数据量变化趋势
export function statisticsEvaluateTow(query) {
  console.log("🚀 ~ statisticsEvaluateTow ~ query:", query)
  return request({
    url: '/dpp/evaluateLog/statisticsEvaluateTow',
    method: 'get',
    params: query
  })
}
//日志 规则列表
export function statisticsEvaluateTable(id) {
  return request({
    url: '/dpp/evaluateLog/statisticsEvaluateTable/' + id,
    method: 'get',
  })
}
//  错误数据
export function pageErrorData(query) {
  return request({
    url: '/dpp/evaluateLog/pageErrorData',
    method: 'get',
    params: query
  })
}
// 修改接口 数据、状态，都是这个接口
export function updateErrorData(data) {
  return request({
    url: 'dpp/evaluateLog/updateErrorData',
    method: 'post',
    data: data
  })
}

