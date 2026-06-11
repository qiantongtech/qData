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
