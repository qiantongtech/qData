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

package tech.qiantong.qdata.common.exception;

import lombok.Data;
import tech.qiantong.qdata.common.exception.enums.GlobalErrorCodeConstants;
import tech.qiantong.qdata.common.utils.MessageUtils;

/**
 * Error code object
 *
 * Global error code, occupies [0, 999], see {@link GlobalErrorCodeConstants}
 * Business exception error code, occupies [1 000 000 000, +∞), see {@link ServiceErrorCodeRange}
 *
 * i18n internationalization: getMsg() preferentially reads from the resource file (key = "error.{code}"). If it cannot find it, the default message passed in the constructor is used.
 * Resource file: qdata-server/src/main/resources/i18n/messages*.properties
 *
 * @author qdata
 */
@Data
public class ErrorCode {

    /**
     * Error code
     */
    private final Integer code;
    /**
     * Error message (default message, clarification when i18n acquisition fails)
     */
    private final String msg;

    public ErrorCode(Integer code, String message) {
        this.code = code;
        this.msg = message;
    }

    /**
     * Get the error message after internationalization
     * Prioritize getting the copy of key = "error.{code}" from the i18n resource file
     * If it cannot be obtained, use the msg passed in the constructor as a backup
     */
    public String getMsg() {
        return MessageUtils.messageWithFallback("error." + code, msg);
    }

}
