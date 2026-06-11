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

