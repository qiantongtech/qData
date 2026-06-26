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

import request from "@/utils/rpRequest";

// 资料类型
export function catApiList(query) {
  return request({
    url: "/rp/attDoc/catApi/list",
    method: "get",
    params: query,
  });
}
// 左侧树
export function rpDictList(query) {
  return request({
    url: `/rp/rpDict/list`,
    method: "get",
    params: query,
  });
}

// 列表
export function rpDocList(query) {
  return request({
    url: "/rp/rpDoc/list",
    method: "get",
    params: query,
  });
}

// 新增
export function rpDocAdd(data) {
  return request({
    url: "/rp/rpDoc",
    method: "post",
    data: data,
  });
}
// 删除
export function rpDocDel(id) {
  return request({
    url: "/rp/rpDoc/" + id,
    method: "delete",
  });
}
// 字典
export function dictDataList(dictType) {
  return request({
    url: `/rp/dictData/type/${dictType}`,
    method: "get",
  });
}
