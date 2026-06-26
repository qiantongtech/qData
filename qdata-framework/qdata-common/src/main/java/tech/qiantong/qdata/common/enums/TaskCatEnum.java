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

package tech.qiantong.qdata.common.enums;

import lombok.Getter;

import java.util.Locale;

/**
 * <P>
 * 用途:类目表枚举
 * </p>
 *
 * @author: FXB
 * @create: 2025-03-11 16:00
 **/

public enum TaskCatEnum {

    ATT_TASK_CAT("1", "离线数据集成任务"),
    CAT("2", "实时任务"),
    ATT_DATA_DEV_CAT("3", "数据开发任务"),
    ATT_JOB_CAT("4", "作业任务");

    @Getter
    private String type;

    @Getter
    private String name;

    TaskCatEnum(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public static TaskCatEnum findEnumByType(String type) {
        for (TaskCatEnum taskCatEnum : TaskCatEnum.values()) {
            if (taskCatEnum.getType().toUpperCase(Locale.ROOT).equals(type.toUpperCase(Locale.ROOT))) {
                return taskCatEnum;
            }
        }
        return null;
    }

}
