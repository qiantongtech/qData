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

// Query the data development category management list
const buildHeaders = (options = {}) => ({
  hideErrorMessage: options.hideErrorMessage === true
})

export function listAttDataDevCat(query, options = {}) {
  return request({
    url: '/att/dataDevCat/list',
    method: 'get',
    params: query,
    headers: buildHeaders(options)
  })
}

// Query data development category management details
export function getAttDataDevCat(id, options = {}) {
  return request({
    url: '/att/dataDevCat/' + id,
    method: 'get',
    headers: buildHeaders(options)
  })
}

// Check whether data development tasks exist under the category
export function hasDataDevelopmentTask(id, options = {}) {
  return request({
    url: '/att/dataDevCat/hasDataDevelopmentTask/' + id,
    method: 'get',
    headers: buildHeaders(options)
  })
}

// Check whether the category name is already used under the same parent
export function isDataDevCatNameUsed(query, options = {}) {
  return request({
    url: '/att/dataDevCat/nameUsed',
    method: 'get',
    params: query,
    headers: buildHeaders(options)
  })
}

// Query the number of data development tasks associated with the category
export function getDataDevelopmentTaskCount(id, options = {}) {
  return request({
    url: '/att/dataDevCat/dataDevelopmentTaskCount/' + id,
    method: 'get',
    headers: buildHeaders(options)
  })
}

// Added new data development category management
export function addAttDataDevCat(data, options = {}) {
  return request({
    url: '/att/dataDevCat',
    method: 'post',
    data: data,
    headers: buildHeaders(options)
  })
}

// Modify data development category management
export function updateAttDataDevCat(data, options = {}) {
  return request({
    url: '/att/dataDevCat',
    method: 'put',
    data: data,
    headers: buildHeaders(options)
  })
}

// Delete data development category management
export function delAttDataDevCat(id, options = {}) {
  return request({
    url: '/att/dataDevCat/' + id,
    method: 'delete',
    headers: buildHeaders(options)
  })
}
