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
