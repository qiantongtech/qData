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

// Query online single data list
export function listdata(query) {
  return request({
    url: "/dpp/data/list",
    method: "get",
    params: query,
  });
}

// Query detailed online order data
export function getdata(id) {
  return request({
    url: "/dpp/data/" + id,
    method: "get",
  });
}

// Add online order data
export function adddata(data) {
  return request({
    url: "/dpp/data",
    method: "post",
    data: data,
  });
}

// Modify online order data
export function updatedata(data) {
  return request({
    url: "/dpp/data",
    method: "put",
    data: data,
  });
}

// Delete online order data
export function deldata(data) {
  return request({
    url: "/dpp/data/deleted",
    method: "delete",
    data: data,
  });
}
