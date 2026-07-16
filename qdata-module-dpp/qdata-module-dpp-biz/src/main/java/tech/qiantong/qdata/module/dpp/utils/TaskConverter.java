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

package tech.qiantong.qdata.module.dpp.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSON;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tech.qiantong.qdata.api.ds.api.etl.*;
import tech.qiantong.qdata.api.ds.api.etl.ds.*;
import tech.qiantong.qdata.common.config.DsRedisConfig;
import tech.qiantong.qdata.common.config.RabbitmqConfig;
import tech.qiantong.qdata.common.enums.TaskComponentTypeEnum;
import tech.qiantong.qdata.common.utils.JSONUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.*;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlNodeDO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlNodeLogDO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskDO;
import tech.qiantong.qdata.module.dpp.utils.datax.FlinkxJson;
import tech.qiantong.qdata.module.dpp.utils.ds.component.ComponentFactory;
import tech.qiantong.qdata.module.dpp.utils.model.DsResource;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class TaskConverter {
    @Resource
    private FlinkxJson flinkxJson;

    private static String resourceName;
    private static String defaultMainClass;
    private static String defaultMaster;
    private static String resourceUrl;
    private static RabbitmqConfig rabbitmqConfig;
    private static DsRedisConfig dsRedisConfig;

    @Value("${ds.spark.main_jar}")
    private void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    @Value("${ds.spark.main_class}")
    private void setDefaultMainClass(String defaultMainClass) {
        this.defaultMainClass = defaultMainClass;
    }

    @Value("${ds.spark.master_url}")
    private void setDefaultMaster(String defaultMaster) {
        this.defaultMaster = defaultMaster;
    }

    @Value("${ds.resource_url}")
    private void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    @Resource
    private void setRabbitmqConfig(RabbitmqConfig rabbitmqConfig) {
        this.rabbitmqConfig = rabbitmqConfig;
    }

    @Resource
    private void setDsRedisConfig(DsRedisConfig dsRedisConfig) {
        this.dsRedisConfig = dsRedisConfig;
    }

    // Default configuration constants
    private static final long DEFAULT_ENVIRONMENT_CODE = 133155949418208L; // Default environment code
    private static final String DEFAULT_WORKER_GROUP = "default"; // Default worker group
    private static final String DEFAULT_FLAG = "YES"; // Default flag, indicates node is enabled
    private static final String DEFAULT_IS_CACHE = "NO"; // Default cache disabled
    private static final String DEFAULT_TASK_PRIORITY = "MEDIUM"; // Default task priority
    private static final String DEFAULT_TASK_TYPE = "SPARK"; // Default task type, SPARK or DATAX etc.
    private static final String DEFAULT_PROGRAM_TYPE = "JAVA"; // Default program type, JAVA
    private static final String DEFAULT_MAIN_JAR = "file:/dolphinscheduler/default/resources/spart-demo-1.0.jar"; // Default main Jar path
    private static final String DEFAULT_DEPLOY_MODE = "client"; // Default deploy mode
    private static final int DEFAULT_DRIVER_CORES = 1; // Default driver cores
    private static final String DEFAULT_DRIVER_MEMORY = "2G"; // Default driver memory
    private static final int DEFAULT_NUM_EXECUTORS = 1; // Default number of executors
    private static final String DEFAULT_EXECUTOR_MEMORY = "4G"; // Default executor memory
    private static final int DEFAULT_EXECUTOR_CORES = 2; // Default executor cores
    private static final String DEFAULT_SQL_EXECUTION_TYPE = "SCRIPT"; // Default SQL execution type
    private static final String DEFAULT_CONDITION_TYPE = "NONE"; // Default condition type is "NONE"

    private static final int DEFAULT_TASK_failRetryTimes = 0; // failRetryTimes: failure retry count
    private static final int DEFAULT_TASK_delayTime = 0; // delayTime: delayed execution time
    private static final int DEFAULT_TASK_failRetryInterval = 1; // failRetryInterval: failure retry interval



    public static final String TASK_INSTANCE_LOG_KEY = "log:taskInstanceLog:";// Task instance log key

    public static final String PROCESS_INSTANCE_LOG_KEY = "log:processInstanceLog:";// Process instance log key

    public static final String ETL_READER_ID_KEY = "etl:reader:id:";

    public static final String ETL_READER_DATE_KEY = "etl:reader:date:";

    public static DsTaskSaveReqDTO buildDsTaskSaveReq(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO) {// name
        // Create return entity
        DsTaskSaveReqDTO dsTaskSaveReqDTO = new DsTaskSaveReqDTO();
        // 1. Wrap basic parameters
        dsTaskSaveReqDTO.setName(dppEtlNewNodeSaveReqVO.getName());
        if(StringUtils.isNotEmpty(dppEtlNewNodeSaveReqVO.getCode())){
            dsTaskSaveReqDTO.setProcessDefinitionCode(Long.parseLong(dppEtlNewNodeSaveReqVO.getCode()));
        }
        dsTaskSaveReqDTO.setDescription(dppEtlNewNodeSaveReqVO.getDescription());
        dsTaskSaveReqDTO.setExecutionType(dppEtlNewNodeSaveReqVO.getExecutionType());


        // 2. Wrap node info DATAX, SPARK
        String taskDefinition = buildTaskDefinition(dppEtlNewNodeSaveReqVO.getTaskDefinitionList());

        String taskRelation = buildTaskRelationJson(dppEtlNewNodeSaveReqVO.getTaskRelationJson());

        String location = buildTaskNodeLocations(dppEtlNewNodeSaveReqVO.getLocations());


        dsTaskSaveReqDTO.setTaskDefinitionJson(taskDefinition);
        dsTaskSaveReqDTO.setTaskRelationJson(taskRelation);
        dsTaskSaveReqDTO.setLocations(location);


        return dsTaskSaveReqDTO;
    }

    private static String buildTaskNodeLocations(List<Map<String, Object>> locations) {
        // Parse the input JSON string into a List
        List<Map<String, Object>> list = locations;

        List<Map<String, Object>> result = new ArrayList<>();

        // Iterate over each node location info
        for (Map<String, Object> location : list) {
            Map<String, Object> locationMap = new HashMap<>();

            // Populate required fields
            locationMap.put("taskCode", Long.parseLong(String.valueOf(location.getOrDefault("taskCode", 0L)))); // Default taskCode is 0
            locationMap.put("x", location.getOrDefault("x", 0)); // Default x is 0
            locationMap.put("y", location.getOrDefault("y", 0)); // Default y is 0

            // Add the processed location info to the result list
            result.add(locationMap);
        }

        // Return the processed JSON string
        return JSON.toJSONString(result);
    }

    private static String buildTaskRelationJson(String taskRelationJson) {
        // Parse the input JSON string into a List
        List<Map<String, Object>> list = JSONUtils.convertTaskDefinitionJson(taskRelationJson);

        List<Map<String, Object>> result = new ArrayList<>();

        // Iterate over each relation node
        for (Map<String, Object> relation : list) {
            Map<String, Object> relationMap = new HashMap<>();

            // Populate default values and required fields
            relationMap.put("id", relation.getOrDefault("dsId", null)); // Default id is 0
            relationMap.put("preTaskCode", relation.getOrDefault("preTaskCode", 0L)); // Default preTaskCode is 0
            relationMap.put("preTaskVersion", relation.getOrDefault("preTaskVersion", 0)); // Default preTaskVersion is 0
            relationMap.put("postTaskCode", relation.getOrDefault("postTaskCode", 0L)); // Default postTaskCode is 0
            relationMap.put("postTaskVersion", relation.getOrDefault("postTaskVersion", 0)); // Default postTaskVersion is 0
            relationMap.put("conditionType", relation.getOrDefault("conditionType", DEFAULT_CONDITION_TYPE)); // Default condition type is "NONE"

            // Add the processed node relation to the result list
            result.add(relationMap);
        }

        // Return the processed JSON string
        return JSON.toJSONString(result);
    }

    /**
     * Build task definition
     *
     * @param taskDefinitionJson Task definition JSON string
     * @return Built task definition JSON string
     */
    public static String buildTaskDefinition(String taskDefinitionJson) {
        List<Map<String, Object>> list = JSONUtils.convertTaskDefinitionJson(taskDefinitionJson);

        List<Map<String, Object>> result = new ArrayList<>();

        // Custom parameters
//        Map<String, Object> definitionJsonMap = JSONUtils.convertTaskDefinitionJsonMap(draftJson);


        // Iterate over each task definition
        for (Map<String, Object> task : list) {
            // Process default values and required fields for each task
            Map<String, Object> taskMap = new HashMap<>();

            // Set basic info fields
            taskMap.put("id", task.getOrDefault("dsId", null)); // Default id is 0
            taskMap.put("name", task.getOrDefault("name", "")); // Default empty string
            taskMap.put("code", task.getOrDefault("code", 0L)); // Default code is 0L
            taskMap.put("version", task.getOrDefault("version", 0)); // Default version is 1
            taskMap.put("description", task.getOrDefault("description", "")); // Default empty description
            taskMap.put("workerGroup", task.getOrDefault("workerGroup", DEFAULT_WORKER_GROUP)); // Default workerGroup is "default"
            taskMap.put("environmentCode", task.getOrDefault("environmentCode", DEFAULT_ENVIRONMENT_CODE)); // Default environment code
            taskMap.put("flag", DEFAULT_FLAG); // Default flag is "YES"
            taskMap.put("isCache", task.getOrDefault("isCache", DEFAULT_IS_CACHE)); // Default isCache is "NO"
            taskMap.put("taskPriority", task.getOrDefault("taskPriority", DEFAULT_TASK_PRIORITY)); // Default task priority is "MEDIUM"
            taskMap.put("taskType", task.getOrDefault("taskType", DEFAULT_TASK_TYPE)); // Default task type is "SPARK"
            taskMap.put("taskExecuteType", "BATCH");

            // 2025-06-25 Added new config item defaults
            taskMap.put("failRetryTimes", MapUtils.getObject(task,"failRetryTimes",DEFAULT_TASK_failRetryTimes));
            taskMap.put("delayTime", MapUtils.getObject(task,"delayTime",DEFAULT_TASK_delayTime));
            taskMap.put("failRetryInterval", MapUtils.getObject(task,"failRetryInterval",DEFAULT_TASK_failRetryInterval));

            // Wrap component taskParams
            String componentType = String.valueOf(task.get("componentType")); // Component type
            Map<String, Object> params = (Map<String, Object>) MapUtils.getObject(task, "taskParams");

            // Store default data based on type
            if (StringUtils.equals(TaskComponentTypeEnum.SPARK_CLEAN.getCode(), componentType)
                    || StringUtils.equals(TaskComponentTypeEnum.SPARK_SQL_DEV.getCode(), componentType)) {
                params.put("mainClass", defaultMainClass);
                params.put("resourceName", resourceName);
                params.put("master", defaultMaster);
            }
            // Extract parameters
//            params.put("driverCores", MapUtils.getObject(definitionJsonMap, "driverCores", DEFAULT_DRIVER_CORES));
//            params.put("driverMemory", MapUtils.getObject(definitionJsonMap, "driverMemory", DEFAULT_DRIVER_MEMORY));
//            params.put("numExecutors", MapUtils.getObject(definitionJsonMap, "numExecutors", DEFAULT_NUM_EXECUTORS));
//            params.put("executorMemory", MapUtils.getObject(definitionJsonMap, "executorMemory", DEFAULT_EXECUTOR_MEMORY));
//            params.put("executorCores", MapUtils.getObject(definitionJsonMap, "executorCores", DEFAULT_EXECUTOR_CORES));
//            params.put("yarnQueue", MapUtils.getObject(definitionJsonMap, "yarnQueue", ""));

            // Add the task's taskParams to taskMap
            taskMap.put("taskParams", ComponentFactory.getComponentItem(componentType).parse(params));

            // Add the populated task to the result list
            result.add(taskMap);
        }

        // Return the processed JSON string
        return JSON.toJSONString(result);
    }


    /**
     * Convert DppEtlNewNodeSaveReqVO and ProcessDefinition to DppEtlTaskSaveReqVO
     *
     * @param dppEtlNewNodeSaveReqVO Task data from external request
     * @param data                   Process definition data
     * @return Converted DppEtlTaskSaveReqVO
     */
    public static DppEtlTaskSaveReqVO convertToDppEtlTaskSaveReqVO(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO, ProcessDefinition data) {
        // Create DppEtlTaskSaveReqVO object
        DppEtlTaskSaveReqVO createReqVO = new DppEtlTaskSaveReqVO();

        // Populate task data
        createReqVO.setType(dppEtlNewNodeSaveReqVO.getType());// Task type
        createReqVO.setName(data.getName()); // Task name
        createReqVO.setCode(String.valueOf(data.getCode())); // Task code
        createReqVO.setVersion(data.getVersion()); // Version number
        createReqVO.setProjectId(dppEtlNewNodeSaveReqVO.getProjectId()); // Project ID
        createReqVO.setProjectCode(String.valueOf(data.getProjectCode())); // Project code
        createReqVO.setDescription(dppEtlNewNodeSaveReqVO.getDescription()); // Description
        createReqVO.setLocations(data.getLocations()); // Node coordinate info
        createReqVO.setLocations(data.getLocations()); // Node coordinate info
        createReqVO.setDsId(data.getId()); // DolphinScheduler ID

        String releaseState = dppEtlNewNodeSaveReqVO.getReleaseState();
        // Set status based on releaseState (0: offline, 1: online)
        if (StringUtils.equals("-2", releaseState) || StringUtils.equals("-3", releaseState)) {
            createReqVO.setStatus(releaseState); // Online
        } else if ("offline".equalsIgnoreCase(data.getReleaseState())) {
            createReqVO.setStatus("0"); // Offline
        } else if ("online".equalsIgnoreCase(data.getReleaseState())) {
            createReqVO.setStatus("1"); // Online
        } else {
            createReqVO.setStatus("0"); // Offline
        }
        createReqVO.setRemark(""); // Default remark (adjustable as needed)

        createReqVO.setExecutionType(data.getExecutionType());// Execution strategy
        // Populate creator and update time info
        createReqVO.setCreatorId(dppEtlNewNodeSaveReqVO.getCreatorId()); // Assuming project ID as creator ID (adjust as needed)
        createReqVO.setCreateBy(dppEtlNewNodeSaveReqVO.getCreateBy()); // Assuming task name as creator (adjust as needed)
        createReqVO.setCreateTime(dppEtlNewNodeSaveReqVO.getCreateTime()); // Set current time as creation time
        createReqVO.setUpdatorId(dppEtlNewNodeSaveReqVO.getUpdatorId()); // Assuming project ID as updater ID (adjust as needed)
        createReqVO.setUpdateBy(dppEtlNewNodeSaveReqVO.getUpdateBy()); // Assuming task name as updater (adjust as needed)
        createReqVO.setUpdateTime(dppEtlNewNodeSaveReqVO.getUpdateTime()); // Set current time as update time

        createReqVO.setPersonCharge(dppEtlNewNodeSaveReqVO.getPersonCharge());
        createReqVO.setContactNumber(dppEtlNewNodeSaveReqVO.getContactNumber());
        createReqVO.setCatCode(dppEtlNewNodeSaveReqVO.getCatCode());
        createReqVO.setType(dppEtlNewNodeSaveReqVO.getType());
        createReqVO.setScheduler(dppEtlNewNodeSaveReqVO.getScheduler());
        createReqVO.setActuator(dppEtlNewNodeSaveReqVO.getActuator());

        // Temporarily meaningless parameter
        createReqVO.setTimeout(dppEtlNewNodeSaveReqVO.getTimeout());


        // Return the generated DppEtlTaskSaveReqVO
        return createReqVO;
    }

    public static DppEtlTaskLogSaveReqVO fromDppEtlTaskLogSaveReqVO(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO, ProcessDefinition processDefinition) {
        // Create DppEtlTaskSaveReqVO object
        DppEtlTaskLogSaveReqVO createReqVO = new DppEtlTaskLogSaveReqVO();
        ProcessDefinitionLog data = processDefinition.getProcessDefinitionLog();
        // Populate task data
        createReqVO.setType(dppEtlNewNodeSaveReqVO.getType());
        createReqVO.setName(data.getName()); // Task name
        createReqVO.setCode(String.valueOf(data.getCode())); // Task code
        createReqVO.setVersion(data.getVersion()); // Version number
        createReqVO.setProjectId(dppEtlNewNodeSaveReqVO.getProjectId()); // Project ID
        createReqVO.setProjectCode(String.valueOf(data.getProjectCode())); // Project code
        createReqVO.setDescription(dppEtlNewNodeSaveReqVO.getDescription()); // Description
        createReqVO.setLocations(data.getLocations()); // Node coordinate info
        createReqVO.setDsId(data.getId()); // DolphinScheduler ID

        // Set status based on releaseState (0: offline, 1: online)
        if ("online".equalsIgnoreCase(data.getReleaseState())) {
            createReqVO.setStatus("1"); // Online
        } else if ("offline".equalsIgnoreCase(data.getReleaseState())) {
            createReqVO.setStatus("0"); // Offline
        } else {
            createReqVO.setStatus("0"); // Offline
        }
        createReqVO.setRemark(""); // Default remark (adjustable as needed)

        createReqVO.setExecutionType(data.getExecutionType());// Execution strategy
        // Populate creator and update time info
        createReqVO.setCreatorId(dppEtlNewNodeSaveReqVO.getCreatorId()); // Assuming project ID as creator ID (adjust as needed)
        createReqVO.setCreateBy(dppEtlNewNodeSaveReqVO.getCreateBy()); // Assuming task name as creator (adjust as needed)
        createReqVO.setCreateTime(dppEtlNewNodeSaveReqVO.getCreateTime()); // Set current time as creation time
        createReqVO.setUpdatorId(dppEtlNewNodeSaveReqVO.getUpdatorId()); // Assuming project ID as updater ID (adjust as needed)
        createReqVO.setUpdateBy(dppEtlNewNodeSaveReqVO.getUpdateBy()); // Assuming task name as updater (adjust as needed)
        createReqVO.setUpdateTime(dppEtlNewNodeSaveReqVO.getUpdateTime()); // Set current time as update time

        // Temporarily meaningless parameter
        createReqVO.setPersonCharge(dppEtlNewNodeSaveReqVO.getPersonCharge());
        createReqVO.setType(dppEtlNewNodeSaveReqVO.getType());
        createReqVO.setTimeout(dppEtlNewNodeSaveReqVO.getTimeout());


        // Return the generated DppEtlTaskSaveReqVO
        return createReqVO;
    }

    /**
     * Convert DppEtlNewNodeSaveReqVO and DppEtlTaskSaveReqVO to DppEtlTaskLogSaveReqVO
     *
     * @param dppEtlNewNodeSaveReqVO Task data from external request
     * @param task                   Process definition data
     * @return Converted DppEtlTaskSaveReqVO
     */
    public static DppEtlTaskLogSaveReqVO fromDppEtlTaskLogSaveReqVO(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO, DppEtlTaskSaveReqVO task) {
        // Create DppEtlTaskSaveReqVO object
        DppEtlTaskLogSaveReqVO createReqVO = new DppEtlTaskLogSaveReqVO();
        // Populate task data
        createReqVO.setType(dppEtlNewNodeSaveReqVO.getType());
        createReqVO.setName(task.getName()); // Task name
        createReqVO.setCode(task.getCode()); // Task code
        createReqVO.setVersion(task.getVersion()); // Version number
        createReqVO.setProjectId(dppEtlNewNodeSaveReqVO.getProjectId()); // Project ID
        createReqVO.setProjectCode(task.getProjectCode()); // Project code
        createReqVO.setDescription(dppEtlNewNodeSaveReqVO.getDescription()); // Description
        createReqVO.setLocations(task.getLocations()); // Node coordinate info
        createReqVO.setDsId(task.getId()); // DolphinScheduler ID

        // Set status based on releaseState (0: offline, 1: online)
        if ("online".equalsIgnoreCase(task.getStatus())) {
            createReqVO.setStatus("1"); // Online
        } else if ("offline".equalsIgnoreCase(task.getStatus())) {
            createReqVO.setStatus("0"); // Offline
        } else {
            createReqVO.setStatus("0"); // Offline
        }
        createReqVO.setRemark(""); // Default remark (adjustable as needed)

        createReqVO.setExecutionType(task.getExecutionType());// Execution strategy
        // Populate creator and update time info
        createReqVO.setCreatorId(dppEtlNewNodeSaveReqVO.getCreatorId()); // Assuming project ID as creator ID (adjust as needed)
        createReqVO.setCreateBy(dppEtlNewNodeSaveReqVO.getCreateBy()); // Assuming task name as creator (adjust as needed)
        createReqVO.setCreateTime(dppEtlNewNodeSaveReqVO.getCreateTime()); // Set current time as creation time
        createReqVO.setUpdatorId(dppEtlNewNodeSaveReqVO.getUpdatorId()); // Assuming project ID as updater ID (adjust as needed)
        createReqVO.setUpdateBy(dppEtlNewNodeSaveReqVO.getUpdateBy()); // Assuming task name as updater (adjust as needed)
        createReqVO.setUpdateTime(dppEtlNewNodeSaveReqVO.getUpdateTime()); // Set current time as update time

        // Temporarily meaningless parameter
        createReqVO.setPersonCharge(dppEtlNewNodeSaveReqVO.getPersonCharge());
        createReqVO.setType(dppEtlNewNodeSaveReqVO.getType());
        createReqVO.setTimeout(dppEtlNewNodeSaveReqVO.getTimeout());


        // Return the generated DppEtlTaskSaveReqVO
        return createReqVO;
    }

    public static DppEtlTaskLogSaveReqVO fromDppEtlTaskSaveReqVO(DppEtlTaskSaveReqVO dppEtlTaskSaveReqVO) {
        DppEtlTaskLogSaveReqVO logSaveReqVO = new DppEtlTaskLogSaveReqVO();

        // Direct mapping of basic fields
        logSaveReqVO.setType(dppEtlTaskSaveReqVO.getType());
        logSaveReqVO.setName(dppEtlTaskSaveReqVO.getName());
        logSaveReqVO.setCode(dppEtlTaskSaveReqVO.getCode());
        logSaveReqVO.setVersion(dppEtlTaskSaveReqVO.getVersion());
        logSaveReqVO.setProjectId(dppEtlTaskSaveReqVO.getProjectId());
        logSaveReqVO.setProjectCode(dppEtlTaskSaveReqVO.getProjectCode());
        logSaveReqVO.setPersonCharge(dppEtlTaskSaveReqVO.getPersonCharge());
        logSaveReqVO.setLocations(dppEtlTaskSaveReqVO.getLocations());
        logSaveReqVO.setDescription(dppEtlTaskSaveReqVO.getDescription());
        logSaveReqVO.setTimeout(dppEtlTaskSaveReqVO.getTimeout());
        logSaveReqVO.setExtractionCount(dppEtlTaskSaveReqVO.getExtractionCount());
        logSaveReqVO.setWriteCount(dppEtlTaskSaveReqVO.getWriteCount());
        logSaveReqVO.setDsId(dppEtlTaskSaveReqVO.getDsId());
        logSaveReqVO.setRemark(dppEtlTaskSaveReqVO.getRemark());
        logSaveReqVO.setStatus(dppEtlTaskSaveReqVO.getStatus());


        // Populate creator and update time info
        logSaveReqVO.setCreatorId(dppEtlTaskSaveReqVO.getProjectId()); // Assuming project ID as creator ID (adjust as needed)
        logSaveReqVO.setCreateBy(dppEtlTaskSaveReqVO.getName()); // Assuming task name as creator (adjust as needed)
        logSaveReqVO.setCreateTime(dppEtlTaskSaveReqVO.getCreateTime()); // Set current time as creation time
        logSaveReqVO.setUpdatorId(dppEtlTaskSaveReqVO.getProjectId()); // Assuming project ID as updater ID (adjust as needed)
        logSaveReqVO.setUpdateBy(dppEtlTaskSaveReqVO.getName()); // Assuming task name as updater (adjust as needed)
        logSaveReqVO.setUpdateTime(dppEtlTaskSaveReqVO.getUpdateTime()); // Set current time as update time

        return logSaveReqVO;
    }


    public static List<DppEtlNodeSaveReqVO> convertToDppEtlNodeSaveReqVOList(ProcessDefinition processDefinition, DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO) {
        List<DppEtlNodeSaveReqVO> resultList = new ArrayList<>();

        // Extract input parameter info
        List<Map<String, Object>> list = JSONUtils.convertTaskDefinitionJson(dppEtlNewNodeSaveReqVO.getTaskDefinitionList());

        // Iterate over taskDefinitionList in ProcessDefinition
        for (TaskDefinition taskDefinition : processDefinition.getTaskDefinitionList()) {
            // Get the node definition data packaged by the frontend
            Map<String, Object> taskDefinitionMap = list.stream().filter(item -> {
                String code = MapUtils.getString(item, "code", "");
                return StringUtils.equals(taskDefinition.getCode(), code);
            }).findFirst().get();
            DppEtlNodeSaveReqVO createReqVO = new DppEtlNodeSaveReqVO();
            // 1. Task-related info
            createReqVO.setTaskType(dppEtlNewNodeSaveReqVO.getType());// Task type
            createReqVO.setType(taskDefinition.getTaskType()); // Node type
            createReqVO.setComponentType(String.valueOf(taskDefinitionMap.get("componentType")));// Component type
            createReqVO.setName(taskDefinition.getName()); // Task name
            createReqVO.setCode(String.valueOf(taskDefinition.getCode())); // Task code
            createReqVO.setVersion(taskDefinition.getVersion()); // Task version
            createReqVO.setProjectId(dppEtlNewNodeSaveReqVO.getProjectId()); // Project ID
            createReqVO.setProjectCode(String.valueOf(taskDefinition.getProjectCode())); // Project code

            createReqVO.setPriority(String.valueOf(taskDefinition.getTaskPriority()));// Task priority
            createReqVO.setFailRetryTimes((long) taskDefinition.getFailRetryTimes());
            createReqVO.setFailRetryInterval((long) taskDefinition.getFailRetryInterval());
            createReqVO.setTimeout((long) taskDefinition.getTimeout());
            createReqVO.setDelayTime((long) taskDefinition.getDelayTime());
            createReqVO.setCpuQuota((long) taskDefinition.getCpuQuota());
            createReqVO.setMemoryMax((long) taskDefinition.getMemoryMax());
            createReqVO.setDescription(taskDefinition.getDescription());
            createReqVO.setDsId(taskDefinition.getId()); // Set the task's dsId as the node's dsId

            createReqVO.setParameters(getTaskParamsAsJson(list, String.valueOf(taskDefinition.getCode()))); // Node parameters

            // Populate creator and update time info
            createReqVO.setCreatorId(dppEtlNewNodeSaveReqVO.getCreatorId()); // Assuming project ID as creator ID (adjust as needed)
            createReqVO.setCreateBy(dppEtlNewNodeSaveReqVO.getCreateBy()); // Assuming task name as creator (adjust as needed)
            createReqVO.setCreateTime(dppEtlNewNodeSaveReqVO.getCreateTime()); // Set current time as creation time
            createReqVO.setUpdatorId(dppEtlNewNodeSaveReqVO.getUpdatorId()); // Assuming project ID as updater ID (adjust as needed)
            createReqVO.setUpdateBy(dppEtlNewNodeSaveReqVO.getUpdateBy()); // Assuming task name as updater (adjust as needed)
            createReqVO.setUpdateTime(dppEtlNewNodeSaveReqVO.getUpdateTime()); // Set current time as update time

            // Add to result list
            resultList.add(createReqVO);
        }
        return resultList;
    }

    public static List<DppEtlNodeSaveReqVO> convertToDppEtlNodeSaveReqVOList(List<TaskDefinition> taskDefinitionList, DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO) {
        //取出入参数的信息
        List<Map<String, Object>> list = JSONUtils.convertTaskDefinitionJson(dppEtlNewNodeSaveReqVO.getTaskDefinitionList());
        return taskDefinitionList.stream()
                .map(taskDefinition -> {
                    DppEtlNodeSaveReqVO createReqVO = new DppEtlNodeSaveReqVO();
                    createReqVO.setTaskType(dppEtlNewNodeSaveReqVO.getType());
                    createReqVO.setType(taskDefinition.getTaskType());
                    createReqVO.setName(taskDefinition.getName());
                    createReqVO.setCode(String.valueOf(taskDefinition.getCode()));
                    createReqVO.setVersion(taskDefinition.getVersion());
                    createReqVO.setProjectId(dppEtlNewNodeSaveReqVO.getProjectId());
                    createReqVO.setProjectCode(String.valueOf(taskDefinition.getProjectCode()));
                    createReqVO.setPriority(String.valueOf(taskDefinition.getTaskPriority()));
                    createReqVO.setFailRetryTimes((long) taskDefinition.getFailRetryTimes());
                    createReqVO.setFailRetryInterval((long) taskDefinition.getFailRetryInterval());
                    createReqVO.setTimeout((long) taskDefinition.getTimeout());
                    createReqVO.setDelayTime((long) taskDefinition.getDelayTime());
                    createReqVO.setCpuQuota((long) taskDefinition.getCpuQuota());
                    createReqVO.setMemoryMax((long) taskDefinition.getMemoryMax());
                    createReqVO.setDescription(taskDefinition.getDescription());
                    createReqVO.setDsId(taskDefinition.getId());
                    createReqVO.setParameters(getTaskParamsAsJson(list, String.valueOf(taskDefinition.getCode())));
                    createReqVO.setCreatorId(dppEtlNewNodeSaveReqVO.getCreatorId());
                    createReqVO.setCreateBy(dppEtlNewNodeSaveReqVO.getCreateBy());
                    createReqVO.setCreateTime(dppEtlNewNodeSaveReqVO.getCreateTime());
                    createReqVO.setUpdatorId(dppEtlNewNodeSaveReqVO.getUpdatorId());
                    createReqVO.setUpdateBy(dppEtlNewNodeSaveReqVO.getUpdateBy());
                    createReqVO.setUpdateTime(dppEtlNewNodeSaveReqVO.getUpdateTime());

                    return createReqVO;
                })
                .collect(Collectors.toList());
    }

    public static String getTaskParamsAsJson(List<Map<String, Object>> list, String code) {
        // Find matching taskParams
        Optional<Map<String, Object>> matchingTaskParams = list.stream()
                .filter(task -> task != null && StringUtils.equals(code, MapUtils.getString(task, "code")))
                .map(task -> (Map<String, Object>) MapUtils.getObject(task, "taskParams"))
                .filter(taskParams -> taskParams != null)
                .findFirst();

        // If a matching taskParams is found, convert to JSON and return
        return matchingTaskParams.map(taskParams -> JSONUtils.toJson(taskParams))  // Call JSONUtils to convert to JSON
                .orElse(null);  // If no match found, return null
    }


    public static List<DppEtlNodeLogSaveReqVO> convertToDppEtlNodeLogSaveReqVOList(ProcessDefinition processDefinition, DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO) {
        List<DppEtlNodeLogSaveReqVO> resultList = new ArrayList<>();

        // Extract input parameter info
        List<Map<String, Object>> list = JSONUtils.convertTaskDefinitionJson(dppEtlNewNodeSaveReqVO.getTaskDefinitionList());

        // Iterate over taskDefinitionList in ProcessDefinition
        for (TaskDefinition taskDefinition : processDefinition.getTaskDefinitionList()) {
            // Get the node definition data packaged by the frontend
            Map<String, Object> taskDefinitionMap = list.stream().filter(item -> {
                String code = MapUtils.getString(item, "code", "");
                return StringUtils.equals(taskDefinition.getCode(), code);
            }).findFirst().get();
            DppEtlNodeLogSaveReqVO createReqVO = new DppEtlNodeLogSaveReqVO();

            // 1. Task-related info
            createReqVO.setTaskType(dppEtlNewNodeSaveReqVO.getType());// Task type
            createReqVO.setType(taskDefinition.getTaskType()); // Node type
            createReqVO.setComponentType(String.valueOf(taskDefinitionMap.get("componentType")));// Component type
            createReqVO.setName(taskDefinition.getName()); // Task name
            createReqVO.setCode(String.valueOf(taskDefinition.getCode())); // Task code
            createReqVO.setVersion((long) taskDefinition.getVersion()); // Task version
            createReqVO.setProjectId(dppEtlNewNodeSaveReqVO.getProjectId()); // Project ID
            createReqVO.setProjectCode(String.valueOf(taskDefinition.getProjectCode())); // Project code

            createReqVO.setPriority(String.valueOf(taskDefinition.getTaskPriority()));// Task priority
            createReqVO.setFailRetryTimes((long) taskDefinition.getFailRetryTimes());
            createReqVO.setFailRetryInterval((long) taskDefinition.getFailRetryInterval());
            createReqVO.setTimeout((long) taskDefinition.getTimeout());
            createReqVO.setDelayTime((long) taskDefinition.getDelayTime());
            createReqVO.setCpuQuota((long) taskDefinition.getCpuQuota());
            createReqVO.setMemoryMax((long) taskDefinition.getMemoryMax());
            createReqVO.setDescription(taskDefinition.getDescription());
            createReqVO.setDsId(taskDefinition.getId()); // Set the task's dsId as the node's dsId

            createReqVO.setParameters(getTaskParamsAsJson(list, String.valueOf(taskDefinition.getCode()))); // Node parameters

            // Populate creator and update time info
            createReqVO.setCreatorId(dppEtlNewNodeSaveReqVO.getCreatorId()); // Assuming project ID as creator ID (adjust as needed)
            createReqVO.setCreateBy(dppEtlNewNodeSaveReqVO.getCreateBy()); // Assuming task name as creator (adjust as needed)
            createReqVO.setCreateTime(dppEtlNewNodeSaveReqVO.getCreateTime()); // Set current time as creation time
            createReqVO.setUpdatorId(dppEtlNewNodeSaveReqVO.getUpdatorId()); // Assuming project ID as updater ID (adjust as needed)
            createReqVO.setUpdateBy(dppEtlNewNodeSaveReqVO.getUpdateBy()); // Assuming task name as updater (adjust as needed)
            createReqVO.setUpdateTime(dppEtlNewNodeSaveReqVO.getUpdateTime()); // Set current time as update time

            // Add to result list
            resultList.add(createReqVO);
        }
        return resultList;
    }

    public static long getIdByCode(List<DppEtlNodeDO> dppEtlNodeDOList, String code, long preTaskVersion) {
        return dppEtlNodeDOList.stream()
                .filter(task -> StringUtils.equals(task.getCode(), code) && task.getVersion() == preTaskVersion)  // Match code
                .map(DppEtlNodeDO::getId)  // Get the corresponding id
                .findFirst()  // If a match is found, return the first one
                .orElse(-1L);  // If no match found, return default value -1
    }

    public static List<DppEtlTaskNodeRelSaveReqVO> convertToDppEtlTaskNodeRelSaveReqVOList(List<ProcessTaskRelation> taskRelationList, DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO, List<DppEtlNodeDO> dppEtlNodeBatch, DppEtlTaskSaveReqVO dppEtlTaskSaveReqVO, String code, Integer version) {
        List<DppEtlTaskNodeRelSaveReqVO> resultList = new ArrayList<>();

        // Iterate over taskRelationList in data, generate DppEtlTaskNodeRelSaveReqVO
        for (ProcessTaskRelation taskRelation : taskRelationList) {
            DppEtlTaskNodeRelSaveReqVO taskNodeRelSaveReqVO = new DppEtlTaskNodeRelSaveReqVO();

            // 1. Populate task-node relation fields
            taskNodeRelSaveReqVO.setProjectId(dppEtlNewNodeSaveReqVO.getProjectId()); // Project ID
            taskNodeRelSaveReqVO.setProjectCode(String.valueOf(dppEtlNewNodeSaveReqVO.getProjectCode())); // Project code

            // Task-related fields
            taskNodeRelSaveReqVO.setTaskId(dppEtlTaskSaveReqVO.getId()); // Task ID
            taskNodeRelSaveReqVO.setTaskCode(code); // Task code
            taskNodeRelSaveReqVO.setTaskVersion(version); // Task version

            // Pre-node related fields
            taskNodeRelSaveReqVO.setPreNodeId(getIdByCode(dppEtlNodeBatch, String.valueOf(taskRelation.getPreTaskCode()), taskRelation.getPreTaskVersion())); // Pre-node ID
            taskNodeRelSaveReqVO.setPreNodeCode(String.valueOf(taskRelation.getPreTaskCode())); // Pre-node code
            taskNodeRelSaveReqVO.setPreNodeVersion(taskRelation.getPreTaskVersion()); // Pre-node version

            // Post-node related fields
            taskNodeRelSaveReqVO.setPostNodeId(getIdByCode(dppEtlNodeBatch, code, taskRelation.getPreTaskVersion())); // Post-node ID
            taskNodeRelSaveReqVO.setPostNodeCode(String.valueOf(taskRelation.getPostTaskCode())); // Post-node code
            taskNodeRelSaveReqVO.setPostNodeVersion(taskRelation.getPostTaskVersion()); // Post-node version

            // Optional fields
            taskNodeRelSaveReqVO.setRemark(null); // Remark

            // 2. Populate create/modify info
            taskNodeRelSaveReqVO.setCreatorId(dppEtlNewNodeSaveReqVO.getCreatorId()); // Assuming project ID as creator ID (adjust as needed)
            taskNodeRelSaveReqVO.setCreateBy(dppEtlNewNodeSaveReqVO.getCreateBy()); // Assuming task name as creator (adjust as needed)
            taskNodeRelSaveReqVO.setCreateTime(dppEtlNewNodeSaveReqVO.getCreateTime()); // Set current time as creation time
            taskNodeRelSaveReqVO.setUpdatorId(dppEtlNewNodeSaveReqVO.getUpdatorId()); // Assuming project ID as updater ID (adjust as needed)
            taskNodeRelSaveReqVO.setUpdateBy(dppEtlNewNodeSaveReqVO.getUpdateBy()); // Assuming task name as updater (adjust as needed)
            taskNodeRelSaveReqVO.setUpdateTime(dppEtlNewNodeSaveReqVO.getUpdateTime()); // Set current time as update time

            // Add to result list
            resultList.add(taskNodeRelSaveReqVO);
        }

        return resultList;
    }

    public static List<DppEtlTaskNodeRelLogSaveReqVO> convertToDppEtlTaskNodeRelLogSaveReqVOList(List<ProcessTaskRelation> taskRelationList, DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO, List<DppEtlNodeLogDO> dppEtlNodeBatch, DppEtlTaskLogSaveReqVO dppEtlTaskSaveReqVO, String code, Integer version) {
        List<DppEtlTaskNodeRelLogSaveReqVO> resultList = new ArrayList<>();

        // Iterate over taskRelationList in data, generate DppEtlTaskNodeRelSaveReqVO
        for (ProcessTaskRelation taskRelation : taskRelationList) {
            DppEtlTaskNodeRelLogSaveReqVO taskNodeRelSaveReqVO = new DppEtlTaskNodeRelLogSaveReqVO();

            // 1. Populate task-node relation fields
            taskNodeRelSaveReqVO.setProjectId(dppEtlNewNodeSaveReqVO.getProjectId()); // Project ID
            taskNodeRelSaveReqVO.setProjectCode(String.valueOf(dppEtlNewNodeSaveReqVO.getProjectCode())); // Project code

            // Task-related fields
            taskNodeRelSaveReqVO.setTaskId(dppEtlTaskSaveReqVO.getId()); // Task ID
            taskNodeRelSaveReqVO.setTaskCode(code); // Task code
            taskNodeRelSaveReqVO.setTaskVersion(version); // Task version

            // Pre-node related fields
            taskNodeRelSaveReqVO.setPreNodeId(getDppEtlNodeLogDOIdByCode(dppEtlNodeBatch, String.valueOf(taskRelation.getPreTaskCode()), taskRelation.getPreTaskVersion())); // Pre-node ID
            taskNodeRelSaveReqVO.setPreNodeCode(String.valueOf(taskRelation.getPreTaskCode())); // Pre-node code
            taskNodeRelSaveReqVO.setPreNodeVersion(taskRelation.getPreTaskVersion()); // Pre-node version

            // Post-node related fields
            taskNodeRelSaveReqVO.setPostNodeId(getDppEtlNodeLogDOIdByCode(dppEtlNodeBatch, code, taskRelation.getPreTaskVersion())); // Post-node ID
            taskNodeRelSaveReqVO.setPostNodeCode(String.valueOf(taskRelation.getPostTaskCode())); // Post-node code
            taskNodeRelSaveReqVO.setPostNodeVersion(taskRelation.getPostTaskVersion()); // Post-node version

            // Optional fields
            taskNodeRelSaveReqVO.setRemark(null); // Remark

            // 2. Populate create/modify info
            taskNodeRelSaveReqVO.setCreatorId(dppEtlNewNodeSaveReqVO.getCreatorId()); // Assuming project ID as creator ID (adjust as needed)
            taskNodeRelSaveReqVO.setCreateBy(dppEtlNewNodeSaveReqVO.getCreateBy()); // Assuming task name as creator (adjust as needed)
            taskNodeRelSaveReqVO.setCreateTime(dppEtlNewNodeSaveReqVO.getCreateTime()); // Set current time as creation time
            taskNodeRelSaveReqVO.setUpdatorId(dppEtlNewNodeSaveReqVO.getUpdatorId()); // Assuming project ID as updater ID (adjust as needed)
            taskNodeRelSaveReqVO.setUpdateBy(dppEtlNewNodeSaveReqVO.getUpdateBy()); // Assuming task name as updater (adjust as needed)
            taskNodeRelSaveReqVO.setUpdateTime(dppEtlNewNodeSaveReqVO.getUpdateTime()); // Set current time as update time

            // Add to result list
            resultList.add(taskNodeRelSaveReqVO);
        }

        return resultList;
    }


    public static long getDppEtlNodeLogDOIdByCode(List<DppEtlNodeLogDO> dppEtlNodeDOList, String code, long preTaskVersion) {
        return dppEtlNodeDOList.stream()
                .filter(task -> StringUtils.equals(task.getCode(), code) && task.getVersion() == preTaskVersion)  // Match code
                .map(DppEtlNodeLogDO::getId)  // Get the corresponding id
                .findFirst()  // If a match is found, return the first one
                .orElse(-1L);  // If no match found, return default value -1
    }


    /**
     * Utility method to generate DsSchedulerSaveReqDTO.
     *
     * @param crontab               Cron expression
     * @param processDefinitionCode Task code
     * @return DsSchedulerSaveReqDTO
     */
    public static DsSchedulerSaveReqDTO createSchedulerRequest(String crontab, String processDefinitionCode) {
        // Get current time
        String startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        // Get time 100 years later
        long currentTime = System.currentTimeMillis();
        String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(currentTime + 100L * 365 * 24 * 60 * 60 * 1000));

        // Create DsSchedulerSaveReqDTO and set default values
        DsSchedulerSaveReqDTO dto = new DsSchedulerSaveReqDTO();
        dto.setSchedule(String.format("{\"startTime\":\"%s\",\"endTime\":\"%s\",\"crontab\":\"%s\",\"timezoneId\":\"Asia/Shanghai\"}",
                startTime, endTime, crontab));
        dto.setProcessDefinitionCode(processDefinitionCode);
        dto.setFailureStrategy("CONTINUE");
        dto.setWorkerGroup("default");
        dto.setTenantCode("default");

        return dto;
    }


    /**
     * Convert DsSchedulerRespDTO to DppEtlSchedulerSaveReqVO
     *
     * @param dsSchedulerRespDTO DsSchedulerRespDTO
     * @param dppEtlTaskDO
     * @return DppEtlSchedulerSaveReqVO
     */
    public static DppEtlSchedulerSaveReqVO convertToDppEtlSchedulerSaveReqVO(DsSchedulerRespDTO dsSchedulerRespDTO, DppEtlTaskDO dppEtlTaskDO) {
        // Create DppEtlSchedulerSaveReqVO object
        DppEtlSchedulerSaveReqVO reqVO = new DppEtlSchedulerSaveReqVO();

        // Extract data from dsSchedulerRespDTO and populate reqVO
        Schedule schedule = dsSchedulerRespDTO.getData();

        reqVO.setStartTime(schedule.getStartTime());
        reqVO.setEndTime(schedule.getEndTime());
        reqVO.setTimezoneId(schedule.getTimezoneId());
        reqVO.setCronExpression(schedule.getCrontab());
        reqVO.setFailureStrategy("1");

        // You can fill in default values or handle dsId and remark fields as needed
        reqVO.setDsId(schedule.getId()); // Assume dsId and id are the same
        reqVO.setRemark(null); // Remark can be modified based on actual requirements

        return reqVO;
    }


    /**
     * Utility method to generate DsSchedulerUpdateReqDTO.
     *
     * @param id                    Schedule ID
     * @param crontab               Cron expression
     * @param processDefinitionCode Task code
     * @return DsSchedulerUpdateReqDTO
     */
    public static DsSchedulerUpdateReqDTO createSchedulerUpdateRequest(Long id, String crontab, String processDefinitionCode) {
        // Get current time
        String startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        // Get time 100 years later
        long currentTime = System.currentTimeMillis();
        String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(currentTime + 100L * 365 * 24 * 60 * 60 * 1000));

        // Create DsSchedulerUpdateReqDTO and set default values
        DsSchedulerUpdateReqDTO dto = new DsSchedulerUpdateReqDTO();
        dto.setId(id); // Set schedule ID
        dto.setSchedule(String.format("{\"startTime\":\"%s\",\"endTime\":\"%s\",\"crontab\":\"%s\",\"timezoneId\":\"Asia/Shanghai\"}",
                startTime, endTime, crontab));
        dto.setProcessDefinitionCode(processDefinitionCode);
        dto.setFailureStrategy("CONTINUE");
        dto.setWorkerGroup("default");
        dto.setTenantCode("default");

        return dto;
    }


    /**
     * Convert DsSchedulerSaveReqDTO to DppEtlSchedulerSaveReqVO
     *
     * @param dppEtlNewNodeSaveReqVO
     * @return DppEtlSchedulerSaveReqVO
     */
    public static DppEtlSchedulerSaveReqVO convertToDppEtlSchedulerSaveReqVO(Long taskId, String taskCode, DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO) {
        // Create DppEtlSchedulerSaveReqVO object
        DppEtlSchedulerSaveReqVO reqVO = new DppEtlSchedulerSaveReqVO();

        // Populate fields directly from dsSchedulerSaveReqDTO
        reqVO.setTaskId(taskId);
        reqVO.setTaskCode(taskCode);

        // 获取100年后的时间
        long currentTime = System.currentTimeMillis();
        Date date = new Date(currentTime + 100L * 365 * 24 * 60 * 60 * 1000);

        reqVO.setStartTime(new Date());
        reqVO.setEndTime(date);
        reqVO.setTimezoneId("Asia/Shanghai"); // Default timezone

        reqVO.setCronExpression(dppEtlNewNodeSaveReqVO.getCrontab());
        reqVO.setFailureStrategy("1");
        reqVO.setStatus("0");
        reqVO.setTaskScheduler(dppEtlNewNodeSaveReqVO.getScheduler());
        reqVO.setTaskActuator(dppEtlNewNodeSaveReqVO.getActuator());

        // Populate dsId, assuming dsId is the same as ID
        reqVO.setDsId((long) -1);

        // Remark can be filled based on requirements

        return reqVO;
    }

    // Method to parse the time portion from the schedule field
    private static Date parseStartTime(String scheduleJson) {
        // Extract and parse start time (assuming you have a method to parse it from JSON)
        return new Date(); // Example, actually need to extract the corresponding time
    }

    private static Date parseEndTime(String scheduleJson) {
        // Extract and parse end time (assuming you have a method to parse it from JSON)
        return new Date(); // Example, actually need to extract the corresponding time
    }

    public static List<String> getPreAndPostNodeCodeList(List<DppEtlTaskNodeRelRespVO> dppEtlTaskNodeRelRespVOList) {
        List<String> result = new ArrayList<>();
        for (DppEtlTaskNodeRelRespVO vo : dppEtlTaskNodeRelRespVOList) {
            result.add(vo.getPreNodeCode());  // Add preNodeCode
            result.add(vo.getPostNodeCode()); // Add postNodeCode
        }
        return result;  // Return List<String>
    }


    public static DsStartTaskReqDTO createDsStartTaskReqDTO(String processDefinitionCode) {
        // Get current date in "yyyy-MM-dd" format
        String currentDate = DateUtil.today();
        // Construct scheduleTime field, fixed format "yyyy-MM-dd 00:00:00"
        String scheduleTime = String.format("{\"complementStartDate\":\"%s 00:00:00\",\"complementEndDate\":\"%s 00:00:00\"}", currentDate, currentDate);

        // Use builder pattern to create DsStartTaskReqDTO object, other fields are hardcoded values
        return DsStartTaskReqDTO.builder()
                .processDefinitionCode(JSONUtils.convertToLong(processDefinitionCode))
                .failureStrategy("CONTINUE")
                .warningType(DEFAULT_CONDITION_TYPE)
                .processInstancePriority(DEFAULT_TASK_PRIORITY)
                .scheduleTime(scheduleTime)
                .build();
    }


    /**
     * Build ETL location coordinate data
     *
     * @param locations
     * @param code
     * @return
     */
    public static String buildEtlTaskLocationsJson(List<Map<String, Object>> locations, String code) {
        List<Map<String, Object>> locationList = new ArrayList<>();

        Map<String, Object> location = locations.get(0);
        Map<String, Object> locationMap = new HashMap<>();
        // Populate required fields
        locationMap.put("taskCode", Long.parseLong(code)); // Default taskCode is 0
        locationMap.put("x", location.getOrDefault("x", 0)); // Default x is 0
        locationMap.put("y", location.getOrDefault("y", 0)); // Default y is 0
        locationList.add(locationMap);
        return JSON.toJSONString(locationList);
    }

    /**
     * Build ETL node relation JSON data
     *
     * @param code
     * @return
     */
    public static String buildEtlTaskRelationJson(Long id, String code) {
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> taskRelation = new HashMap<>();
        taskRelation.put("id", id);
        taskRelation.put("preTaskCode", 0);
        taskRelation.put("preTaskVersion", 0);
        taskRelation.put("postTaskCode", Long.parseLong(code));
        taskRelation.put("postTaskVersion", 0);
        taskRelation.put("conditionType", "NONE");
        result.add(taskRelation);
        return JSON.toJSONString(result);
    }


    /**
     * Build ETL node definition JSON data
     *
     * @param id        ETL node id
     * @param name      ETL node name
     * @param code      ETL node code
     * @param version   ETL node version
     * @param mainArgs  ETL node parameters
     * @param draftJson
     * @return
     */
    public static String buildEtlTaskDefinitionJson(Long id, String name, String code, Integer version, Map<String, Object> mainArgs, String draftJson) {
        List<Map<String, Object>> result = new ArrayList<>();
        // Custom parameters
        Map<String, Object> definitionJsonMap = JSONUtils.convertTaskDefinitionJsonMap(draftJson);

        // Process default values and required fields for each task
        Map<String, Object> taskMap = new HashMap<>();

        // Set basic info fields
        taskMap.put("id", id); // Default id is 0
        taskMap.put("name", name); // Default empty string
        taskMap.put("code", code); // Default code is 0L
        taskMap.put("version", version); // Default version is 1
        taskMap.put("description", ""); // Default empty description
        taskMap.put("workerGroup", MapUtils.getObject(definitionJsonMap,"workerGroup",DEFAULT_WORKER_GROUP) ); // Default workerGroup is "default"
        taskMap.put("environmentCode", DEFAULT_ENVIRONMENT_CODE); // Default environment code
        taskMap.put("flag", DEFAULT_FLAG); // Default flag is "YES"
        taskMap.put("isCache", DEFAULT_IS_CACHE); // Default isCache is "NO"
        taskMap.put("taskPriority", MapUtils.getObject(definitionJsonMap,"taskPriority",DEFAULT_TASK_PRIORITY)); // Default task priority is "MEDIUM"
        taskMap.put("taskType", DEFAULT_TASK_TYPE); // Default task type is "SPARK"
        taskMap.put("taskExecuteType", "BATCH");

        // 2025-06-25 Added new config item defaults
        taskMap.put("failRetryTimes", MapUtils.getObject(definitionJsonMap,"failRetryTimes",DEFAULT_TASK_failRetryTimes));
        taskMap.put("delayTime", MapUtils.getObject(definitionJsonMap,"delayTime",DEFAULT_TASK_delayTime));
        taskMap.put("failRetryInterval", MapUtils.getObject(definitionJsonMap,"failRetryInterval",DEFAULT_TASK_failRetryInterval));

        Map<String, Object> taskParams = new LinkedHashMap<>();

        taskParams.put("localParams", new ArrayList<>()); // Default empty list
        taskParams.put("rawScript", ""); // Default empty string
        taskParams.put("resourceList", new ArrayList<>()); // Default empty list
        taskParams.put("programType", DEFAULT_PROGRAM_TYPE); // Default program type is "JAVA"
        taskParams.put("mainClass", defaultMainClass);

        // mainJar is a Map, with resourceName field set to default value
        Map<String, Object> mainJar = new HashMap<>();
        mainJar.put("resourceName", resourceName);
        taskParams.put("mainJar", mainJar);
        taskParams.put("deployMode", DEFAULT_DEPLOY_MODE); // Default deploy mode is "client"
        taskParams.put("mainArgs", Base64.encode(JSON.toJSONString(mainArgs))); // Default empty string
        taskParams.put("master", defaultMaster); // Default Spark master URL
        taskParams.put("driverCores",MapUtils.getObject(definitionJsonMap,"driverCores",DEFAULT_DRIVER_CORES) ); // Default driver cores
        taskParams.put("driverMemory",MapUtils.getObject(definitionJsonMap,"driverMemory",DEFAULT_DRIVER_MEMORY) ); // Default driver memory
        taskParams.put("numExecutors", MapUtils.getObject(definitionJsonMap,"numExecutors",DEFAULT_NUM_EXECUTORS)); // Default number of executors
        taskParams.put("executorMemory",MapUtils.getObject(definitionJsonMap,"executorMemory",DEFAULT_EXECUTOR_MEMORY) ); // Default executor memory
        taskParams.put("executorCores",MapUtils.getObject(definitionJsonMap,"executorCores",DEFAULT_EXECUTOR_CORES) ); // Default executor cores
        taskParams.put("yarnQueue",MapUtils.getObject(definitionJsonMap,"yarnQueue","") ); // Default YARN queue
        taskParams.put("sqlExecutionType", DEFAULT_SQL_EXECUTION_TYPE); // Default SQL execution type is "SCRIPT"

        // Add the task's taskParams to taskMap
        taskMap.put("taskParams", taskParams);

        // Add the populated task to the result list
        result.add(taskMap);
        // Return the processed JSON string
        return JSON.toJSONString(result);
    }

    public static List<DppEtlNodeSaveReqVO> convertToDppEtlNodeSaveReqVOList(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO, String taskDefinitionJson) {
        List<DppEtlNodeSaveReqVO> resultList = new ArrayList<>();

        // Extract input parameter info
        List<DppEtlNodeSaveReqVO> list = JSON.parseArray(taskDefinitionJson, DppEtlNodeSaveReqVO.class);

        // Iterate over taskDefinitionList in ProcessDefinition
        for (DppEtlNodeSaveReqVO createReqVO : list) {
            // 1. Task-related info
            createReqVO.setType(createReqVO.getTaskType());// Node type
            createReqVO.setTaskType(dppEtlNewNodeSaveReqVO.getType());// Task type
            createReqVO.setVersion(1); // Task version
            createReqVO.setProjectId(dppEtlNewNodeSaveReqVO.getProjectId()); // Project ID
            createReqVO.setProjectCode(String.valueOf(dppEtlNewNodeSaveReqVO.getProjectCode())); // Project code
            // Populate creator and update time info
            createReqVO.setCreatorId(dppEtlNewNodeSaveReqVO.getCreatorId()); // Assuming project ID as creator ID (adjust as needed)
            createReqVO.setCreateBy(dppEtlNewNodeSaveReqVO.getCreateBy()); // Assuming task name as creator (adjust as needed)
            createReqVO.setCreateTime(dppEtlNewNodeSaveReqVO.getCreateTime()); // Set current time as creation time
            createReqVO.setUpdatorId(dppEtlNewNodeSaveReqVO.getUpdatorId()); // Assuming project ID as updater ID (adjust as needed)
            createReqVO.setUpdateBy(dppEtlNewNodeSaveReqVO.getUpdateBy()); // Assuming task name as updater (adjust as needed)
            createReqVO.setUpdateTime(dppEtlNewNodeSaveReqVO.getUpdateTime()); // Set current time as update time
            createReqVO.setParameters(JSON.toJSONString(createReqVO.getTaskParams()));
            // Add to result list
            resultList.add(createReqVO);
        }
        return resultList;
    }

    public static List<DppEtlNodeSaveReqVO> convertToDppEtlNodeSaveReqVOList(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO, Integer nodeVersion) {
        List<DppEtlNodeSaveReqVO> resultList = new ArrayList<>();
        // Extract input parameter info
        List<DppEtlNodeSaveReqVO> list = JSON.parseArray(dppEtlNewNodeSaveReqVO.getTaskDefinitionList(), DppEtlNodeSaveReqVO.class);

        // Iterate over taskDefinitionList in ProcessDefinition
        for (DppEtlNodeSaveReqVO createReqVO : list) {
            // 1. Task-related info
            createReqVO.setType(createReqVO.getTaskType());// Node type
            createReqVO.setTaskType(dppEtlNewNodeSaveReqVO.getType());// Task type
            createReqVO.setProjectId(dppEtlNewNodeSaveReqVO.getProjectId()); // Project ID
            createReqVO.setProjectCode(String.valueOf(dppEtlNewNodeSaveReqVO.getProjectCode())); // Project code
            // Populate creator and update time info
            createReqVO.setCreatorId(dppEtlNewNodeSaveReqVO.getCreatorId()); // Assuming project ID as creator ID (adjust as needed)
            createReqVO.setCreateBy(dppEtlNewNodeSaveReqVO.getCreateBy()); // Assuming task name as creator (adjust as needed)
            createReqVO.setCreateTime(dppEtlNewNodeSaveReqVO.getCreateTime()); // Set current time as creation time
            createReqVO.setUpdatorId(dppEtlNewNodeSaveReqVO.getUpdatorId()); // Assuming project ID as updater ID (adjust as needed)
            createReqVO.setUpdateBy(dppEtlNewNodeSaveReqVO.getUpdateBy()); // Assuming task name as updater (adjust as needed)
            createReqVO.setUpdateTime(dppEtlNewNodeSaveReqVO.getUpdateTime()); // Set current time as update time
            createReqVO.setParameters(JSON.toJSONString(createReqVO.getTaskParams()));
            // Add to result list
            resultList.add(createReqVO);
        }
        if (nodeVersion != null) {
            list.forEach(i -> i.setVersion(nodeVersion));
        }
        return resultList;
    }


    public static List<DppEtlNodeLogSaveReqVO> convertToDppEtlNodeLogSaveReqVOList(List<DppEtlNodeSaveReqVO> dppEtlNodeSaveReqVOList) {
        List<DppEtlNodeLogSaveReqVO> resultList = new ArrayList<>();
        for (DppEtlNodeSaveReqVO dppEtlNodeSaveReqVO : dppEtlNodeSaveReqVOList) {
            resultList.add(BeanUtils.toBean(dppEtlNodeSaveReqVO, DppEtlNodeLogSaveReqVO.class));
        }
        return resultList;
    }

    public static List<DppEtlTaskNodeRelSaveReqVO> convertToDppEtlTaskNodeRelSaveReqVOList(List<DppEtlNodeDO> dppEtlNodeBatch, DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO, DppEtlTaskSaveReqVO dppEtlTaskSaveReqVO) {
        List<DppEtlTaskNodeRelSaveReqVO> resultList = new ArrayList<>();
        List<ProcessTaskRelation> list = JSON.parseArray(dppEtlNewNodeSaveReqVO.getTaskRelationJson(), ProcessTaskRelation.class);
        // Iterate over taskRelationList in data, generate DppEtlTaskNodeRelSaveReqVO
        for (ProcessTaskRelation taskRelation : list) {

            DppEtlTaskNodeRelSaveReqVO taskNodeRelSaveReqVO = new DppEtlTaskNodeRelSaveReqVO();

            // 1. Populate task-node relation fields
            taskNodeRelSaveReqVO.setProjectId(dppEtlNewNodeSaveReqVO.getProjectId()); // Project ID
            taskNodeRelSaveReqVO.setProjectCode(String.valueOf(dppEtlNewNodeSaveReqVO.getProjectCode())); // Project code

            // Task-related fields
            taskNodeRelSaveReqVO.setTaskId(dppEtlTaskSaveReqVO.getId()); // Task ID
            taskNodeRelSaveReqVO.setTaskCode(dppEtlTaskSaveReqVO.getCode()); // Task code
            taskNodeRelSaveReqVO.setTaskVersion(dppEtlTaskSaveReqVO.getVersion()); // Task version

            // Pre-node related fields
            taskNodeRelSaveReqVO.setPreNodeCode(String.valueOf(taskRelation.getPreTaskCode())); // Pre-node code
            taskNodeRelSaveReqVO.setPreNodeVersion(taskRelation.getPreTaskVersion()); // Pre-node version
            if (StringUtils.isNotEmpty(taskNodeRelSaveReqVO.getPreNodeCode()) && taskNodeRelSaveReqVO.getPreNodeVersion() == 0) {
                taskNodeRelSaveReqVO.setPreNodeVersion(1);
            }
            taskNodeRelSaveReqVO.setPreNodeId(getIdByCode(dppEtlNodeBatch, taskNodeRelSaveReqVO.getPreNodeCode(), taskNodeRelSaveReqVO.getPreNodeVersion())); // Pre-node ID

            // Post-node related fields
            taskNodeRelSaveReqVO.setPostNodeCode(String.valueOf(taskRelation.getPostTaskCode())); // Post-node code
            taskNodeRelSaveReqVO.setPostNodeVersion(taskRelation.getPostTaskVersion()); // Post-node version
            if (StringUtils.isNotEmpty(taskNodeRelSaveReqVO.getPostNodeCode()) && taskNodeRelSaveReqVO.getPostNodeVersion() == 0) {
                taskNodeRelSaveReqVO.setPostNodeVersion(1);
            }
            taskNodeRelSaveReqVO.setPostNodeId(getIdByCode(dppEtlNodeBatch, taskNodeRelSaveReqVO.getPostNodeCode(), taskNodeRelSaveReqVO.getPostNodeVersion())); // Post-node ID

            // Optional fields
            taskNodeRelSaveReqVO.setRemark(null); // Remark

            // 2. Populate create/modify info
            taskNodeRelSaveReqVO.setCreatorId(dppEtlNewNodeSaveReqVO.getCreatorId()); // Assuming project ID as creator ID (adjust as needed)
            taskNodeRelSaveReqVO.setCreateBy(dppEtlNewNodeSaveReqVO.getCreateBy()); // Assuming task name as creator (adjust as needed)
            taskNodeRelSaveReqVO.setCreateTime(dppEtlNewNodeSaveReqVO.getCreateTime()); // Set current time as creation time
            taskNodeRelSaveReqVO.setUpdatorId(dppEtlNewNodeSaveReqVO.getUpdatorId()); // Assuming project ID as updater ID (adjust as needed)
            taskNodeRelSaveReqVO.setUpdateBy(dppEtlNewNodeSaveReqVO.getUpdateBy()); // Assuming task name as updater (adjust as needed)
            taskNodeRelSaveReqVO.setUpdateTime(dppEtlNewNodeSaveReqVO.getUpdateTime()); // Set current time as update time

            // Add to result list
            resultList.add(taskNodeRelSaveReqVO);
        }
        return resultList;
    }

    public static List<DppEtlTaskNodeRelLogSaveReqVO> convertToDppEtlTaskNodeRelLogSaveReqVOList(List<DppEtlTaskNodeRelSaveReqVO> dppEtlTaskNodeRelSaveReqVOS) {
        List<DppEtlTaskNodeRelLogSaveReqVO> resultList = new ArrayList<>();
        for (DppEtlTaskNodeRelSaveReqVO dppEtlNodeSaveReqVO : dppEtlTaskNodeRelSaveReqVOS) {
            resultList.add(BeanUtils.toBean(dppEtlNodeSaveReqVO, DppEtlTaskNodeRelLogSaveReqVO.class));
        }
        return resultList;
    }

    /**
     * Build ETL parameter data
     *
     * @return
     */
    public static Map<String, Object> buildEtlTaskParams(String taskDefinitionList, Map<String, DppEtlNodeSaveReqVO> nodeMap, Map<String, Object> taskInfo, List<DsResource> resourceList) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> transitionList = new ArrayList<>();
        List<DppEtlNodeSaveReqVO> nodeList = JSON.parseArray(taskDefinitionList, DppEtlNodeSaveReqVO.class);
        for (DppEtlNodeSaveReqVO dppEtlNodeSaveReqVO : nodeList) {
            Integer version = 1;
            if (nodeMap.containsKey(dppEtlNodeSaveReqVO.getCode())) {
                version = nodeMap.get(dppEtlNodeSaveReqVO.getCode()).getVersion();
            }
            // Component types in this method include DB_READER, EXCEL_READER, CSV_READER, SPARK_CLEAN, DB_WRITER
            String componentType = dppEtlNodeSaveReqVO.getComponentType();
            TaskComponentTypeEnum taskComponentTypeEnum = TaskComponentTypeEnum.findEnumByType(componentType);
            Map<String, Object> data = ComponentFactory.getComponentItem(componentType)
                    .parse2(dppEtlNodeSaveReqVO.getCode(), version, taskComponentTypeEnum, dppEtlNodeSaveReqVO.getTaskParams(), resourceUrl, resourceList);
            data.put("nodeName", dppEtlNodeSaveReqVO.getName());
            data.put("projectCode", taskInfo.get("projectCode"));
            switch (taskComponentTypeEnum) {
                case DB_READER:
                case EXCEL_READER:
                case CSV_READER:
                    result.put("reader", data);
                    break;
                case SPARK_CLEAN:
                case SORT_RECORD:
                case FIELD_DERIVATION:
                case DATA_DEDUPLICATION:
                case VALUE_MAP:
                case ADD_CONSTANT:
                case SELECT_FIELDS:
                    transitionList.add(data);
                    break;
                case DB_WRITER:
                    result.put("writer", data);
                    break;
            }
        }
        // Configure config
        Map<String, Object> config = new HashMap<>();
        config.put("taskInfo", taskInfo);
        // Redis config info used by EtlApplication.java (to get the latest data source info, ensuring task execution)
        config.put("redis", dsRedisConfig);
        config.put("rabbitmq", rabbitmqConfig);
        config.put("resourceUrl", resourceUrl);
        result.put("transition", transitionList);
        result.put("config", config);
        return result;
    }
}
