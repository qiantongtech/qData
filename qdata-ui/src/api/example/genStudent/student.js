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

// 查询学生列表
export function listStudent(query) {
  return request({
    url: '/example/student/list',
    method: 'get',
    params: query
  })
}

// 查询学生详细
export function getStudent(id) {
  return request({
    url: '/example/student/' + id,
    method: 'get'
  })
}

// 新增学生
export function addStudent(data) {
  return request({
    url: '/example/student',
    method: 'post',
    data: data
  })
}

// 修改学生
export function updateStudent(data) {
  return request({
    url: '/example/student',
    method: 'put',
    data: data
  })
}

// 删除学生
export function delStudent(id) {
  return request({
    url: '/example/student/' + id,
    method: 'delete'
  })
}
