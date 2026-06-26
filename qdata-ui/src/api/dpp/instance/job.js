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

// 查询数据集成任务实例列表
export function listDppEtlTaskInstance(query) {
  return request({
    url: '/dpp/etlTaskInstance/list',
    method: 'get',
    params: query
  })
}

// 查询数据集成任务实例详细
export function getDppEtlTaskInstance(id) {
  return request({
    url: '/dpp/etlTaskInstance/' + id,
    method: 'get'
  })
}

// 新增数据集成任务实例
export function addDppEtlTaskInstance(data) {
  return request({
    url: '/dpp/etlTaskInstance',
    method: 'post',
    data: data
  })
}

// 修改数据集成任务实例
export function updateDppEtlTaskInstance(data) {
  return request({
    url: '/dpp/etlTaskInstance',
    method: 'put',
    data: data
  })
}

// 删除数据集成任务实例
export function delDppEtlTaskInstance(id) {
  return request({
    url: '/dpp/etlTaskInstance/' + id,
    method: 'delete'
  })
}


// 查询数据集成任务实例列表
export function listDppEtlTreeList(query) {
  return request({
    url: '/dpp/etlTaskInstance/treeList',
    method: 'get',
    params: query
  })
}
// 获取子任务列表
export function subNodeList(query) {
  return request({
    url: '/dpp/etlTaskInstance/subNodeList',
    method: 'get',
    params: query
  })
}

