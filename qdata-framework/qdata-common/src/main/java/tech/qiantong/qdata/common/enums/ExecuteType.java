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

/**
 * execute type
 */
public enum ExecuteType {

    /**
     * operation type
     * 1 repeat running
     * 2 resume pause
     * 3 resume failure
     * 4 stop
     * 5 pause
     */
    NONE(0, "NONE"),

    // ******************************* Workflow ***************************
    REPEAT_RUNNING(1, "REPEAT_RUNNING"),
    RECOVER_SUSPENDED_PROCESS(2, "RECOVER_SUSPENDED_PROCESS"),
    START_FAILURE_TASK_PROCESS(3, "START_FAILURE_TASK_PROCESS"),
    STOP(4, "STOP"),
    PAUSE(5, "PAUSE"),
    // ******************************* Workflow ***************************

    // ******************************* Task *******************************
    EXECUTE_TASK(6, "EXECUTE_TASK"),
    // ******************************* Task *******************************
    ;

    private final int code;
    private final String desc;

    ExecuteType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static ExecuteType getEnum(int value) {
        for (ExecuteType e : ExecuteType.values()) {
            if (e.getCode() == value) {
                return e;
            }
        }
        return NONE;
    }
}
