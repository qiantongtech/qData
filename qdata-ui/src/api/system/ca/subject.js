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

import request from '@/utils/request.js'

// 查询主体管理列表
export function listSubject(query) {
  return request({
    url: '/ca/subject/list',
    method: 'get',
    params: query
  })
}

// 查询主体管理详细
export function getSubject(id) {
  return request({
    url: '/ca/subject/' + id,
    method: 'get'
  })
}

// 新增主体管理
export function addSubject(data) {
  return request({
    url: '/ca/subject',
    method: 'post',
    data: data
  })
}

// 修改主体管理
export function updateSubject(data) {
  return request({
    url: '/ca/subject',
    method: 'put',
    data: data
  })
}

// 删除主体管理
export function delSubject(id) {
  return request({
    url: '/ca/subject/' + id,
    method: 'delete'
  })
}
