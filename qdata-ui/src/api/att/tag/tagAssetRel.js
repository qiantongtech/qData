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

// 查询标签与资产关联关系列表
export function listAttTagAssetRel(query) {
  return request({
    url: '/att/tagAssetRel/list',
    method: 'get',
    params: query
  })
}

// 查询标签与资产关联关系详细
export function getAttTagAssetRel(ID) {
  return request({
    url: '/att/tagAssetRel/' + ID,
    method: 'get'
  })
}

// 新增标签与资产关联关系
export function addAttTagAssetRel(data) {
  return request({
    url: '/att/tagAssetRel',
    method: 'post',
    data: data
  })
}

// 修改标签与资产关联关系
export function updateAttTagAssetRel(data) {
  return request({
    url: '/att/tagAssetRel',
    method: 'put',
    data: data
  })
}

// 删除标签与资产关联关系
export function delAttTagAssetRel(ID) {
  return request({
    url: '/att/tagAssetRel/' + ID,
    method: 'delete'
  })
}

export function delByTagIdAndAesstId(query) {
  return request({
    url: '/att/tagAssetRel/delByTagIdAndAesstId',
    method: 'delete',
    params: query
  })
}

