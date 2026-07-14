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

    // Implementation details.
    private static final long DEFAULT_ENVIRONMENT_CODE = 133155949418208L; // Implementation details.
    private static final String DEFAULT_WORKER_GROUP = "default"; // Implementation details.
    private static final String DEFAULT_FLAG = "YES"; // Handle node-related data and operations.
    private static final String DEFAULT_IS_CACHE = "NO"; // Implementation details.
    private static final String DEFAULT_TASK_PRIORITY = "MEDIUM"; // Handle task-related data and operations.
    private static final String DEFAULT_TASK_TYPE = "SPARK"; // Handle task-related data and operations.
    private static final String DEFAULT_PROGRAM_TYPE = "JAVA"; // Implementation details.
    private static final String DEFAULT_MAIN_JAR = "file:/dolphinscheduler/default/resources/spart-demo-1.0.jar"; // Implementation details.
    private static final String DEFAULT_DEPLOY_MODE = "client"; // Implementation details.
    private static final int DEFAULT_DRIVER_CORES = 1; // Implementation details.
    private static final String DEFAULT_DRIVER_MEMORY = "2G"; // Implementation details.
    private static final int DEFAULT_NUM_EXECUTORS = 1; // Implementation details.
    private static final String DEFAULT_EXECUTOR_MEMORY = "4G"; // Implementation details.
    private static final int DEFAULT_EXECUTOR_CORES = 2; // Implementation details.
    private static final String DEFAULT_SQL_EXECUTION_TYPE = "SCRIPT"; // Handle JDBC SQL execution.
    private static final String DEFAULT_CONDITION_TYPE = "NONE"; // Implementation details.

    private static final int DEFAULT_TASK_failRetryTimes = 0; // Implementation details.
    private static final int DEFAULT_TASK_delayTime = 0; // Implementation details.
    private static final int DEFAULT_TASK_failRetryInterval = 1; // Implementation details.



    public static final String TASK_INSTANCE_LOG_KEY = "log:taskInstanceLog:";// Handle task-related data and operations.

    public static final String PROCESS_INSTANCE_LOG_KEY = "log:processInstanceLog:";// Handle execution logging.

    public static final String ETL_READER_ID_KEY = "etl:reader:id:";

    public static final String ETL_READER_DATE_KEY = "etl:reader:date:";

    public static DsTaskSaveReqDTO buildDsTaskSaveReq(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO) {// Implementation details.
        // Create the required record.
        DsTaskSaveReqDTO dsTaskSaveReqDTO = new DsTaskSaveReqDTO();
        // Implementation details.
        dsTaskSaveReqDTO.setName(dppEtlNewNodeSaveReqVO.getName());
        if(StringUtils.isNotEmpty(dppEtlNewNodeSaveReqVO.getCode())){
            dsTaskSaveReqDTO.setProcessDefinitionCode(Long.parseLong(dppEtlNewNodeSaveReqVO.getCode()));
        }
        dsTaskSaveReqDTO.setDescription(dppEtlNewNodeSaveReqVO.getDescription());
        dsTaskSaveReqDTO.setExecutionType(dppEtlNewNodeSaveReqVO.getExecutionType());


        // Handle node-related data and operations.
        String taskDefinition = buildTaskDefinition(dppEtlNewNodeSaveReqVO.getTaskDefinitionList());

        String taskRelation = buildTaskRelationJson(dppEtlNewNodeSaveReqVO.getTaskRelationJson());

        String location = buildTaskNodeLocations(dppEtlNewNodeSaveReqVO.getLocations());


        dsTaskSaveReqDTO.setTaskDefinitionJson(taskDefinition);
        dsTaskSaveReqDTO.setTaskRelationJson(taskRelation);
        dsTaskSaveReqDTO.setLocations(location);


        return dsTaskSaveReqDTO;
    }

    private static String buildTaskNodeLocations(List<Map<String, Object>> locations) {
        // Handle JSON data for this operation.
        List<Map<String, Object>> list = locations;

        List<Map<String, Object>> result = new ArrayList<>();

        // Handle node-related data and operations.
        for (Map<String, Object> location : list) {
            Map<String, Object> locationMap = new HashMap<>();

            // Implementation details.
            locationMap.put("taskCode", Long.parseLong(String.valueOf(location.getOrDefault("taskCode", 0L)))); // Implementation details.
            locationMap.put("x", location.getOrDefault("x", 0)); // Implementation details.
            locationMap.put("y", location.getOrDefault("y", 0)); // Implementation details.

            // Implementation details.
            result.add(locationMap);
        }

        // Handle JSON data for this operation.
        return JSON.toJSONString(result);
    }

    private static String buildTaskRelationJson(String taskRelationJson) {
        // Handle JSON data for this operation.
        List<Map<String, Object>> list = JSONUtils.convertTaskDefinitionJson(taskRelationJson);

        List<Map<String, Object>> result = new ArrayList<>();

        // Handle node-related data and operations.
        for (Map<String, Object> relation : list) {
            Map<String, Object> relationMap = new HashMap<>();

            // Implementation details.
            relationMap.put("id", relation.getOrDefault("dsId", null)); // Implementation details.
            relationMap.put("preTaskCode", relation.getOrDefault("preTaskCode", 0L)); // Implementation details.
            relationMap.put("preTaskVersion", relation.getOrDefault("preTaskVersion", 0)); // Implementation details.
            relationMap.put("postTaskCode", relation.getOrDefault("postTaskCode", 0L)); // Implementation details.
            relationMap.put("postTaskVersion", relation.getOrDefault("postTaskVersion", 0)); // Implementation details.
            relationMap.put("conditionType", relation.getOrDefault("conditionType", DEFAULT_CONDITION_TYPE)); // Implementation details.

            // Handle node-related data and operations.
            result.add(relationMap);
        }

        // Handle JSON data for this operation.
        return JSON.toJSONString(result);
    }

    /**
     * Handle task-related data and operations.
     *
     * @param taskDefinitionJson parameter value
     * @return the operation result
     */
    public static String buildTaskDefinition(String taskDefinitionJson) {
        List<Map<String, Object>> list = JSONUtils.convertTaskDefinitionJson(taskDefinitionJson);

        List<Map<String, Object>> result = new ArrayList<>();

        // Implementation details.
//        Map<String, Object> definitionJsonMap = JSONUtils.convertTaskDefinitionJsonMap(draftJson);


        // Handle task-related data and operations.
        for (Map<String, Object> task : list) {
            // Implementation details.
            Map<String, Object> taskMap = new HashMap<>();

            // Implementation details.
            taskMap.put("id", task.getOrDefault("dsId", null)); // Implementation details.
            taskMap.put("name", task.getOrDefault("name", "")); // Implementation details.
            taskMap.put("code", task.getOrDefault("code", 0L)); // Implementation details.
            taskMap.put("version", task.getOrDefault("version", 0)); // Implementation details.
            taskMap.put("description", task.getOrDefault("description", "")); // Implementation details.
            taskMap.put("workerGroup", task.getOrDefault("workerGroup", DEFAULT_WORKER_GROUP)); // Implementation details.
            taskMap.put("environmentCode", task.getOrDefault("environmentCode", DEFAULT_ENVIRONMENT_CODE)); // Implementation details.
            taskMap.put("flag", DEFAULT_FLAG); // Implementation details.
            taskMap.put("isCache", task.getOrDefault("isCache", DEFAULT_IS_CACHE)); // Implementation details.
            taskMap.put("taskPriority", task.getOrDefault("taskPriority", DEFAULT_TASK_PRIORITY)); // Handle task-related data and operations.
            taskMap.put("taskType", task.getOrDefault("taskType", DEFAULT_TASK_TYPE)); // Handle task-related data and operations.
            taskMap.put("taskExecuteType", "BATCH");

            // Implementation details.
            taskMap.put("failRetryTimes", MapUtils.getObject(task,"failRetryTimes",DEFAULT_TASK_failRetryTimes));
            taskMap.put("delayTime", MapUtils.getObject(task,"delayTime",DEFAULT_TASK_delayTime));
            taskMap.put("failRetryInterval", MapUtils.getObject(task,"failRetryInterval",DEFAULT_TASK_failRetryInterval));

            // Implementation details.
            String componentType = String.valueOf(task.get("componentType")); // Implementation details.
            Map<String, Object> params = (Map<String, Object>) MapUtils.getObject(task, "taskParams");

            // Implementation details.
            if (StringUtils.equals(TaskComponentTypeEnum.SPARK_CLEAN.getCode(), componentType)
                    || StringUtils.equals(TaskComponentTypeEnum.SPARK_SQL_DEV.getCode(), componentType)) {
                params.put("mainClass", defaultMainClass);
                params.put("resourceName", resourceName);
                params.put("master", defaultMaster);
            }
            // Implementation details.
//            params.put("driverCores", MapUtils.getObject(definitionJsonMap, "driverCores", DEFAULT_DRIVER_CORES));
//            params.put("driverMemory", MapUtils.getObject(definitionJsonMap, "driverMemory", DEFAULT_DRIVER_MEMORY));
//            params.put("numExecutors", MapUtils.getObject(definitionJsonMap, "numExecutors", DEFAULT_NUM_EXECUTORS));
//            params.put("executorMemory", MapUtils.getObject(definitionJsonMap, "executorMemory", DEFAULT_EXECUTOR_MEMORY));
//            params.put("executorCores", MapUtils.getObject(definitionJsonMap, "executorCores", DEFAULT_EXECUTOR_CORES));
//            params.put("yarnQueue", MapUtils.getObject(definitionJsonMap, "yarnQueue", ""));

            // Handle task-related data and operations.
            taskMap.put("taskParams", ComponentFactory.getComponentItem(componentType).parse(params));

            // Handle task-related data and operations.
            result.add(taskMap);
        }

        // Handle JSON data for this operation.
        return JSON.toJSONString(result);
    }


    /**
     * Implementation details.
     *
     * @param dppEtlNewNodeSaveReqVO parameter value
     * @param data parameter value
     * @return the operation result
     */
    public static DppEtlTaskSaveReqVO convertToDppEtlTaskSaveReqVO(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO, ProcessDefinition data) {
        // Create the required record.
        DppEtlTaskSaveReqVO createReqVO = new DppEtlTaskSaveReqVO();

        // Handle task-related data and operations.
        createReqVO.setType(dppEtlNewNodeSaveReqVO.getType());// Handle task-related data and operations.
        createReqVO.setName(data.getName()); // Handle task-related data and operations.
        createReqVO.setCode(String.valueOf(data.getCode())); // Task code
        createReqVO.setVersion(data.getVersion()); // Implementation details.
        createReqVO.setProjectId(dppEtlNewNodeSaveReqVO.getProjectId()); // Implementation details.
        createReqVO.setProjectCode(String.valueOf(data.getProjectCode())); // Implementation details.
        createReqVO.setDescription(dppEtlNewNodeSaveReqVO.getDescription()); // Implementation details.
        createReqVO.setLocations(data.getLocations()); // Handle node-related data and operations.
        createReqVO.setLocations(data.getLocations()); // Handle node-related data and operations.
        createReqVO.setDsId(data.getId()); // Handle DolphinScheduler operations.

        String releaseState = dppEtlNewNodeSaveReqVO.getReleaseState();
        // Implementation details.
        if (StringUtils.equals("-2", releaseState) || StringUtils.equals("-3", releaseState)) {
            createReqVO.setStatus(releaseState); // Implementation details.
        } else if ("offline".equalsIgnoreCase(data.getReleaseState())) {
            createReqVO.setStatus("0"); // Implementation details.
        } else if ("online".equalsIgnoreCase(data.getReleaseState())) {
            createReqVO.setStatus("1"); // Implementation details.
        } else {
            createReqVO.setStatus("0"); // Implementation details.
        }
        createReqVO.setRemark(""); // Implementation details.

        createReqVO.setExecutionType(data.getExecutionType());// Implementation details.
        // Create the required record.
        createReqVO.setCreatorId(dppEtlNewNodeSaveReqVO.getCreatorId()); // Create the required record.
        createReqVO.setCreateBy(dppEtlNewNodeSaveReqVO.getCreateBy()); // Handle task-related data and operations.
        createReqVO.setCreateTime(dppEtlNewNodeSaveReqVO.getCreateTime()); // Create the required record.
        createReqVO.setUpdatorId(dppEtlNewNodeSaveReqVO.getUpdatorId()); // Update the related record.
        createReqVO.setUpdateBy(dppEtlNewNodeSaveReqVO.getUpdateBy()); // Handle task-related data and operations.
        createReqVO.setUpdateTime(dppEtlNewNodeSaveReqVO.getUpdateTime()); // Update the related record.

        createReqVO.setPersonCharge(dppEtlNewNodeSaveReqVO.getPersonCharge());
        createReqVO.setContactNumber(dppEtlNewNodeSaveReqVO.getContactNumber());
        createReqVO.setCatCode(dppEtlNewNodeSaveReqVO.getCatCode());
        createReqVO.setType(dppEtlNewNodeSaveReqVO.getType());
        createReqVO.setScheduler(dppEtlNewNodeSaveReqVO.getScheduler());
        createReqVO.setActuator(dppEtlNewNodeSaveReqVO.getActuator());

        // Implementation details.
        createReqVO.setTimeout(dppEtlNewNodeSaveReqVO.getTimeout());


        // Return the operation result.
        return createReqVO;
    }

    public static DppEtlTaskLogSaveReqVO fromDppEtlTaskLogSaveReqVO(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO, ProcessDefinition processDefinition) {
        // Create the required record.
        DppEtlTaskLogSaveReqVO createReqVO = new DppEtlTaskLogSaveReqVO();
        ProcessDefinitionLog data = processDefinition.getProcessDefinitionLog();
        // Handle task-related data and operations.
        createReqVO.setType(dppEtlNewNodeSaveReqVO.getType());
        createReqVO.setName(data.getName()); // Handle task-related data and operations.
        createReqVO.setCode(String.valueOf(data.getCode())); // Task code
        createReqVO.setVersion(data.getVersion()); // Implementation details.
        createReqVO.setProjectId(dppEtlNewNodeSaveReqVO.getProjectId()); // Implementation details.
        createReqVO.setProjectCode(String.valueOf(data.getProjectCode())); // Implementation details.
        createReqVO.setDescription(dppEtlNewNodeSaveReqVO.getDescription()); // Implementation details.
        createReqVO.setLocations(data.getLocations()); // Handle node-related data and operations.
        createReqVO.setDsId(data.getId()); // Handle DolphinScheduler operations.

        // Implementation details.
        if ("online".equalsIgnoreCase(data.getReleaseState())) {
            createReqVO.setStatus("1"); // Implementation details.
        } else if ("offline".equalsIgnoreCase(data.getReleaseState())) {
            createReqVO.setStatus("0"); // Implementation details.
        } else {
            createReqVO.setStatus("0"); // Implementation details.
        }
        createReqVO.setRemark(""); // Implementation details.

        createReqVO.setExecutionType(data.getExecutionType());// Implementation details.
        // Create the required record.
        createReqVO.setCreatorId(dppEtlNewNodeSaveReqVO.getCreatorId()); // Create the required record.
        createReqVO.setCreateBy(dppEtlNewNodeSaveReqVO.getCreateBy()); // Handle task-related data and operations.
        createReqVO.setCreateTime(dppEtlNewNodeSaveReqVO.getCreateTime()); // Create the required record.
        createReqVO.setUpdatorId(dppEtlNewNodeSaveReqVO.getUpdatorId()); // Update the related record.
        createReqVO.setUpdateBy(dppEtlNewNodeSaveReqVO.getUpdateBy()); // Handle task-related data and operations.
        createReqVO.setUpdateTime(dppEtlNewNodeSaveReqVO.getUpdateTime()); // Update the related record.

        // Implementation details.
        createReqVO.setPersonCharge(dppEtlNewNodeSaveReqVO.getPersonCharge());
        createReqVO.setType(dppEtlNewNodeSaveReqVO.getType());
        createReqVO.setTimeout(dppEtlNewNodeSaveReqVO.getTimeout());


        // Return the operation result.
        return createReqVO;
    }

    /**
     * Implementation details.
     *
     * @param dppEtlNewNodeSaveReqVO parameter value
     * @param task parameter value
     * @return the operation result
     */
    public static DppEtlTaskLogSaveReqVO fromDppEtlTaskLogSaveReqVO(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO, DppEtlTaskSaveReqVO task) {
        // Create the required record.
        DppEtlTaskLogSaveReqVO createReqVO = new DppEtlTaskLogSaveReqVO();
        // Handle task-related data and operations.
        createReqVO.setType(dppEtlNewNodeSaveReqVO.getType());
        createReqVO.setName(task.getName()); // Handle task-related data and operations.
        createReqVO.setCode(task.getCode()); // Task code
        createReqVO.setVersion(task.getVersion()); // Implementation details.
        createReqVO.setProjectId(dppEtlNewNodeSaveReqVO.getProjectId()); // Implementation details.
        createReqVO.setProjectCode(task.getProjectCode()); // Implementation details.
        createReqVO.setDescription(dppEtlNewNodeSaveReqVO.getDescription()); // Implementation details.
        createReqVO.setLocations(task.getLocations()); // Handle node-related data and operations.
        createReqVO.setDsId(task.getId()); // Handle DolphinScheduler operations.

        // Implementation details.
        if ("online".equalsIgnoreCase(task.getStatus())) {
            createReqVO.setStatus("1"); // Implementation details.
        } else if ("offline".equalsIgnoreCase(task.getStatus())) {
            createReqVO.setStatus("0"); // Implementation details.
        } else {
            createReqVO.setStatus("0"); // Implementation details.
        }
        createReqVO.setRemark(""); // Implementation details.

        createReqVO.setExecutionType(task.getExecutionType());// Implementation details.
        // Create the required record.
        createReqVO.setCreatorId(dppEtlNewNodeSaveReqVO.getCreatorId()); // Create the required record.
        createReqVO.setCreateBy(dppEtlNewNodeSaveReqVO.getCreateBy()); // Handle task-related data and operations.
        createReqVO.setCreateTime(dppEtlNewNodeSaveReqVO.getCreateTime()); // Create the required record.
        createReqVO.setUpdatorId(dppEtlNewNodeSaveReqVO.getUpdatorId()); // Update the related record.
        createReqVO.setUpdateBy(dppEtlNewNodeSaveReqVO.getUpdateBy()); // Handle task-related data and operations.
        createReqVO.setUpdateTime(dppEtlNewNodeSaveReqVO.getUpdateTime()); // Update the related record.

        // Implementation details.
        createReqVO.setPersonCharge(dppEtlNewNodeSaveReqVO.getPersonCharge());
        createReqVO.setType(dppEtlNewNodeSaveReqVO.getType());
        createReqVO.setTimeout(dppEtlNewNodeSaveReqVO.getTimeout());


        // Return the operation result.
        return createReqVO;
    }

    public static DppEtlTaskLogSaveReqVO fromDppEtlTaskSaveReqVO(DppEtlTaskSaveReqVO dppEtlTaskSaveReqVO) {
        DppEtlTaskLogSaveReqVO logSaveReqVO = new DppEtlTaskLogSaveReqVO();

        // Implementation details.
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


        // Create the required record.
        logSaveReqVO.setCreatorId(dppEtlTaskSaveReqVO.getProjectId()); // Create the required record.
        logSaveReqVO.setCreateBy(dppEtlTaskSaveReqVO.getName()); // Handle task-related data and operations.
        logSaveReqVO.setCreateTime(dppEtlTaskSaveReqVO.getCreateTime()); // Create the required record.
        logSaveReqVO.setUpdatorId(dppEtlTaskSaveReqVO.getProjectId()); // Update the related record.
        logSaveReqVO.setUpdateBy(dppEtlTaskSaveReqVO.getName()); // Handle task-related data and operations.
        logSaveReqVO.setUpdateTime(dppEtlTaskSaveReqVO.getUpdateTime()); // Update the related record.

        return logSaveReqVO;
    }


    public static List<DppEtlNodeSaveReqVO> convertToDppEtlNodeSaveReqVOList(ProcessDefinition processDefinition, DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO) {
        List<DppEtlNodeSaveReqVO> resultList = new ArrayList<>();

        // Implementation details.
        List<Map<String, Object>> list = JSONUtils.convertTaskDefinitionJson(dppEtlNewNodeSaveReqVO.getTaskDefinitionList());

        // Implementation details.
        for (TaskDefinition taskDefinition : processDefinition.getTaskDefinitionList()) {
            // Handle node-related data and operations.
            Map<String, Object> taskDefinitionMap = list.stream().filter(item -> {
                String code = MapUtils.getString(item, "code", "");
                return StringUtils.equals(taskDefinition.getCode(), code);
            }).findFirst().get();
            DppEtlNodeSaveReqVO createReqVO = new DppEtlNodeSaveReqVO();
            // Handle task-related data and operations.
            createReqVO.setTaskType(dppEtlNewNodeSaveReqVO.getType());// Handle task-related data and operations.
            createReqVO.setType(taskDefinition.getTaskType()); // Handle node-related data and operations.
            createReqVO.setComponentType(String.valueOf(taskDefinitionMap.get("componentType")));// Implementation details.
            createReqVO.setName(taskDefinition.getName()); // Handle task-related data and operations.
            createReqVO.setCode(String.valueOf(taskDefinition.getCode())); // Task code
            createReqVO.setVersion(taskDefinition.getVersion()); // Task version
            createReqVO.setProjectId(dppEtlNewNodeSaveReqVO.getProjectId()); // Implementation details.
            createReqVO.setProjectCode(String.valueOf(taskDefinition.getProjectCode())); // Implementation details.

            createReqVO.setPriority(String.valueOf(taskDefinition.getTaskPriority()));// Handle task-related data and operations.
            createReqVO.setFailRetryTimes((long) taskDefinition.getFailRetryTimes());
            createReqVO.setFailRetryInterval((long) taskDefinition.getFailRetryInterval());
            createReqVO.setTimeout((long) taskDefinition.getTimeout());
            createReqVO.setDelayTime((long) taskDefinition.getDelayTime());
            createReqVO.setCpuQuota((long) taskDefinition.getCpuQuota());
            createReqVO.setMemoryMax((long) taskDefinition.getMemoryMax());
            createReqVO.setDescription(taskDefinition.getDescription());
            createReqVO.setDsId(taskDefinition.getId()); // Handle task-related data and operations.

            createReqVO.setParameters(getTaskParamsAsJson(list, String.valueOf(taskDefinition.getCode()))); // Handle node-related data and operations.

            // Create the required record.
            createReqVO.setCreatorId(dppEtlNewNodeSaveReqVO.getCreatorId()); // Create the required record.
            createReqVO.setCreateBy(dppEtlNewNodeSaveReqVO.getCreateBy()); // Handle task-related data and operations.
            createReqVO.setCreateTime(dppEtlNewNodeSaveReqVO.getCreateTime()); // Create the required record.
            createReqVO.setUpdatorId(dppEtlNewNodeSaveReqVO.getUpdatorId()); // Update the related record.
            createReqVO.setUpdateBy(dppEtlNewNodeSaveReqVO.getUpdateBy()); // Handle task-related data and operations.
            createReqVO.setUpdateTime(dppEtlNewNodeSaveReqVO.getUpdateTime()); // Update the related record.

            // Implementation details.
            resultList.add(createReqVO);
        }
        return resultList;
    }

    public static List<DppEtlNodeSaveReqVO> convertToDppEtlNodeSaveReqVOList(List<TaskDefinition> taskDefinitionList, DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO) {
        // Implementation details.
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
        // Implementation details.
        Optional<Map<String, Object>> matchingTaskParams = list.stream()
                .filter(task -> task != null && StringUtils.equals(code, MapUtils.getString(task, "code")))
                .map(task -> (Map<String, Object>) MapUtils.getObject(task, "taskParams"))
                .filter(taskParams -> taskParams != null)
                .findFirst();

        // Handle JSON data for this operation.
        return matchingTaskParams.map(taskParams -> JSONUtils.toJson(taskParams))  // Handle JSON data for this operation.
                .orElse(null);  // Return the operation result.
    }


    public static List<DppEtlNodeLogSaveReqVO> convertToDppEtlNodeLogSaveReqVOList(ProcessDefinition processDefinition, DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO) {
        List<DppEtlNodeLogSaveReqVO> resultList = new ArrayList<>();

        // Implementation details.
        List<Map<String, Object>> list = JSONUtils.convertTaskDefinitionJson(dppEtlNewNodeSaveReqVO.getTaskDefinitionList());

        // Implementation details.
        for (TaskDefinition taskDefinition : processDefinition.getTaskDefinitionList()) {
            // Handle node-related data and operations.
            Map<String, Object> taskDefinitionMap = list.stream().filter(item -> {
                String code = MapUtils.getString(item, "code", "");
                return StringUtils.equals(taskDefinition.getCode(), code);
            }).findFirst().get();
            DppEtlNodeLogSaveReqVO createReqVO = new DppEtlNodeLogSaveReqVO();

            // Handle task-related data and operations.
            createReqVO.setTaskType(dppEtlNewNodeSaveReqVO.getType());// Handle task-related data and operations.
            createReqVO.setType(taskDefinition.getTaskType()); // Handle node-related data and operations.
            createReqVO.setComponentType(String.valueOf(taskDefinitionMap.get("componentType")));// Implementation details.
            createReqVO.setName(taskDefinition.getName()); // Handle task-related data and operations.
            createReqVO.setCode(String.valueOf(taskDefinition.getCode())); // Task code
            createReqVO.setVersion((long) taskDefinition.getVersion()); // Task version
            createReqVO.setProjectId(dppEtlNewNodeSaveReqVO.getProjectId()); // Implementation details.
            createReqVO.setProjectCode(String.valueOf(taskDefinition.getProjectCode())); // Implementation details.

            createReqVO.setPriority(String.valueOf(taskDefinition.getTaskPriority()));// Handle task-related data and operations.
            createReqVO.setFailRetryTimes((long) taskDefinition.getFailRetryTimes());
            createReqVO.setFailRetryInterval((long) taskDefinition.getFailRetryInterval());
            createReqVO.setTimeout((long) taskDefinition.getTimeout());
            createReqVO.setDelayTime((long) taskDefinition.getDelayTime());
            createReqVO.setCpuQuota((long) taskDefinition.getCpuQuota());
            createReqVO.setMemoryMax((long) taskDefinition.getMemoryMax());
            createReqVO.setDescription(taskDefinition.getDescription());
            createReqVO.setDsId(taskDefinition.getId()); // Handle task-related data and operations.

            createReqVO.setParameters(getTaskParamsAsJson(list, String.valueOf(taskDefinition.getCode()))); // Handle node-related data and operations.

            // Create the required record.
            createReqVO.setCreatorId(dppEtlNewNodeSaveReqVO.getCreatorId()); // Create the required record.
            createReqVO.setCreateBy(dppEtlNewNodeSaveReqVO.getCreateBy()); // Handle task-related data and operations.
            createReqVO.setCreateTime(dppEtlNewNodeSaveReqVO.getCreateTime()); // Create the required record.
            createReqVO.setUpdatorId(dppEtlNewNodeSaveReqVO.getUpdatorId()); // Update the related record.
            createReqVO.setUpdateBy(dppEtlNewNodeSaveReqVO.getUpdateBy()); // Handle task-related data and operations.
            createReqVO.setUpdateTime(dppEtlNewNodeSaveReqVO.getUpdateTime()); // Update the related record.

            // Implementation details.
            resultList.add(createReqVO);
        }
        return resultList;
    }

    public static long getIdByCode(List<DppEtlNodeDO> dppEtlNodeDOList, String code, long preTaskVersion) {
        return dppEtlNodeDOList.stream()
                .filter(task -> StringUtils.equals(task.getCode(), code) && task.getVersion() == preTaskVersion)  // Implementation details.
                .map(DppEtlNodeDO::getId)  // Retrieve the required data.
                .findFirst()  // Return the operation result.
                .orElse(-1L);  // Return the operation result.
    }

    public static List<DppEtlTaskNodeRelSaveReqVO> convertToDppEtlTaskNodeRelSaveReqVOList(List<ProcessTaskRelation> taskRelationList, DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO, List<DppEtlNodeDO> dppEtlNodeBatch, DppEtlTaskSaveReqVO dppEtlTaskSaveReqVO, String code, Integer version) {
        List<DppEtlTaskNodeRelSaveReqVO> resultList = new ArrayList<>();

        // Implementation details.
        for (ProcessTaskRelation taskRelation : taskRelationList) {
            DppEtlTaskNodeRelSaveReqVO taskNodeRelSaveReqVO = new DppEtlTaskNodeRelSaveReqVO();

            // Handle task-related data and operations.
            taskNodeRelSaveReqVO.setProjectId(dppEtlNewNodeSaveReqVO.getProjectId()); // Implementation details.
            taskNodeRelSaveReqVO.setProjectCode(String.valueOf(dppEtlNewNodeSaveReqVO.getProjectCode())); // Implementation details.

            // Handle task-related data and operations.
            taskNodeRelSaveReqVO.setTaskId(dppEtlTaskSaveReqVO.getId()); // Task ID
            taskNodeRelSaveReqVO.setTaskCode(code); // Task code
            taskNodeRelSaveReqVO.setTaskVersion(version); // Task version

            // Handle node-related data and operations.
            taskNodeRelSaveReqVO.setPreNodeId(getIdByCode(dppEtlNodeBatch, String.valueOf(taskRelation.getPreTaskCode()), taskRelation.getPreTaskVersion())); // Handle node-related data and operations.
            taskNodeRelSaveReqVO.setPreNodeCode(String.valueOf(taskRelation.getPreTaskCode())); // Handle node-related data and operations.
            taskNodeRelSaveReqVO.setPreNodeVersion(taskRelation.getPreTaskVersion()); // Handle node-related data and operations.

            // Handle node-related data and operations.
            taskNodeRelSaveReqVO.setPostNodeId(getIdByCode(dppEtlNodeBatch, code, taskRelation.getPreTaskVersion())); // Handle node-related data and operations.
            taskNodeRelSaveReqVO.setPostNodeCode(String.valueOf(taskRelation.getPostTaskCode())); // Handle node-related data and operations.
            taskNodeRelSaveReqVO.setPostNodeVersion(taskRelation.getPostTaskVersion()); // Handle node-related data and operations.

            // Implementation details.
            taskNodeRelSaveReqVO.setRemark(null); // Implementation details.

            // Update the related record.
            taskNodeRelSaveReqVO.setCreatorId(dppEtlNewNodeSaveReqVO.getCreatorId()); // Create the required record.
            taskNodeRelSaveReqVO.setCreateBy(dppEtlNewNodeSaveReqVO.getCreateBy()); // Handle task-related data and operations.
            taskNodeRelSaveReqVO.setCreateTime(dppEtlNewNodeSaveReqVO.getCreateTime()); // Create the required record.
            taskNodeRelSaveReqVO.setUpdatorId(dppEtlNewNodeSaveReqVO.getUpdatorId()); // Update the related record.
            taskNodeRelSaveReqVO.setUpdateBy(dppEtlNewNodeSaveReqVO.getUpdateBy()); // Handle task-related data and operations.
            taskNodeRelSaveReqVO.setUpdateTime(dppEtlNewNodeSaveReqVO.getUpdateTime()); // Update the related record.

            // Implementation details.
            resultList.add(taskNodeRelSaveReqVO);
        }

        return resultList;
    }

    public static List<DppEtlTaskNodeRelLogSaveReqVO> convertToDppEtlTaskNodeRelLogSaveReqVOList(List<ProcessTaskRelation> taskRelationList, DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO, List<DppEtlNodeLogDO> dppEtlNodeBatch, DppEtlTaskLogSaveReqVO dppEtlTaskSaveReqVO, String code, Integer version) {
        List<DppEtlTaskNodeRelLogSaveReqVO> resultList = new ArrayList<>();

        // Implementation details.
        for (ProcessTaskRelation taskRelation : taskRelationList) {
            DppEtlTaskNodeRelLogSaveReqVO taskNodeRelSaveReqVO = new DppEtlTaskNodeRelLogSaveReqVO();

            // Handle task-related data and operations.
            taskNodeRelSaveReqVO.setProjectId(dppEtlNewNodeSaveReqVO.getProjectId()); // Implementation details.
            taskNodeRelSaveReqVO.setProjectCode(String.valueOf(dppEtlNewNodeSaveReqVO.getProjectCode())); // Implementation details.

            // Handle task-related data and operations.
            taskNodeRelSaveReqVO.setTaskId(dppEtlTaskSaveReqVO.getId()); // Task ID
            taskNodeRelSaveReqVO.setTaskCode(code); // Task code
            taskNodeRelSaveReqVO.setTaskVersion(version); // Task version

            // Handle node-related data and operations.
            taskNodeRelSaveReqVO.setPreNodeId(getDppEtlNodeLogDOIdByCode(dppEtlNodeBatch, String.valueOf(taskRelation.getPreTaskCode()), taskRelation.getPreTaskVersion())); // Handle node-related data and operations.
            taskNodeRelSaveReqVO.setPreNodeCode(String.valueOf(taskRelation.getPreTaskCode())); // Handle node-related data and operations.
            taskNodeRelSaveReqVO.setPreNodeVersion(taskRelation.getPreTaskVersion()); // Handle node-related data and operations.

            // Handle node-related data and operations.
            taskNodeRelSaveReqVO.setPostNodeId(getDppEtlNodeLogDOIdByCode(dppEtlNodeBatch, code, taskRelation.getPreTaskVersion())); // Handle node-related data and operations.
            taskNodeRelSaveReqVO.setPostNodeCode(String.valueOf(taskRelation.getPostTaskCode())); // Handle node-related data and operations.
            taskNodeRelSaveReqVO.setPostNodeVersion(taskRelation.getPostTaskVersion()); // Handle node-related data and operations.

            // Implementation details.
            taskNodeRelSaveReqVO.setRemark(null); // Implementation details.

            // Update the related record.
            taskNodeRelSaveReqVO.setCreatorId(dppEtlNewNodeSaveReqVO.getCreatorId()); // Create the required record.
            taskNodeRelSaveReqVO.setCreateBy(dppEtlNewNodeSaveReqVO.getCreateBy()); // Handle task-related data and operations.
            taskNodeRelSaveReqVO.setCreateTime(dppEtlNewNodeSaveReqVO.getCreateTime()); // Create the required record.
            taskNodeRelSaveReqVO.setUpdatorId(dppEtlNewNodeSaveReqVO.getUpdatorId()); // Update the related record.
            taskNodeRelSaveReqVO.setUpdateBy(dppEtlNewNodeSaveReqVO.getUpdateBy()); // Handle task-related data and operations.
            taskNodeRelSaveReqVO.setUpdateTime(dppEtlNewNodeSaveReqVO.getUpdateTime()); // Update the related record.

            // Implementation details.
            resultList.add(taskNodeRelSaveReqVO);
        }

        return resultList;
    }


    public static long getDppEtlNodeLogDOIdByCode(List<DppEtlNodeLogDO> dppEtlNodeDOList, String code, long preTaskVersion) {
        return dppEtlNodeDOList.stream()
                .filter(task -> StringUtils.equals(task.getCode(), code) && task.getVersion() == preTaskVersion)  // Implementation details.
                .map(DppEtlNodeLogDO::getId)  // Retrieve the required data.
                .findFirst()  // Return the operation result.
                .orElse(-1L);  // Return the operation result.
    }


    /**
     * Implementation details.
     *
     * @param crontab parameter value
     * @param processDefinitionCode parameter value
     * @return DsSchedulerSaveReqDTO
     */
    public static DsSchedulerSaveReqDTO createSchedulerRequest(String crontab, String processDefinitionCode) {
        // Retrieve the required data.
        String startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        // Retrieve the required data.
        long currentTime = System.currentTimeMillis();
        String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(currentTime + 100L * 365 * 24 * 60 * 60 * 1000));

        // Create the required record.
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
     * Implementation details.
     *
     * @param dsSchedulerRespDTO DsSchedulerRespDTO
     * @param dppEtlTaskDO
     * @return DppEtlSchedulerSaveReqVO
     */
    public static DppEtlSchedulerSaveReqVO convertToDppEtlSchedulerSaveReqVO(DsSchedulerRespDTO dsSchedulerRespDTO, DppEtlTaskDO dppEtlTaskDO) {
        // Create the required record.
        DppEtlSchedulerSaveReqVO reqVO = new DppEtlSchedulerSaveReqVO();

        // Implementation details.
        Schedule schedule = dsSchedulerRespDTO.getData();

        reqVO.setStartTime(schedule.getStartTime());
        reqVO.setEndTime(schedule.getEndTime());
        reqVO.setTimezoneId(schedule.getTimezoneId());
        reqVO.setCronExpression(schedule.getCrontab());
        reqVO.setFailureStrategy("1");

        // Implementation details.
        reqVO.setDsId(schedule.getId()); // Implementation details.
        reqVO.setRemark(null); // Update the related record.

        return reqVO;
    }


    /**
     * Implementation details.
     *
     * @param id parameter value
     * @param crontab parameter value
     * @param processDefinitionCode parameter value
     * @return DsSchedulerUpdateReqDTO
     */
    public static DsSchedulerUpdateReqDTO createSchedulerUpdateRequest(Long id, String crontab, String processDefinitionCode) {
        // Retrieve the required data.
        String startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        // Retrieve the required data.
        long currentTime = System.currentTimeMillis();
        String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(currentTime + 100L * 365 * 24 * 60 * 60 * 1000));

        // Create the required record.
        DsSchedulerUpdateReqDTO dto = new DsSchedulerUpdateReqDTO();
        dto.setId(id); // Handle scheduling configuration and operations.
        dto.setSchedule(String.format("{\"startTime\":\"%s\",\"endTime\":\"%s\",\"crontab\":\"%s\",\"timezoneId\":\"Asia/Shanghai\"}",
                startTime, endTime, crontab));
        dto.setProcessDefinitionCode(processDefinitionCode);
        dto.setFailureStrategy("CONTINUE");
        dto.setWorkerGroup("default");
        dto.setTenantCode("default");

        return dto;
    }


    /**
     * Implementation details.
     *
     * @param dppEtlNewNodeSaveReqVO
     * @return DppEtlSchedulerSaveReqVO
     */
    public static DppEtlSchedulerSaveReqVO convertToDppEtlSchedulerSaveReqVO(Long taskId, String taskCode, DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO) {
        // Create the required record.
        DppEtlSchedulerSaveReqVO reqVO = new DppEtlSchedulerSaveReqVO();

        // Implementation details.
        reqVO.setTaskId(taskId);
        reqVO.setTaskCode(taskCode);

        // Retrieve the required data.
        long currentTime = System.currentTimeMillis();
        Date date = new Date(currentTime + 100L * 365 * 24 * 60 * 60 * 1000);

        reqVO.setStartTime(new Date());
        reqVO.setEndTime(date);
        reqVO.setTimezoneId("Asia/Shanghai"); // Implementation details.

        reqVO.setCronExpression(dppEtlNewNodeSaveReqVO.getCrontab());
        reqVO.setFailureStrategy("1");
        reqVO.setStatus("0");
        reqVO.setTaskScheduler(dppEtlNewNodeSaveReqVO.getScheduler());
        reqVO.setTaskActuator(dppEtlNewNodeSaveReqVO.getActuator());

        // Implementation details.
        reqVO.setDsId((long) -1);

        // Implementation details.

        return reqVO;
    }

    // Implementation details.
    private static Date parseStartTime(String scheduleJson) {
        // Handle JSON data for this operation.
        return new Date(); // Implementation details.
    }

    private static Date parseEndTime(String scheduleJson) {
        // Handle JSON data for this operation.
        return new Date(); // Implementation details.
    }

    public static List<String> getPreAndPostNodeCodeList(List<DppEtlTaskNodeRelRespVO> dppEtlTaskNodeRelRespVOList) {
        List<String> result = new ArrayList<>();
        for (DppEtlTaskNodeRelRespVO vo : dppEtlTaskNodeRelRespVOList) {
            result.add(vo.getPreNodeCode());  // Implementation details.
            result.add(vo.getPostNodeCode()); // Implementation details.
        }
        return result;  // Return the operation result.
    }


    public static DsStartTaskReqDTO createDsStartTaskReqDTO(String processDefinitionCode) {
        // Retrieve the required data.
        String currentDate = DateUtil.today();
        // Implementation details.
        String scheduleTime = String.format("{\"complementStartDate\":\"%s 00:00:00\",\"complementEndDate\":\"%s 00:00:00\"}", currentDate, currentDate);

        // Create the required record.
        return DsStartTaskReqDTO.builder()
                .processDefinitionCode(JSONUtils.convertToLong(processDefinitionCode))
                .failureStrategy("CONTINUE")
                .warningType(DEFAULT_CONDITION_TYPE)
                .processInstancePriority(DEFAULT_TASK_PRIORITY)
                .scheduleTime(scheduleTime)
                .build();
    }


    /**
     * Implementation details.
     *
     * @param locations
     * @param code
     * @return
     */
    public static String buildEtlTaskLocationsJson(List<Map<String, Object>> locations, String code) {
        List<Map<String, Object>> locationList = new ArrayList<>();

        Map<String, Object> location = locations.get(0);
        Map<String, Object> locationMap = new HashMap<>();
        // Implementation details.
        locationMap.put("taskCode", Long.parseLong(code)); // Implementation details.
        locationMap.put("x", location.getOrDefault("x", 0)); // Implementation details.
        locationMap.put("y", location.getOrDefault("y", 0)); // Implementation details.
        locationList.add(locationMap);
        return JSON.toJSONString(locationList);
    }

    /**
     * Handle node-related data and operations.
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
     * Handle node-related data and operations.
     *
     * @param id parameter value
     * @param name parameter value
     * @param code parameter value
     * @param version parameter value
     * @param mainArgs parameter value
     * @param draftJson
     * @return
     */
    public static String buildEtlTaskDefinitionJson(Long id, String name, String code, Integer version, Map<String, Object> mainArgs, String draftJson) {
        List<Map<String, Object>> result = new ArrayList<>();
        // Implementation details.
        Map<String, Object> definitionJsonMap = JSONUtils.convertTaskDefinitionJsonMap(draftJson);

        // Implementation details.
        Map<String, Object> taskMap = new HashMap<>();

        // Implementation details.
        taskMap.put("id", id); // Implementation details.
        taskMap.put("name", name); // Implementation details.
        taskMap.put("code", code); // Implementation details.
        taskMap.put("version", version); // Implementation details.
        taskMap.put("description", ""); // Implementation details.
        taskMap.put("workerGroup", MapUtils.getObject(definitionJsonMap,"workerGroup",DEFAULT_WORKER_GROUP) ); // Implementation details.
        taskMap.put("environmentCode", DEFAULT_ENVIRONMENT_CODE); // Implementation details.
        taskMap.put("flag", DEFAULT_FLAG); // Implementation details.
        taskMap.put("isCache", DEFAULT_IS_CACHE); // Implementation details.
        taskMap.put("taskPriority", MapUtils.getObject(definitionJsonMap,"taskPriority",DEFAULT_TASK_PRIORITY)); // Handle task-related data and operations.
        taskMap.put("taskType", DEFAULT_TASK_TYPE); // Handle task-related data and operations.
        taskMap.put("taskExecuteType", "BATCH");

        // Implementation details.
        taskMap.put("failRetryTimes", MapUtils.getObject(definitionJsonMap,"failRetryTimes",DEFAULT_TASK_failRetryTimes));
        taskMap.put("delayTime", MapUtils.getObject(definitionJsonMap,"delayTime",DEFAULT_TASK_delayTime));
        taskMap.put("failRetryInterval", MapUtils.getObject(definitionJsonMap,"failRetryInterval",DEFAULT_TASK_failRetryInterval));

        Map<String, Object> taskParams = new LinkedHashMap<>();

        taskParams.put("localParams", new ArrayList<>()); // Implementation details.
        taskParams.put("rawScript", ""); // Implementation details.
        taskParams.put("resourceList", new ArrayList<>()); // Implementation details.
        taskParams.put("programType", DEFAULT_PROGRAM_TYPE); // Implementation details.
        taskParams.put("mainClass", defaultMainClass);

        // Implementation details.
        Map<String, Object> mainJar = new HashMap<>();
        mainJar.put("resourceName", resourceName);
        taskParams.put("mainJar", mainJar);
        taskParams.put("deployMode", DEFAULT_DEPLOY_MODE); // Implementation details.
        taskParams.put("mainArgs", Base64.encode(JSON.toJSONString(mainArgs))); // Implementation details.
        taskParams.put("master", defaultMaster); // Implementation details.
        taskParams.put("driverCores",MapUtils.getObject(definitionJsonMap,"driverCores",DEFAULT_DRIVER_CORES) ); // Implementation details.
        taskParams.put("driverMemory",MapUtils.getObject(definitionJsonMap,"driverMemory",DEFAULT_DRIVER_MEMORY) ); // Implementation details.
        taskParams.put("numExecutors", MapUtils.getObject(definitionJsonMap,"numExecutors",DEFAULT_NUM_EXECUTORS)); // Implementation details.
        taskParams.put("executorMemory",MapUtils.getObject(definitionJsonMap,"executorMemory",DEFAULT_EXECUTOR_MEMORY) ); // Implementation details.
        taskParams.put("executorCores",MapUtils.getObject(definitionJsonMap,"executorCores",DEFAULT_EXECUTOR_CORES) ); // Implementation details.
        taskParams.put("yarnQueue",MapUtils.getObject(definitionJsonMap,"yarnQueue","") ); // Implementation details.
        taskParams.put("sqlExecutionType", DEFAULT_SQL_EXECUTION_TYPE); // Handle JDBC SQL execution.

        // Handle task-related data and operations.
        taskMap.put("taskParams", taskParams);

        // Handle task-related data and operations.
        result.add(taskMap);
        // Handle JSON data for this operation.
        return JSON.toJSONString(result);
    }

    public static List<DppEtlNodeSaveReqVO> convertToDppEtlNodeSaveReqVOList(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO, String taskDefinitionJson) {
        List<DppEtlNodeSaveReqVO> resultList = new ArrayList<>();

        // Implementation details.
        List<DppEtlNodeSaveReqVO> list = JSON.parseArray(taskDefinitionJson, DppEtlNodeSaveReqVO.class);

        // Implementation details.
        for (DppEtlNodeSaveReqVO createReqVO : list) {
            // Handle task-related data and operations.
            createReqVO.setType(createReqVO.getTaskType());// Handle node-related data and operations.
            createReqVO.setTaskType(dppEtlNewNodeSaveReqVO.getType());// Handle task-related data and operations.
            createReqVO.setVersion(1); // Task version
            createReqVO.setProjectId(dppEtlNewNodeSaveReqVO.getProjectId()); // Implementation details.
            createReqVO.setProjectCode(String.valueOf(dppEtlNewNodeSaveReqVO.getProjectCode())); // Implementation details.
            // Create the required record.
            createReqVO.setCreatorId(dppEtlNewNodeSaveReqVO.getCreatorId()); // Create the required record.
            createReqVO.setCreateBy(dppEtlNewNodeSaveReqVO.getCreateBy()); // Handle task-related data and operations.
            createReqVO.setCreateTime(dppEtlNewNodeSaveReqVO.getCreateTime()); // Create the required record.
            createReqVO.setUpdatorId(dppEtlNewNodeSaveReqVO.getUpdatorId()); // Update the related record.
            createReqVO.setUpdateBy(dppEtlNewNodeSaveReqVO.getUpdateBy()); // Handle task-related data and operations.
            createReqVO.setUpdateTime(dppEtlNewNodeSaveReqVO.getUpdateTime()); // Update the related record.
            createReqVO.setParameters(JSON.toJSONString(createReqVO.getTaskParams()));
            // Implementation details.
            resultList.add(createReqVO);
        }
        return resultList;
    }

    public static List<DppEtlNodeSaveReqVO> convertToDppEtlNodeSaveReqVOList(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO, Integer nodeVersion) {
        List<DppEtlNodeSaveReqVO> resultList = new ArrayList<>();
        // Implementation details.
        List<DppEtlNodeSaveReqVO> list = JSON.parseArray(dppEtlNewNodeSaveReqVO.getTaskDefinitionList(), DppEtlNodeSaveReqVO.class);

        // Implementation details.
        for (DppEtlNodeSaveReqVO createReqVO : list) {
            // Handle task-related data and operations.
            createReqVO.setType(createReqVO.getTaskType());// Handle node-related data and operations.
            createReqVO.setTaskType(dppEtlNewNodeSaveReqVO.getType());// Handle task-related data and operations.
            createReqVO.setProjectId(dppEtlNewNodeSaveReqVO.getProjectId()); // Implementation details.
            createReqVO.setProjectCode(String.valueOf(dppEtlNewNodeSaveReqVO.getProjectCode())); // Implementation details.
            // Create the required record.
            createReqVO.setCreatorId(dppEtlNewNodeSaveReqVO.getCreatorId()); // Create the required record.
            createReqVO.setCreateBy(dppEtlNewNodeSaveReqVO.getCreateBy()); // Handle task-related data and operations.
            createReqVO.setCreateTime(dppEtlNewNodeSaveReqVO.getCreateTime()); // Create the required record.
            createReqVO.setUpdatorId(dppEtlNewNodeSaveReqVO.getUpdatorId()); // Update the related record.
            createReqVO.setUpdateBy(dppEtlNewNodeSaveReqVO.getUpdateBy()); // Handle task-related data and operations.
            createReqVO.setUpdateTime(dppEtlNewNodeSaveReqVO.getUpdateTime()); // Update the related record.
            createReqVO.setParameters(JSON.toJSONString(createReqVO.getTaskParams()));
            // Implementation details.
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
        // Implementation details.
        for (ProcessTaskRelation taskRelation : list) {

            DppEtlTaskNodeRelSaveReqVO taskNodeRelSaveReqVO = new DppEtlTaskNodeRelSaveReqVO();

            // Handle task-related data and operations.
            taskNodeRelSaveReqVO.setProjectId(dppEtlNewNodeSaveReqVO.getProjectId()); // Implementation details.
            taskNodeRelSaveReqVO.setProjectCode(String.valueOf(dppEtlNewNodeSaveReqVO.getProjectCode())); // Implementation details.

            // Handle task-related data and operations.
            taskNodeRelSaveReqVO.setTaskId(dppEtlTaskSaveReqVO.getId()); // Task ID
            taskNodeRelSaveReqVO.setTaskCode(dppEtlTaskSaveReqVO.getCode()); // Task code
            taskNodeRelSaveReqVO.setTaskVersion(dppEtlTaskSaveReqVO.getVersion()); // Task version

            // Handle node-related data and operations.
            taskNodeRelSaveReqVO.setPreNodeCode(String.valueOf(taskRelation.getPreTaskCode())); // Handle node-related data and operations.
            taskNodeRelSaveReqVO.setPreNodeVersion(taskRelation.getPreTaskVersion()); // Handle node-related data and operations.
            if (StringUtils.isNotEmpty(taskNodeRelSaveReqVO.getPreNodeCode()) && taskNodeRelSaveReqVO.getPreNodeVersion() == 0) {
                taskNodeRelSaveReqVO.setPreNodeVersion(1);
            }
            taskNodeRelSaveReqVO.setPreNodeId(getIdByCode(dppEtlNodeBatch, taskNodeRelSaveReqVO.getPreNodeCode(), taskNodeRelSaveReqVO.getPreNodeVersion())); // Handle node-related data and operations.

            // Handle node-related data and operations.
            taskNodeRelSaveReqVO.setPostNodeCode(String.valueOf(taskRelation.getPostTaskCode())); // Handle node-related data and operations.
            taskNodeRelSaveReqVO.setPostNodeVersion(taskRelation.getPostTaskVersion()); // Handle node-related data and operations.
            if (StringUtils.isNotEmpty(taskNodeRelSaveReqVO.getPostNodeCode()) && taskNodeRelSaveReqVO.getPostNodeVersion() == 0) {
                taskNodeRelSaveReqVO.setPostNodeVersion(1);
            }
            taskNodeRelSaveReqVO.setPostNodeId(getIdByCode(dppEtlNodeBatch, taskNodeRelSaveReqVO.getPostNodeCode(), taskNodeRelSaveReqVO.getPostNodeVersion())); // Handle node-related data and operations.

            // Implementation details.
            taskNodeRelSaveReqVO.setRemark(null); // Implementation details.

            // Update the related record.
            taskNodeRelSaveReqVO.setCreatorId(dppEtlNewNodeSaveReqVO.getCreatorId()); // Create the required record.
            taskNodeRelSaveReqVO.setCreateBy(dppEtlNewNodeSaveReqVO.getCreateBy()); // Handle task-related data and operations.
            taskNodeRelSaveReqVO.setCreateTime(dppEtlNewNodeSaveReqVO.getCreateTime()); // Create the required record.
            taskNodeRelSaveReqVO.setUpdatorId(dppEtlNewNodeSaveReqVO.getUpdatorId()); // Update the related record.
            taskNodeRelSaveReqVO.setUpdateBy(dppEtlNewNodeSaveReqVO.getUpdateBy()); // Handle task-related data and operations.
            taskNodeRelSaveReqVO.setUpdateTime(dppEtlNewNodeSaveReqVO.getUpdateTime()); // Update the related record.

            // Implementation details.
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
     * Implementation details.
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
            // Implementation details.
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
        // Implementation details.
        Map<String, Object> config = new HashMap<>();
        config.put("taskInfo", taskInfo);
        // Handle Redis state for this operation.
        config.put("redis", dsRedisConfig);
        config.put("rabbitmq", rabbitmqConfig);
        config.put("resourceUrl", resourceUrl);
        result.put("transition", transitionList);
        result.put("config", config);
        return result;
    }
}
