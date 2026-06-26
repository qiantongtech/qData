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

package tech.qiantong.qdata.common.database.exception;

import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.StringUtils;

/**
 * 数据查询异常
 * 支持 i18n 国际化：优先从资源文件获取对应语言文案
 *
 * @author qdata
 */
public class DataQueryException extends RuntimeException {

    /**
     * i18n 消息键
     */
    private String i18nCode;

    /**
     * 消息格式化参数
     */
    private Object[] args;

    /**
     * 纯文本构造
     */
    public DataQueryException(String message) {
        super(message);
    }

    /**
     * 使用 i18n 消息键 + 兜底消息构造
     *
     * @param i18nCode       消息键（如 "db.error.connection"）
     * @param defaultMessage 兜底消息
     * @param args           格式化参数
     */
    public DataQueryException(String i18nCode, String defaultMessage, Object... args) {
        super(defaultMessage);
        this.i18nCode = i18nCode;
        this.args = args;
    }

    @Override
    public String getMessage() {
        String defaultMsg = super.getMessage();
        if (!StringUtils.isEmpty(i18nCode)) {
            return MessageUtils.messageWithFallback(i18nCode, defaultMsg, args);
        }
        return defaultMsg;
    }
}
