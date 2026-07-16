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

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.qiantong.qdata.common.enums.*;

import java.util.Date;

/**
 * Process instance
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProcessInstance {

    /**
     * id
     */
    private Long id;

    /**
     * Process definition code
     */
    private String processDefinitionCode;
    /**
     * Process version
     */
    private int processDefinitionVersion;
    /**
     * Project code
     */
    private String projectCode;
    /**
     * State
     */
    private WorkflowExecutionStatus state;
    /**
     * State history
     */
    private String stateHistory;
    /**
     * Schedule time
     */
    private Date scheduleTime;
    /**
     * Command start time
     */
    private Date commandStartTime;

    /**
     * Start time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;
    /**
     * End time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    /**
     * Run times
     */
    private Integer runTimes;
    /**
     * Task instance name
     */
    private String name;
    /**
     * Process definition
     */
    @TableField(exist = false)
    private ProcessDefinition processDefinition;
    /**
     * Command type
     */
    private CommandType commandType;

    private String commandParam;
    /**
     * Maximum retry times
     */
    private int maxTryTimes;
    /**
     * Whether it is a sub-process
     */
    private Flag isSubProcess;
    /**
     * Priority
     */
    private Priority processInstancePriority;
    /**
     * Failure strategy
     */
    private FailureStrategy failureStrategy;


    private String dataSource;
}
