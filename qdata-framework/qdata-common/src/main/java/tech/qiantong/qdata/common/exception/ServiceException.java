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

import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.StringUtils;

/**
 * Business abnormality
 *
 * @author qdata
 */
public final class ServiceException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    /**
     * Error code (HTTP status code, retained for backward compatibility)
     */
    private Integer code;

    /**
     * i18n message key (corresponding to the key in messages.properties)
     */
    private String i18nCode;

    /**
     * Message formatting parameters
     */
    private Object[] args;

    /**
     * Error message (secret message)
     */
    private String message;

    /**
     * Error details, internal debugging errors
     *
     * Design consistent with {@link CommonResult#getDetailMessage()}
     */
    private String detailMessage;

    /**
     * Empty constructor to avoid deserialization problems
     */
    public ServiceException()
    {
    }

    /**
     * Use plain text message construction (no i18n)
     */
    public ServiceException(String message)
    {
        this.message = message;
    }

    /**
     * Constructed using plain text message + HTTP status code
     */
    public ServiceException(String message, Integer code)
    {
        this.message = message;
        this.code = code;
    }

    /**
     * Use i18n message key + hidden message structure
     * Prioritize getting the copy in the current language from the resource file. If it cannot be obtained, use defaultMessage.
     *
     * @param i18nCode message key (such as "user.not.exists")
     * @param defaultMessage divulge message
     * @param args format parameters (can replace {0}, {1} and other placeholders)
     */
    public ServiceException(String i18nCode, String defaultMessage, Object... args)
    {
        this.i18nCode = i18nCode;
        this.message = defaultMessage;
        this.args = args;
    }

    /**
     * Use i18n message key + cryptic message + HTTP status code structure
     */
    public ServiceException(String i18nCode, String defaultMessage, Integer code, Object... args)
    {
        this.i18nCode = i18nCode;
        this.message = defaultMessage;
        this.code = code;
        this.args = args;
    }

    public String getDetailMessage()
    {
        return detailMessage;
    }

    /**
     * Get the internationalized message text
     * Get it from the i18n resource file first, and use the message field for details.
     */
    @Override
    public String getMessage()
    {
        if (!StringUtils.isEmpty(i18nCode))
        {
            // Obtain the current language copy through MessageUtils and support the backend chain
            String i18nMessage = MessageUtils.messageWithFallback(i18nCode, message, args);
            if (i18nMessage != null)
            {
                return i18nMessage;
            }
        }
        return message;
    }

    public Integer getCode()
    {
        return code;
    }

    /**
     * Get i18n message key
     */
    public String getI18nCode()
    {
        return i18nCode;
    }

    public Object[] getArgs()
    {
        return args;
    }

    public ServiceException setMessage(String message)
    {
        this.message = message;
        return this;
    }

    public ServiceException setDetailMessage(String detailMessage)
    {
        this.detailMessage = detailMessage;
        return this;
    }
}
