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

package tech.qiantong.qdata.ai.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * <P>
 * 用途:
 * </p>
 *
 * @author: FXB
 * @create: 2026-04-12 10:49
 **/
@Getter
@AllArgsConstructor
public enum ChatMessageTypeEnum {

    CHAT("1", "知识图表"),
    MSG("2", "纯文本"),
    SQL("3", "SQL"),
    ERROR("500", "报错异常");

    /**
     * 类型
     */
    private final String type;
    /**
     * 名称
     */
    private final String name;

    public static ChatMessageTypeEnum getByType(String type) {
        return Arrays.stream(values())
                .filter(e -> e.getType().equals(type))
                .findFirst().orElse(null);
    }
}
