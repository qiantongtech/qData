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

import com.alibaba.fastjson2.JSON;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tech.qiantong.qdata.api.ds.api.etl.DsSchedulerSaveReqDTO;
import tech.qiantong.qdata.api.ds.api.etl.DsSchedulerUpdateReqDTO;
import tech.qiantong.qdata.api.ds.api.etl.DsStartTaskReqDTO;
import tech.qiantong.qdata.api.ds.api.etl.DsTaskSaveReqDTO;
import tech.qiantong.qdata.api.ds.api.etl.ds.ProcessDefinition;
import tech.qiantong.qdata.api.ds.api.etl.ds.TaskDefinition;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.JSONUtils;
import tech.qiantong.qdata.module.dpp.utils.model.TaskSaveReqInput;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class DppTaskConverter {


    private static String defaultURL;

    @Value("${ds.quality_url}")
    private void setDefaultURL(String defaultURL) {
        this.defaultURL = defaultURL;
    }


    private static final String DEFAULT_CONDITION_TYPE = "NONE"; // Default condition type is "NONE"
    private static final String DEFAULT_FLAG = "YES"; // Default flag, indicates node is enabled


    // Default configuration constants
    private static final long DEFAULT_ENVIRONMENT_CODE = 133155949418208L; // Default environment code
    private static final String DEFAULT_WORKER_GROUP = "default"; // Default worker group
    private static final String DEFAULT_IS_CACHE = "NO"; // Default cache disabled
    private static final String DEFAULT_TASK_PRIORITY = "MEDIUM"; // Default task priority
    private static final String DEFAULT_TASK_TYPE = "HTTP"; // Default task type, HTTP
    private static final String HTTP_METHOD = "PUT";
    private static final String HTTP_BODY = "";
    private static final String HTTP_CHECK_CONDITION = "STATUS_CODE_DEFAULT";
    private static final String CONDITION = "";
    private static final int CONNECT_TIMEOUT = 60000;
    private static final int SOCKET_TIMEOUT = 60000;



    public static DsTaskSaveReqDTO buildDsTaskSaveReq(TaskSaveReqInput input) {
        // Create return entity
        DsTaskSaveReqDTO dsTaskSaveReqDTO = new DsTaskSaveReqDTO();

        // 1. Wrap basic parameters
        dsTaskSaveReqDTO.setName(input.getName());  // Task name
        dsTaskSaveReqDTO.setDescription("");  // Empty description
        dsTaskSaveReqDTO.setExecutionType("PARALLEL");  // Hardcoded execution type as "PARALLEL"

        // 2. Wrap node info, HTTP type
        String taskDefinition = buildTaskDefinitionForHttp(input);

        // 3. Build task relation
        String taskRelation = buildTaskRelationJson(input);  // Default relation type is "NONE"

        // 4. Build task location
        String location = buildTaskNodeLocations(input);

        // Set the task fields
        dsTaskSaveReqDTO.setTaskDefinitionJson(taskDefinition);
        dsTaskSaveReqDTO.setTaskRelationJson(taskRelation);
        dsTaskSaveReqDTO.setLocations(location);

        return dsTaskSaveReqDTO;
    }

    private static String buildTaskDefinitionForHttp(TaskSaveReqInput input) {
        // Set HTTP type task parameters
        Map<String, Object> taskMap = new HashMap<>();

        taskMap.put("id", input.getNodeId());  // Use the passed-in task ID
        taskMap.put("name", input.getName());  // Hardcoded task name
        taskMap.put("code", input.getNodeCode());  // Use the passed-in task code
        taskMap.put("version", 1);  // Hardcoded version
        taskMap.put("description", "");  // Empty description
        taskMap.put("workerGroup",DEFAULT_WORKER_GROUP);  // Hardcoded worker group
        taskMap.put("environmentCode", DEFAULT_ENVIRONMENT_CODE);  // Hardcoded environment code
        taskMap.put("flag",  DEFAULT_FLAG); // Default flag is "YES"
        taskMap.put("isCache", DEFAULT_IS_CACHE);  // No caching
        taskMap.put("taskPriority", DEFAULT_TASK_PRIORITY);  // Medium priority
        taskMap.put("taskType", DEFAULT_TASK_TYPE);  // HTTP type task
        taskMap.put("taskExecuteType", "BATCH");

        // Task parameters
        Map<String, Object> taskParams = new HashMap<>();
        taskParams.put("localParams", new ArrayList<>());  // Default empty list
        taskParams.put("resourceList", new ArrayList<>());  // Default empty list
        taskParams.put("httpMethod", HTTP_METHOD);  // Default HTTP method is PUT
        taskParams.put("httpBody", HTTP_BODY);  // Default HTTP body empty
        taskParams.put("httpCheckCondition", HTTP_CHECK_CONDITION);  // Default check condition
        taskParams.put("httpParams", input.getHttpParams());  // Use the passed-in httpParams
        taskParams.put("url", defaultURL + "/"+ String.valueOf(input.getId()));  // Default URL
        taskParams.put("condition", CONDITION);  // Default condition is empty
        taskParams.put("connectTimeout", CONNECT_TIMEOUT);  // Default connect timeout 60000ms
        taskParams.put("socketTimeout", SOCKET_TIMEOUT);  // Default socket timeout 60000ms

        // Add taskParams to taskMap
        taskMap.put("taskParams", taskParams);

        // Return JSON string
        return JSON.toJSONString(Collections.singletonList(taskMap));
    }

    private static String buildTaskRelationJson(TaskSaveReqInput input) {
        // Build task relation
        Map<String, Object> relationMap = new HashMap<>();
        relationMap.put("id", input.getNodeId());  // Hardcoded relation ID
        relationMap.put("preTaskCode", 0L);  // Pre-task code hardcoded to 0
        relationMap.put("preTaskVersion", 0);  // Pre-task version hardcoded to 0
        relationMap.put("postTaskCode", input.getNodeCode());  // Post-task code hardcoded
        relationMap.put("postTaskVersion", 1);  // Post-task version hardcoded
        relationMap.put("conditionType", DEFAULT_CONDITION_TYPE);  // Use the input conditionType

        // Return JSON string
        return JSON.toJSONString(Collections.singletonList(relationMap));
    }

    private static String buildTaskNodeLocations(TaskSaveReqInput input) {
        // Hardcoded task node location
        Map<String, Object> locationMap = new HashMap<>();
        locationMap.put("taskCode", input.getNodeCode());  // Task ID
        locationMap.put("x", 138.4886474609375);  // Hardcoded X coordinate
        locationMap.put("y", 184.9232940673828);  // Hardcoded Y coordinate

        // Return location JSON string
        return JSON.toJSONString(Collections.singletonList(locationMap));
    }

    public static Long stringToLong(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            // If conversion fails, return null or return a default value as needed
            System.err.println("Invalid number format: " + value);
            return null; // Or you can return a default value, such as 0L
        }
    }

    public static String longToString(Long value) {
        if (value == null) {
            return null; // Or return empty string "", adjust as needed
        }
        return value.toString();
    }
    public static TaskDefinition getFirstTaskDefinition(ProcessDefinition definition) {
        // Get task definition list
        List<TaskDefinition> taskDefinitionList = definition.getTaskDefinitionList();

        // Check if the list is empty to prevent IndexOutOfBoundsException
        if (taskDefinitionList != null && !taskDefinitionList.isEmpty()) {
            // Return the first task definition
            return taskDefinitionList.get(0);
        }

        // If the list is empty, return null or other default value as needed
        throw new ServiceException("dpp.error.scheduler.create", "Failed to create scheduler!");
    }

    /**
     * Utility method to generate DsSchedulerSaveReqDTO.
     *
     * @param crontab Cron expression
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
     * Utility method to generate DsSchedulerUpdateReqDTO.
     *
     * @param id Schedule ID
     * @param crontab Cron expression
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

    public static DsStartTaskReqDTO createDsStartTaskReqDTO(String processDefinitionCode) {
        // Get current date in "yyyy-MM-dd" format
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        // Construct scheduleTime field, fixed format "yyyy-MM-dd 00:00:00"
        String scheduleTime = String.format("{\"complementStartDate\":\"%s 00:00:00\",\"complementEndDate\":\"%s 00:00:00\"}", currentDate, currentDate);

        // Use builder pattern to create DsStartTaskReqDTO object, other fields are hardcoded values
        return DsStartTaskReqDTO.builder()
                .processDefinitionCode(JSONUtils.convertToLong(processDefinitionCode))
                .failureStrategy("CONTINUE")
                .warningType("NONE")
                .processInstancePriority("MEDIUM")
                .scheduleTime(scheduleTime)
                .build();
    }
}
