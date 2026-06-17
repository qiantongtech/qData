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
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
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
