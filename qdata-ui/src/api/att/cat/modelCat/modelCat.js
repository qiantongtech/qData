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
 * For brand customization, please contact the official team for authorization.
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
 * 如需定制品牌，请通过官方渠道申请品牌授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

import request from '@/utils/request.js'

// 查询逻辑模型类目管理列表
export function listAttModelCat(query) {
  return request({
    url: '/att/modelCat/list',
    method: 'get',
    params: query
  })
}

// 查询逻辑模型类目管理详细
export function getAttModelCat(ID) {
  return request({
    url: '/att/modelCat/' + ID,
    method: 'get'
  })
}

// 新增逻辑模型类目管理
export function addAttModelCat(data) {
  return request({
    url: '/att/modelCat',
    method: 'post',
    data: data
  })
}

// 修改逻辑模型类目管理
export function updateAttModelCat(data) {
  return request({
    url: '/att/modelCat',
    method: 'put',
    data: data
  })
}

// 删除逻辑模型类目管理
export function delAttModelCat(ID) {
  return request({
    url: '/att/modelCat/' + ID,
    method: 'delete'
  })
}
