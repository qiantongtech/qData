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

package tech.qiantong.qdata.module.dpp.utils.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import tech.qiantong.qdata.common.utils.JSONUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Data
public class TaskNode {

    /**
     * task node id
     */
    private String id;

    /**
     * task node code
     */
    private String code;

    /**
     * task node version
     */
    private Long version;

    /**
     * task node name
     */
    private String name;

    /**
     * task node description
     */
    private String desc;

    /**
     * task node type
     */
    private String type;

    /**
     * the run flag has two states, NORMAL or FORBIDDEN
     */
    private String runFlag;

    private int isCache;

    /**
     * the front field
     */
    private String loc;

    /**
     * maximum number of retries
     */
    private Long maxRetryTimes;

    /**
     * Unit of retry interval: points
     */
    private Long retryInterval;

    /**
     * task group id
     */
    private int taskGroupId;
    /**
     * task group id
     */
    private int taskGroupPriority;

    /**
     * params information
     */
    @JsonDeserialize(using = JSONUtils.JsonDataDeserializer.class)
    @JsonSerialize(using = JSONUtils.JsonDataSerializer.class)
    private String params;

    /**
     * inner dependency information
     */
    @JsonDeserialize(using = JSONUtils.JsonDataDeserializer.class)
    @JsonSerialize(using = JSONUtils.JsonDataSerializer.class)
    private String preTasks;

    /**
     * node dependency list
     */
    private List<PreviousTaskNode> preTaskNodeList;

    /**
     * users store additional information
     */
    @JsonDeserialize(using = JSONUtils.JsonDataDeserializer.class)
    @JsonSerialize(using = JSONUtils.JsonDataSerializer.class)
    private String extras;

    /**
     * node dependency list
     */
    private List<Long> depList;

    /**
     * outer dependency information
     */
    @JsonDeserialize(using = JSONUtils.JsonDataDeserializer.class)
    @JsonSerialize(using = JSONUtils.JsonDataSerializer.class)
    private String dependence;

    @JsonDeserialize(using = JSONUtils.JsonDataDeserializer.class)
    @JsonSerialize(using = JSONUtils.JsonDataSerializer.class)
    private String conditionResult;

    @JsonDeserialize(using = JSONUtils.JsonDataDeserializer.class)
    @JsonSerialize(using = JSONUtils.JsonDataSerializer.class)
    private String switchResult;

    @JsonDeserialize(using = JSONUtils.JsonDataDeserializer.class)
    @JsonSerialize(using = JSONUtils.JsonDataSerializer.class)
    private String waitStartTimeout;

    /**
     * task instance priority
     */
    private String taskInstancePriority;

    /**
     * worker group
     */
    private String workerGroup;

    /**
     * environment code
     */
    private Long environmentCode;

    /**
     * task time out
     */
    @JsonDeserialize(using = JSONUtils.JsonDataDeserializer.class)
    @JsonSerialize(using = JSONUtils.JsonDataSerializer.class)
    private String timeout;

    /**
     * delay execution time.
     */
    private int delayTime;

    /**
     * cpu quota
     */
    private Integer cpuQuota;

    /**
     * max memory
     */
    private Integer memoryMax;

    /**
     * task execute type
     */
    private String taskExecuteType;
    public void setPreTasks(String preTasks) {
        this.preTasks = preTasks;
        this.depList = JSONUtils.toList(preTasks, Long.class);
    }

    public void setDepList(List<Long> depList) {
        if (depList != null) {
            this.depList = depList;
            this.preTasks = JSONUtils.toJsonString(depList);
        }
    }

    public boolean isForbidden() {
        // skip stream task when run DAG
        if (taskExecuteType == "stream") {
            return true;
        }
        return StringUtils.isNotEmpty(this.runFlag) && this.runFlag.equals("FORBIDDEN");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TaskNode taskNode = (TaskNode) o;
        return Objects.equals(name, taskNode.name)
                && Objects.equals(desc, taskNode.desc)
                && Objects.equals(type, taskNode.type)
                && Objects.equals(params, taskNode.params)
                && Objects.equals(preTasks, taskNode.preTasks)
                && Objects.equals(extras, taskNode.extras)
                && Objects.equals(runFlag, taskNode.runFlag)
                && Objects.equals(dependence, taskNode.dependence)
                && Objects.equals(workerGroup, taskNode.workerGroup)
                && Objects.equals(environmentCode, taskNode.environmentCode)
                && Objects.equals(conditionResult, taskNode.conditionResult)
                && CollectionUtils.isEqualCollection(depList, taskNode.depList)
                && Objects.equals(taskExecuteType, taskNode.taskExecuteType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, desc, type, params, preTasks, extras, depList, runFlag);
    }

    public String getTaskParams() {
        Map<String, Object> taskParams = JSONUtils.parseObject(this.params, new TypeReference<Map<String, Object>>() {
        });

        if (taskParams == null) {
            taskParams = new HashMap<>();
        }
        taskParams.put("conditionResult", this.conditionResult);
        taskParams.put("dependence", this.dependence);
        taskParams.put("switchResult", this.switchResult);
        taskParams.put("waitStartTimeout", this.waitStartTimeout);
        return JSONUtils.toJsonString(taskParams);
    }

    public Map<String, Object> taskParamsToJsonObj(String taskParams) {
        Map<String, Object> taskParamsMap = JSONUtils.parseObject(taskParams, new TypeReference<Map<String, Object>>() {
        });
        if (taskParamsMap == null) {
            taskParamsMap = new HashMap<>();
        }
        return taskParamsMap;
    }

    @Override
    public String toString() {
        return "TaskNode{"
                + "id='" + id + '\''
                + ", code=" + code
                + ", version=" + version
                + ", name='" + name + '\''
                + ", desc='" + desc + '\''
                + ", type='" + type + '\''
                + ", runFlag='" + runFlag + '\''
                + ", loc='" + loc + '\''
                + ", maxRetryTimes=" + maxRetryTimes
                + ", retryInterval=" + retryInterval
                + ", params='" + params + '\''
                + ", preTasks='" + preTasks + '\''
                + ", preTaskNodeList=" + preTaskNodeList
                + ", extras='" + extras + '\''
                + ", depList=" + depList
                + ", dependence='" + dependence + '\''
                + ", conditionResult='" + conditionResult + '\''
                + ", workerGroup='" + workerGroup + '\''
                + ", environmentCode=" + environmentCode
                + ", timeout='" + timeout + '\''
                + ", delayTime=" + delayTime + '\''
                + ", taskExecuteType=" + taskExecuteType
                + '}';
    }
}
