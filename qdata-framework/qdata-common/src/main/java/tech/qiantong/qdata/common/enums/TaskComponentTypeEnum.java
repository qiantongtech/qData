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
