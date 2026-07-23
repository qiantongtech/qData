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

    ErrorCode SUCCESS = new ErrorCode(200, "Operation successful");
    ErrorCode ERROR = new ErrorCode(500, "Operation failed");

    // ========== Client Error Section ==========

    ErrorCode BAD_REQUEST = new ErrorCode(400, "Invalid request parameters");
    ErrorCode UNAUTHORIZED = new ErrorCode(401, "Account not logged in");
    ErrorCode FORBIDDEN = new ErrorCode(403, "No operation permission");
    ErrorCode NOT_FOUND = new ErrorCode(404, "Request not found");
    ErrorCode METHOD_NOT_ALLOWED = new ErrorCode(405, "Incorrect request method");
    ErrorCode LOCKED = new ErrorCode(423, "Request failed, please try again later"); // Concurrent requests, not allowed
    ErrorCode TOO_MANY_REQUESTS = new ErrorCode(429, "Too many requests, please try again later");

    // ========== Server error section ==========

    ErrorCode NOT_IMPLEMENTED = new ErrorCode(501, "Function not implemented or enabled");
    ErrorCode ERROR_CONFIGURATION = new ErrorCode(502, "Incorrect configuration item");

    // ========== Custom error section ==========
    ErrorCode REPEATED_REQUESTS = new ErrorCode(900, "Duplicate request, please try again later"); // Repeat request
    ErrorCode DEMO_DENY = new ErrorCode(901, "Demo mode prohibits write operations");

    ErrorCode UNKNOWN = new ErrorCode(999, "Unknown error");

}
