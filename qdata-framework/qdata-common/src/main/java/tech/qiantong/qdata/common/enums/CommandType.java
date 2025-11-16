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

/**
 * command types
 */
public enum CommandType {

    /**
     * command types
     * 0 start a new process
     * 1 start a new process from current nodes
     * 2 recover tolerance fault process
     * 3 recover suspended process
     * 4 start process from failure task nodes
     * 5 complement data
     * 6 start a new process from scheduler
     * 7 repeat running a process
     * 8 pause a process
     * 9 stop a process
     * 10 recover waiting thread
     * 11 recover serial wait
     * 12 start a task node in a process instance
     */
    START_PROCESS(0, "start a new process"),
    START_CURRENT_TASK_PROCESS(1, "start a new process from current nodes"),
    RECOVER_TOLERANCE_FAULT_PROCESS(2, "recover tolerance fault process"),
    RECOVER_SUSPENDED_PROCESS(3, "recover suspended process"),
    START_FAILURE_TASK_PROCESS(4, "start process from failure task nodes"),
    COMPLEMENT_DATA(5, "complement data"),
    SCHEDULER(6, "start a new process from scheduler"),
    REPEAT_RUNNING(7, "repeat running a process"),
    PAUSE(8, "pause a process"),
    STOP(9, "stop a process"),
    RECOVER_WAITING_THREAD(10, "recover waiting thread"),
    RECOVER_SERIAL_WAIT(11, "recover serial wait"),
    EXECUTE_TASK(12, "start a task node in a process instance"),
    DYNAMIC_GENERATION(13, "dynamic generation"),
    ;

    CommandType(int code, String descp) {
        this.code = code;
        this.descp = descp;
    }

    @EnumValue
    private final int code;
    private final String descp;

    public int getCode() {
        return code;
    }

    public String getDescp() {
        return descp;
    }

    private static final Map<Integer, CommandType> COMMAND_TYPE_MAP = new HashMap<>();

    static {
        for (CommandType commandType : CommandType.values()) {
            COMMAND_TYPE_MAP.put(commandType.code, commandType);
        }
    }

    public static CommandType of(Integer status) {
        if (COMMAND_TYPE_MAP.containsKey(status)) {
            return COMMAND_TYPE_MAP.get(status);
        }
        throw new IllegalArgumentException("invalid status : " + status);
    }
}
