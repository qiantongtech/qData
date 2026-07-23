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

package tech.qiantong.qdata.api.ds.api.etl.ds;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.qiantong.qdata.common.enums.Flag;
import tech.qiantong.qdata.common.enums.Priority;
import tech.qiantong.qdata.common.enums.TaskExecutionStatus;

import java.io.Serializable;
import java.util.Date;

/**
 * Task instance
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TaskInstance implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * Task instance name
     */
    private String name;

    /**
     * Task type
     */
    private String taskType;

    /**
     * Process instance id
     */
    private Long processInstanceId;

    /**
     * Process instance name
     */
    private String processInstanceName;

    /**
     * Project code
     */
    private String projectCode;

    /**
     * Task code
     */
    private String taskCode;

    /**
     * Task definition version
     */
    private int taskDefinitionVersion;

    /**
     * State
     */
    private TaskExecutionStatus state;


    /**
     * Submit time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date submitTime;

    /**
     * Task start time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    /**
     * Task end time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    /**
     * Execute path
     */
    private String executePath;

    /**
     * Log path
     */
    private String logPath;

    /**
     * Retry times
     */
    private int retryTimes;

    /**
     * Process instance
     */
    private ProcessInstance processInstance;

    /**
     * Process definition
     */
    private ProcessDefinition processDefine;

    /**
     * Task definition
     */
    private TaskDefinition taskDefine;

    /**
     * flag
     */
    private Flag flag;

    /**
     * Priority
     */
    private Priority taskInstancePriority;


    /**
     * Delay time (minutes)
     */
    private int delayTime;

    /**
     * Task parameters
     */
    private String taskParams;

    /**
     * CPU quota
     */
    private Integer cpuQuota;

    /**
     * Maximum memory
     */
    private Integer memoryMax;
}
