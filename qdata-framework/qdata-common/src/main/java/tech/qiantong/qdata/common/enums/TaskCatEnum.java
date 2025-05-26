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
