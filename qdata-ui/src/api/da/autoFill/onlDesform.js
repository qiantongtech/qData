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

import request from "@/utils/request";

/**
 * 分页列表
 * @param params
 * @returns {AxiosPromise}
 */
export function pageData(params) {
  return request({
    url: "/data/masterdata/onlDesform/page",
    method: "get",
    params,
  });
}

/**
 * 添加
 * @param data
 * @returns {AxiosPromise}
 */
export function add(data) {
  return request({
    url: "/data/masterdata/onlDesform/add",
    method: "post",
    data,
  });
}

/**
 * 编辑
 * @param data
 * @returns {AxiosPromise}
 */
export function edit(data) {
  return request({
    url: "/data/masterdata/onlDesform/edit",
    method: "post",
    data,
  });
}

/**
 * 删除
 * @param params
 * @returns {AxiosPromise}
 */
export function deleted(params) {
  return request({
    url: "/data/masterdata/onlDesform/deleted",
    method: "post",
    params,
  });
}

/**
 * 通过表单编码查询
 * @param params
 * @returns {AxiosPromise}
 */
export function getByDesformCode(desformCode) {
  return request({
    url: "/data/masterdata/onlDesform/" + desformCode,
    method: "get",
  });
}

export function duplicateCheck(params) {
  return request({
    url: "/data/masterdata/onlDesform/duplicateCheck",
    method: "get",
    params,
  });
}
