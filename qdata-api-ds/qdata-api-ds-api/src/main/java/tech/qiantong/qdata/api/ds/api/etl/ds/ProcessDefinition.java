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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessDefinition {

    /**
     * id
     */
    private Long id;

    /**
     * Code
     */
    private String code;

    /**
     * Name
     */
    private String name;

    /**
     * Version
     */
    private int version;

    /**
     * Release state : online/offline
     */
    private String releaseState;

    /**
     * Project code
     */
    private String projectCode;

    /**
     * description
     */
    private String description;

    /**
     * create time
     */
    private Date createTime;

    /**
     * update time
     */
    private Date updateTime;

    /**
     * locations array for web
     */
    private String locations;

    /**
     * schedule release state : online/offline
     */
//    private ReleaseState scheduleReleaseState;

    /**
     * Process definition log list
     */
    ProcessDefinitionLog processDefinitionLog;

    /**
     * Task definition log list
     */
    List<TaskDefinitionLog> taskDefinitionLogList;

    /**
     * Task relation log list
     */
    @TableField(exist = false)
    List<ProcessTaskRelationLog> taskRelationLogList;


    /**
     * Task definition list
     */
    @TableField(exist = false)
    List<TaskDefinition> taskDefinitionList;

    /**
     * Task relation list
     */
    @TableField(exist = false)
    List<ProcessTaskRelation> taskRelationList;

    /**
     * Execution type
     */
    private String executionType;
}
