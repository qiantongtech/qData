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

// 查询清洗规则类目列表
export function listAttCleanCat(query) {
  return request({
    url: '/att/cleanCat/list',
    method: 'get',
    params: query
  })
}

// 查询清洗规则类目详细
export function getAttCleanCat(ID) {
  return request({
    url: '/att/cleanCat/' + ID,
    method: 'get'
  })
}

// 新增清洗规则类目
export function addAttCleanCat(data) {
  return request({
    url: '/att/cleanCat',
    method: 'post',
    data: data
  })
}

// 修改清洗规则类目
export function updateAttCleanCat(data) {
  return request({
    url: '/att/cleanCat',
    method: 'put',
    data: data
  })
}

// 删除清洗规则类目
export function delAttCleanCat(ID) {
  return request({
    url: '/att/cleanCat/' + ID,
    method: 'delete'
  })
}
