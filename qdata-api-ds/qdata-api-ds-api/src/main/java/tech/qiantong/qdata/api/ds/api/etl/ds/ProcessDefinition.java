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
     * 编码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 版本号
     */
    private int version;

    /**
     * 发布状态 : online/offline
     */
    private String releaseState;

    /**
     * 项目编码
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
     * 流程定义日志列表
     */
    ProcessDefinitionLog processDefinitionLog;

    /**
     * 任务定义日志列表
     */
    List<TaskDefinitionLog> taskDefinitionLogList;

    /**
     * 任务关系日志列表
     */
    @TableField(exist = false)
    List<ProcessTaskRelationLog> taskRelationLogList;


    /**
     * 任务定义列表
     */
    @TableField(exist = false)
    List<TaskDefinition> taskDefinitionList;

    /**
     * 任务关系日志列表
     */
    @TableField(exist = false)
    List<ProcessTaskRelation> taskRelationList;

    /**
     * 执行策略
     */
    private String executionType;
}
