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
 * Business License: https://docs.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

import request from "@/utils/request";

export function listDesensitizationRules(query) {
  return request({
    url: "/dg/safety/desensitizationRules/list",
    method: "get",
    params: query,
  });
}

export function getDesensitizationRule(id) {
  return request({
    url: "/dg/safety/desensitizationRules/" + id,
    method: "get",
  });
}

export function addDesensitizationRule(data) {
  return request({
    url: "/dg/safety/desensitizationRules",
    method: "post",
    data,
  });
}

export function updateDesensitizationRule(data) {
  return request({
    url: "/dg/safety/desensitizationRules",
    method: "put",
    data,
  });
}

export function delDesensitizationRule(id) {
  return request({
    url: "/dg/safety/desensitizationRules/" + id,
    method: "delete",
  });
}

