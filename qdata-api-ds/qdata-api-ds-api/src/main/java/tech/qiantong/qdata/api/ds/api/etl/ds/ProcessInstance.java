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
 * 流程实例
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
     * 流程编码
     */
    private String processDefinitionCode;
    /**
     * 流程版本
     */
    private int processDefinitionVersion;
    /**
     * 项目编码
     */
    private String projectCode;
    /**
     * 状态
     */
    private WorkflowExecutionStatus state;
    /**
     * 状态历史
     */
    private String stateHistory;
    /**
     * 调度时间
     */
    private Date scheduleTime;
    /**
     * 执行开始时间
     */
    private Date commandStartTime;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;
    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    /**
     * 运行次数
     */
    private Integer runTimes;
    /**
     * 任务实例名称
     */
    private String name;
    /**
     * 流程定义
     */
    @TableField(exist = false)
    private ProcessDefinition processDefinition;
    /**
     * 运行类型
     */
    private CommandType commandType;

    private String commandParam;
    /**
     * 最大重试次数
     */
    private int maxTryTimes;
    /**
     * 是否是子流程
     */
    private Flag isSubProcess;
    /**
     * 优先级
     */
    private Priority processInstancePriority;
    /**
     * 失败策略
     */
    private FailureStrategy failureStrategy;


    private String dataSource;
}
