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
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import tech.qiantong.qdata.common.enums.Priority;
import tech.qiantong.qdata.common.utils.json.JsonDataDeserializer;
import tech.qiantong.qdata.common.utils.json.JsonDataSerializer;

import java.util.Map;
import java.util.Objects;

@Data
public class TaskDefinition {

    /**
     * id
     */
    private Long id;

    /**
     * code
     */
    private String code;

    /**
     * name
     */
    private String name;

    /**
     * version
     */
    private int version;

    /**
     * description
     */
    private String description;

    /**
     * project code
     */
    private String projectCode;

    /**
     * task user id
     */
    private int userId;

    /**
     * task type
     */
    private String taskType;

    /**
     * user defined parameters
     */
    @JsonDeserialize(using = JsonDataDeserializer.class)
    @JsonSerialize(using = JsonDataSerializer.class)
    private String taskParams;


    /**
     * user defined parameter map
     */
    @TableField(exist = false)
    private Map<String, String> taskParamMap;

    /**
     * task is valid: yes/no
     */
    private String flag;

    /**
     * task is cache: yes/no
     */
    private String isCache;

    /**
     * worker group
     */
    private String workerGroup;

    /**
     * environment code
     */
    private long environmentCode;

    /**
     * fail retry times
     */
    private int failRetryTimes;

    /**
     * fail retry interval
     */
    private int failRetryInterval;

    /**
     * task warning time out. unit: minute
     */
    private int timeout;

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
     * task priority
     */
    private Priority taskPriority;

    public TaskDefinition() {
    }

    public TaskDefinition(String code, int version) {
        this.code = code;
        this.version = version;
    }


    public Integer getCpuQuota() {
        return cpuQuota == null ? -1 : cpuQuota;
    }

    public Integer getMemoryMax() {
        return memoryMax == null ? -1 : memoryMax;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        TaskDefinition that = (TaskDefinition) o;
        return failRetryTimes == that.failRetryTimes
                && failRetryInterval == that.failRetryInterval
                && timeout == that.timeout
                && delayTime == that.delayTime
                && Objects.equals(name, that.name)
                && Objects.equals(description, that.description)
                && Objects.equals(taskType, that.taskType)
                && Objects.equals(taskParams, that.taskParams)
                && flag == that.flag
                && isCache == that.isCache
                && taskPriority == that.taskPriority
                && Objects.equals(workerGroup, that.workerGroup)
//                && timeoutFlag == that.timeoutFlag
//                && timeoutNotifyStrategy == that.timeoutNotifyStrategy
//                && (Objects.equals(resourceIds, that.resourceIds)
//                        || ("".equals(resourceIds) && that.resourceIds == null)
//                        || ("".equals(that.resourceIds) && resourceIds == null))
                && environmentCode == that.environmentCode
//                && taskGroupId == that.taskGroupId
//                && taskGroupPriority == that.taskGroupPriority
                && Objects.equals(cpuQuota, that.cpuQuota)
                && Objects.equals(memoryMax, that.memoryMax);
//                && Objects.equals(taskExecuteType, that.taskExecuteType);
    }
}
