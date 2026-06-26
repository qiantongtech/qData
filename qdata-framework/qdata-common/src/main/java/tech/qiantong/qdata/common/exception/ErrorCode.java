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
 * 错误码对象
 *
 * 全局错误码，占用 [0, 999]，参见 {@link GlobalErrorCodeConstants}
 * 业务异常错误码，占用 [1 000 000 000, +∞)，参见 {@link ServiceErrorCodeRange}
 *
 * i18n 国际化：getMsg() 优先从资源文件读取（key = "error.{code}"），找不到则使用构造函数传入的默认 message
 * 资源文件：qdata-server/src/main/resources/i18n/messages*.properties
 *
 * @author qdata
 */
@Data
public class ErrorCode {

    /**
     * 错误码
     */
    private final Integer code;
    /**
     * 错误提示（默认消息，i18n 获取失败时的兜底）
     */
    private final String msg;

    public ErrorCode(Integer code, String message) {
        this.code = code;
        this.msg = message;
    }

    /**
     * 获取国际化后的错误提示
     * 优先从 i18n 资源文件获取 key = "error.{code}" 的文案
     * 获取不到则使用构造函数传入的 msg 作为兜底
     */
    public String getMsg() {
        return MessageUtils.messageWithFallback("error." + code, msg);
    }

}
