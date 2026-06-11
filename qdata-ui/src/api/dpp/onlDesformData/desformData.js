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

import request from "@/utils/request";

// 查询在线单数据列表
export function listdata(query) {
  return request({
    url: "/dpp/data/list",
    method: "get",
    params: query,
  });
}

// 查询在线单数据详细
export function getdata(id) {
  return request({
    url: "/dpp/data/" + id,
    method: "get",
  });
}

// 新增在线单数据
export function adddata(data) {
  return request({
    url: "/dpp/data",
    method: "post",
    data: data,
  });
}

// 修改在线单数据
export function updatedata(data) {
  return request({
    url: "/dpp/data",
    method: "put",
    data: data,
  });
}

// 删除在线单数据
export function deldata(data) {
  return request({
    url: "/dpp/data/deleted",
    method: "delete",
    data: data,
  });
}
