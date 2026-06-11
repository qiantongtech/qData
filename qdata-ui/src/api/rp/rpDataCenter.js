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
