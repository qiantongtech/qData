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

// Query the list of online single designers
export function listdesform(query) {
  return request({
    url: "/dpp/desForm/list",
    method: "get",
    params: query,
  });
}

// Query online single designer details
export function getdesform(id) {
  return request({
    url: "/dpp/desForm/" + id,
    method: "get",
  });
}

// Added online single designer
export function add(data) {
  return request({
    url: "/dpp/desForm",
    method: "post",
    data: data,
  });
}

// Modify online single designer
export function edit(data) {
  return request({
    url: "/dpp/desForm",
    method: "put",
    data: data,
  });
}

// Delete online single designer
export function deldesform(id) {
  return request({
    url: "/dpp/desForm/deleted?id=" + id,
    method: "delete",
  });
}
// Whether the form code is repeated and verified
export function duplicateCheck(query) {
  return request({
    url: "/dpp/desForm/duplicateCheck",
    method: "get",
    params: query,
  });
}
// Query via form encoding
export function getByDesformCode(desformCode) {
  return request({
    url: "/dpp/desForm/" + desformCode,
    method: "get",
  });
}
