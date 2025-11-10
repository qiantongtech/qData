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
 * 任务实例
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
     * 任务实例名称
     */
    private String name;

    /**
     * 任务类型
     */
    private String taskType;

    /**
     * 流程实例id
     */
    private Long processInstanceId;

    /**
     * 流程实例名称
     */
    private String processInstanceName;

    /**
     * 项目编码
     */
    private String projectCode;

    /**
     * 任务编码
     */
    private String taskCode;

    /**
     * 任务版本号
     */
    private int taskDefinitionVersion;

    /**
     * 状态
     */
    private TaskExecutionStatus state;


    /**
     * 提交时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date submitTime;

    /**
     * 任务开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    /**
     * 任务结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    /**
     * 执行路径
     */
    private String executePath;

    /**
     * 日志路径
     */
    private String logPath;

    /**
     * 重试次数
     */
    private int retryTimes;

    /**
     * 流程实例
     */
    private ProcessInstance processInstance;

    /**
     * 流程定义
     */
    private ProcessDefinition processDefine;

    /**
     * 任务定义
     */
    private TaskDefinition taskDefine;

    /**
     * flag
     */
    private Flag flag;

    /**
     * 优先级
     */
    private Priority taskInstancePriority;


    /**
     * 延迟执行时间（分钟）
     */
    private int delayTime;

    /**
     * 任务参数
     */
    private String taskParams;

    /**
     * CPU配额
     */
    private Integer cpuQuota;

    /**
     * 最大内存
     */
    private Integer memoryMax;
}
