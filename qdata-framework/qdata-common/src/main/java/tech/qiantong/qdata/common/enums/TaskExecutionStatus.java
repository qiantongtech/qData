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

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tech.qiantong.qdata.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import java.util.HashMap;
import java.util.Map;

public enum TaskExecutionStatus {

    SUBMITTED_SUCCESS(0, "submit success"),
    RUNNING_EXECUTION(1, "running"),
    PAUSE(3, "pause"),
    STOP(5, "stop"),
    FAILURE(6, "failure"),
    SUCCESS(7, "success"),
    NEED_FAULT_TOLERANCE(8, "need fault tolerance"),
    KILL(9, "kill"),
    DELAY_EXECUTION(12, "delay execution"),
    FORCED_SUCCESS(13, "forced success"),
    DISPATCH(17, "dispatch"),

    ;

    private static final Map<Integer, TaskExecutionStatus> CODE_MAP = new HashMap<>();

    static {
        for (TaskExecutionStatus executionStatus : TaskExecutionStatus.values()) {
            CODE_MAP.put(executionStatus.getCode(), executionStatus);
        }
    }

    /**
     * Get <code>TaskExecutionStatus</code> by code, if the code is invalidated will throw {@link IllegalArgumentException}.
     */
    public static TaskExecutionStatus of(int code) {
        TaskExecutionStatus taskExecutionStatus = CODE_MAP.get(code);
        if (taskExecutionStatus == null) {
            throw new IllegalArgumentException(String.format("The task execution status code: %s is invalidated",
                    code));
        }
        return taskExecutionStatus;
    }

    public boolean isRunning() {
        return this == RUNNING_EXECUTION;
    }

    public boolean isSuccess() {
        return this == TaskExecutionStatus.SUCCESS;
    }

    public boolean isForceSuccess() {
        return this == TaskExecutionStatus.FORCED_SUCCESS;
    }

    public boolean isKill() {
        return this == TaskExecutionStatus.KILL;
    }

    public boolean isFailure() {
        return this == TaskExecutionStatus.FAILURE || this == NEED_FAULT_TOLERANCE;
    }

    public boolean isPause() {
        return this == TaskExecutionStatus.PAUSE;
    }

    public boolean isStop() {
        return this == TaskExecutionStatus.STOP;
    }

    public boolean isFinished() {
        return isSuccess() || isKill() || isFailure() || isPause() || isStop() || isForceSuccess();
    }

    public boolean isNeedFaultTolerance() {
        return this == NEED_FAULT_TOLERANCE;
    }

    public boolean shouldFailover() {
        return SUBMITTED_SUCCESS == this
                || DISPATCH == this
                || RUNNING_EXECUTION == this
                || DELAY_EXECUTION == this;
    }

    @EnumValue
    private final int code;
    private final String desc;

    TaskExecutionStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        return "TaskExecutionStatus{" + "code=" + code + ", desc='" + desc + '\'' + '}';
    }

}
