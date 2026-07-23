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

// Query the data integration task category management list
const buildHeaders = (options = {}) => ({
  hideErrorMessage: options.hideErrorMessage === true
})

export function listAttTaskCat(query, options = {}) {
  return request({
    url: '/att/taskCat/list',
    method: 'get',
    params: query,
    headers: buildHeaders(options)
  })
}

// Query data integration task category management details
export function getAttTaskCat(id, options = {}) {
  return request({
    url: '/att/taskCat/' + id,
    method: 'get',
    headers: buildHeaders(options)
  })
}

// Check whether data integration tasks exist under the category
export function hasIntegrationTask(id, options = {}) {
  return request({
    url: '/att/taskCat/hasIntegrationTask/' + id,
    method: 'get',
    headers: buildHeaders(options)
  })
}

// Check whether the category name is already used under the same parent
export function isTaskCatNameUsed(query, options = {}) {
  return request({
    url: '/att/taskCat/nameUsed',
    method: 'get',
    params: query,
    headers: buildHeaders(options)
  })
}

// Query the number of data integration tasks associated with the category
export function getTaskCatIntegrationTaskCount(id, options = {}) {
  return request({
    url: '/att/taskCat/integrationTaskCount/' + id,
    method: 'get',
    headers: buildHeaders(options)
  })
}

// Added data integration task category management
export function addAttTaskCat(data, options = {}) {
  return request({
    url: '/att/taskCat',
    method: 'post',
    data: data,
    headers: buildHeaders(options)
  })
}

// Modify data integration task category management
export function updateAttTaskCat(data, options = {}) {
  return request({
    url: '/att/taskCat',
    method: 'put',
    data: data,
    headers: buildHeaders(options)
  })
}

// Delete data integration task category management
export function delAttTaskCat(id, options = {}) {
  return request({
    url: '/att/taskCat/' + id,
    method: 'delete',
    headers: buildHeaders(options)
  })
}
