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

package tech.qiantong.qdata.quartz.enums;

/**
 * 任务操作错误信息枚举
 *
 * @author qdata
 */
public enum JobErrorEnum {

    CRON_INVALID(-1L, "Cron表达式不正确"),
    RMI_NOT_ALLOWED(-2L, "目标字符串不允许'rmi'调用"),
    LDAP_NOT_ALLOWED(-3L, "目标字符串不允许'ldap(s)'调用"),
    HTTP_NOT_ALLOWED(-4L, "目标字符串不允许'http(s)'调用"),
    INVALID_TARGET(-5L, "目标字符串存在违规"),
    NOT_IN_WHITELIST(-6L, "目标字符串不在白名单内");

    private final Long code;
    private final String message;

    JobErrorEnum(Long code, String message) {
        this.code = code;
        this.message = message;
    }

    public Long getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getMessage(String jobName, String operationType) {
        return String.format("%s任务'%s'失败，%s", operationType, jobName, this.message);
    }

    /**
     * 根据错误码获取枚举
     */
    public static JobErrorEnum getByCode(Long code) {
        for (JobErrorEnum error : values()) {
            if (error.getCode().equals(code)) {
                return error;
            }
        }
        return null;
    }
}
