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

package tech.qiantong.qdata.common.exception.enums;


import tech.qiantong.qdata.common.exception.ErrorCode;

/**
 * Global error code enumeration
 * 0-999 system exception coding reserved
 *
 * Generally, use the HTTP response status code https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Status
 * Although the HTTP response status code is weak in expressiveness for business use, it is still very good at the system level.
 * What's special is that because 0 has been used as success before, 200 is no longer used.
 *
 * @author taro source code
 */
public interface GlobalErrorCodeConstants {

    ErrorCode SUCCESS = new ErrorCode(200, "操作成功");
    ErrorCode ERROR = new ErrorCode(500, "操作失败");

    // ========== Client Error Section ==========

    ErrorCode BAD_REQUEST = new ErrorCode(400, "请求参数不正确");
    ErrorCode UNAUTHORIZED = new ErrorCode(401, "账号未登录");
    ErrorCode FORBIDDEN = new ErrorCode(403, "没有该操作权限");
    ErrorCode NOT_FOUND = new ErrorCode(404, "请求未找到");
    ErrorCode METHOD_NOT_ALLOWED = new ErrorCode(405, "请求方法不正确");
    ErrorCode LOCKED = new ErrorCode(423, "请求失败，请稍后重试"); // Concurrent requests, not allowed
    ErrorCode TOO_MANY_REQUESTS = new ErrorCode(429, "请求过于频繁，请稍后重试");

    // ========== Server error section ==========

    ErrorCode NOT_IMPLEMENTED = new ErrorCode(501, "功能未实现/未开启");
    ErrorCode ERROR_CONFIGURATION = new ErrorCode(502, "错误的配置项");

    // ========== Custom error section ==========
    ErrorCode REPEATED_REQUESTS = new ErrorCode(900, "重复请求，请稍后重试"); // Repeat request
    ErrorCode DEMO_DENY = new ErrorCode(901, "演示模式，禁止写操作");

    ErrorCode UNKNOWN = new ErrorCode(999, "未知错误");

}
