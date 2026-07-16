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
 * Purpose: Task component type enumeration
 * </p>
 *
 * @author: FXB
 * @create: 2025-03-12 16:40
 **/

public enum TaskComponentTypeEnum {

    //Input
    DB_READER("1", "数据库输入"),
    EXCEL_READER("2", "Excel输入"),
    KAFKA_READER("3", "Kafka输入"),
    CSV_READER("4", "csv输入"),

    //Clean
    SELECT_FIELDS("22", "字段选择"),

    SPARK_CLEAN("31", "SPARK清洗"),
    SORT_RECORD("34", "排序记录"),
    FIELD_DERIVATION("39", "字段派生器"),
    DATA_DEDUPLICATION("40", "数据去重"),
    VALUE_MAP("47", "值映射"),
    ADD_CONSTANT("48", "增加常量"),

    //Develop
    SQL_DEV("51", "SQL开发"),
    PROCEDURE_DEV("52", "存储过程开发"),
    SPARK_SQL_DEV("53", "SparkSql开发"),
    SHELL_DEV("54", "SHELL开发"),

    //Subtask
    SUB_PROCESS("71", "子任务"),

    //Output
    DB_WRITER("91", "数据库输出");

    @Getter
    private String code;

    @Getter
    private String name;

    TaskComponentTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static TaskComponentTypeEnum findEnumByType(String type) {
        for (TaskComponentTypeEnum taskCatEnum : TaskComponentTypeEnum.values()) {
            if (taskCatEnum.getCode().toUpperCase(Locale.ROOT).equals(type.toUpperCase(Locale.ROOT))) {
                return taskCatEnum;
            }
        }
        return null;
    }

}
