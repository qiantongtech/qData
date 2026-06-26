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

// 查询服务资源门户区域字典列表
export function rpLogin(username, password, code, uuid) {
  const data = {
    username,
    password,
    code,
    uuid,
  };
  return request({
    url: "/rp/login",
    headers: {
      isToken: false,
      repeatSubmit: false,
    },
    method: "post",
    data: data,
  });
}
// 获取用户详细信息
export function rpGetInfo() {
  return request({
    url: "/rp/getInfo",
    method: "get",
  });
}

// 退出方法
export function rpLogout() {
  return request({
    url: "/rp/logout",
    method: "post",
  });
}
// 获取验证码
export function rpCodeImg() {
  return request({
    url: "/rp/captchaImage",
    headers: {
      isToken: false,
    },
    method: "get",
    timeout: 20000,
  });
}
