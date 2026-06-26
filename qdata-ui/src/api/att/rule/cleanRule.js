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

// 查询清洗规则列表
export function listAttCleanRule(query) {
  return request({
    url: '/att/cleanRule/list',
    method: 'get',
    params: query
  })
}

// 查询清洗规则详细
export function getAttCleanRule(id) {
  return request({
    url: '/att/cleanRule/' + id,
    method: 'get'
  })
}

// 新增清洗规则
export function addAttCleanRule(data) {
  return request({
    url: '/att/cleanRule',
    method: 'post',
    data: data
  })
}

// 修改清洗规则
export function updateAttCleanRule(data) {
  return request({
    url: '/att/cleanRule',
    method: 'put',
    data: data
  })
}

// 删除清洗规则
export function delAttCleanRule(id) {
  return request({
    url: '/att/cleanRule/' + id,
    method: 'delete'
  })
}

// tree
export function treeAttCleanRule(params) {
  return request({
    url: '/att/cleanRule/tree',
    method: 'get',
    params
  })
}

// 数据集成用到的 清洗规则
export function listAll(params) {
  return request({
    url: '/att/cleanRule/listAll',
    method: 'get',
    params
  })
}
