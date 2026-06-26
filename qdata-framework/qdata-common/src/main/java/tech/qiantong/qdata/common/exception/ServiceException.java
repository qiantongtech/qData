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
 * 业务异常
 *
 * @author qdata
 */
public final class ServiceException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    /**
     * 错误码（HTTP 状态码，保留向后兼容）
     */
    private Integer code;

    /**
     * i18n 消息键（对应 messages.properties 中的 key）
     */
    private String i18nCode;

    /**
     * 消息格式化参数
     */
    private Object[] args;

    /**
     * 错误提示（兜底消息）
     */
    private String message;

    /**
     * 错误明细，内部调试错误
     *
     * 和 {@link CommonResult#getDetailMessage()} 一致的设计
     */
    private String detailMessage;

    /**
     * 空构造方法，避免反序列化问题
     */
    public ServiceException()
    {
    }

    /**
     * 使用纯文本消息构造（不进行 i18n）
     */
    public ServiceException(String message)
    {
        this.message = message;
    }

    /**
     * 使用纯文本消息 + HTTP 状态码构造
     */
    public ServiceException(String message, Integer code)
    {
        this.message = message;
        this.code = code;
    }

    /**
     * 使用 i18n 消息键 + 兜底消息构造
     * 优先从资源文件按当前语言获取文案，获取不到则使用 defaultMessage
     *
     * @param i18nCode 消息键（如 "user.not.exists"）
     * @param defaultMessage 兜底消息
     * @param args 格式化参数（可替换 {0}、{1} 等占位符）
     */
    public ServiceException(String i18nCode, String defaultMessage, Object... args)
    {
        this.i18nCode = i18nCode;
        this.message = defaultMessage;
        this.args = args;
    }

    /**
     * 使用 i18n 消息键 + 兜底消息 + HTTP 状态码构造
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
     * 获取国际化后的消息文本
     * 优先从 i18n 资源文件获取，兜底使用 message 字段
     */
    @Override
    public String getMessage()
    {
        if (!StringUtils.isEmpty(i18nCode))
        {
            // 通过 MessageUtils 获取当前语言文案，支持兜底链
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
     * 获取 i18n 消息键
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
