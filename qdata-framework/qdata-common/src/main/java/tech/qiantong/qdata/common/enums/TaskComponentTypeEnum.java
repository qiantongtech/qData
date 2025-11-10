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
 * For brand customization, please contact the official team for authorization.
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
 * 如需定制品牌，请通过官方渠道申请品牌授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.common.enums;

import lombok.Getter;

import java.util.Locale;

/**
 * <P>
 * 用途:任务组件类型枚举
 * </p>
 *
 * @author: FXB
 * @create: 2025-03-12 16:40
 **/

public enum TaskComponentTypeEnum {

    //输入
    DB_READER("1", "数据库输入"),
    EXCEL_READER("2", "Excel输入"),
    KAFKA_READER("3", "Kafka输入"),
    CSV_READER("4", "csv输入"),


    //清洗
    SPARK_CLEAN("31", "SPARK清洗"),
    SORT_RECORD("34", "排序记录"),
    FIELD_DERIVATION("39", "字段派生器"),

    //开发
    SQL_DEV("51", "SQL开发"),
    PROCEDURE_DEV("52", "存储过程开发"),
    SPARK_SQL_DEV("53", "SparkSql开发"),
    SHELL_DEV("54", "SHELL开发"),

    //子任务
    SUB_PROCESS("71", "子任务"),

    //输出
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
