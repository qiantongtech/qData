package tech.qiantong.qdata.module.mc.utils;

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
import tech.qiantong.qdata.module.mc.utils.model.TaskSaveReqInput;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class McTaskConverter {


    private static String defaultURL;

    @Value("${ds.mc_url:}")
    private void setDefaultURL(String defaultURL) {
        McTaskConverter.defaultURL = defaultURL;
    }


    private static final String DEFAULT_CONDITION_TYPE = "NONE"; // The default condition type is "NONE"
    private static final String DEFAULT_FLAG = "YES"; // Default flag indicating that the node is enabled


    // Default configuration constants
    private static final long DEFAULT_ENVIRONMENT_CODE = 133155949418208L; // Default environment encoding
    private static final String DEFAULT_WORKER_GROUP = "default"; // Default workgroup
    private static final String DEFAULT_IS_CACHE = "NO"; // Caching is not enabled by default
    private static final String DEFAULT_TASK_PRIORITY = "MEDIUM"; // Default task priority
    private static final String DEFAULT_TASK_TYPE = "HTTP"; // Default task type, SPARK or DATAX, etc.
    private static final String HTTP_METHOD = "PUT";
    private static final String HTTP_BODY = "";
    private static final String HTTP_CHECK_CONDITION = "STATUS_CODE_DEFAULT";
    private static final String CONDITION = "";
    private static final int CONNECT_TIMEOUT = 60000;
    private static final int SOCKET_TIMEOUT = 60000;



    public static DsTaskSaveReqDTO buildDsTaskSaveReq(TaskSaveReqInput input) {
        // Create return entity
        DsTaskSaveReqDTO dsTaskSaveReqDTO = new DsTaskSaveReqDTO();

        // 1. Encapsulate basic parameters
        dsTaskSaveReqDTO.setName(input.getName());  // Task name
        dsTaskSaveReqDTO.setDescription("");  // Description is empty
        dsTaskSaveReqDTO.setExecutionType("PARALLEL");  // Hard-coded execution type is "PARALLEL"

        // 2. Encapsulate node information, HTTP type
        String taskDefinition = buildTaskDefinitionForHttp(input);

        // 3. Build task relationships
        String taskRelation = buildTaskRelationJson(input);  // The default relationship type is "NONE"

        // 4. Build task location
        String location = buildTaskNodeLocations(input);

        // Set various fields of the task
        dsTaskSaveReqDTO.setTaskDefinitionJson(taskDefinition);
        dsTaskSaveReqDTO.setTaskRelationJson(taskRelation);
        dsTaskSaveReqDTO.setLocations(location);

        return dsTaskSaveReqDTO;
    }

    private static String buildTaskDefinitionForHttp(TaskSaveReqInput input) {
        // Set parameters for HTTP type tasks
        Map<String, Object> taskMap = new HashMap<>();

        taskMap.put("id", input.getNodeId());  // Use the task ID passed in
        taskMap.put("name", input.getName());  // Hard-code the task name
        taskMap.put("code", input.getNodeCode());  // Use the passed task code
        taskMap.put("version", 1);  // Hard-coded version
        taskMap.put("description", "");  // Description is empty
        taskMap.put("workerGroup",DEFAULT_WORKER_GROUP);  // Hard-coded worker group
        taskMap.put("environmentCode", DEFAULT_ENVIRONMENT_CODE);  // Hard-coded environment code
        taskMap.put("flag",  DEFAULT_FLAG); // The default flag is "YES"
        taskMap.put("isCache", DEFAULT_IS_CACHE);  // Do not cache
        taskMap.put("taskPriority", DEFAULT_TASK_PRIORITY);  // Medium priority
        taskMap.put("taskType", DEFAULT_TASK_TYPE);  // HTTP type tasks
        taskMap.put("taskExecuteType", "BATCH");

        // Task parameters
        Map<String, Object> taskParams = new HashMap<>();
        taskParams.put("localParams", new ArrayList<>());  // Default empty list
        taskParams.put("resourceList", new ArrayList<>());  // Default empty list
        taskParams.put("httpMethod", HTTP_METHOD);  // The default HTTP method is GET
        taskParams.put("httpBody", HTTP_BODY);  // Default HTTP body is empty
        taskParams.put("httpCheckCondition", HTTP_CHECK_CONDITION);  // Default check condition
        taskParams.put("httpParams", input.getHttpParams());  // Use the httpParams passed in
        taskParams.put("url", defaultURL + "/"+ String.valueOf(input.getId()));  // Default URL
        taskParams.put("condition", CONDITION);  // The default condition is empty
        taskParams.put("connectTimeout", CONNECT_TIMEOUT);  // Default connection timeout 60000ms
        taskParams.put("socketTimeout", SOCKET_TIMEOUT);  // Default socket timeout 60000ms

        // Add taskParams to taskMap
        taskMap.put("taskParams", taskParams);

        // Return JSON string
        return JSON.toJSONString(Collections.singletonList(taskMap));
    }

    private static String buildTaskRelationJson(TaskSaveReqInput input) {
        // Build task relationships
        Map<String, Object> relationMap = new HashMap<>();
        relationMap.put("id", input.getNodeId());  // Hard-coded relationship ID
        relationMap.put("preTaskCode", 0L);  // The previous task code is hard-coded to 0
        relationMap.put("preTaskVersion", 0);  // The previous task version is hard-coded to 0
        relationMap.put("postTaskCode", input.getNodeCode());  // The downstream task code is hard-coded
        relationMap.put("postTaskVersion", 1);  // The version number of the post-task is hard-coded
        relationMap.put("conditionType", DEFAULT_CONDITION_TYPE);  // Use input parameter conditionType

        // Return JSON string
        return JSON.toJSONString(Collections.singletonList(relationMap));
    }

    private static String buildTaskNodeLocations(TaskSaveReqInput input) {
        // Hard-code the task node location
        Map<String, Object> locationMap = new HashMap<>();
        locationMap.put("taskCode", input.getNodeCode());  // Task ID
        locationMap.put("x", 138.4886474609375);  // Hard-coded coordinates X
        locationMap.put("y", 184.9232940673828);  // Hard-coded coordinate Y

        // Returns a JSON string of location
        return JSON.toJSONString(Collections.singletonList(locationMap));
    }

    public static Long stringToLong(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            // If the conversion fails, return null or a default value if appropriate
            System.err.println("Invalid number format: " + value);
            return null; // Or you can return the default value, such as 0L
        }
    }

    public static String longToString(Long value) {
        if (value == null) {
            return null; // Or return the empty string "", adjust according to needs
        }
        return value.toString();
    }

    public static TaskDefinition getFirstTaskDefinition(ProcessDefinition definition) {
        // Get a list of task definitions
        List<TaskDefinition> taskDefinitionList = definition.getTaskDefinitionList();

        // Determine whether the list is empty and prevent IndexOutOfBoundsException
        if (taskDefinitionList != null && !taskDefinitionList.isEmpty()) {
            // Return the first task definition
            return taskDefinitionList.get(0);
        }

        // If the list is empty, return null or other default values as appropriate
        throw new ServiceException("mc.error.scheduler.create", "创建调度器，失败！");
    }

    /**
     * Utility method to generate DsSchedulerSaveReqDTO.
     *
     * @param crontab Cron expression
     * @param processDefinitionCode task code
     * @return DsSchedulerSaveReqDTO
     */
    public static DsSchedulerSaveReqDTO createSchedulerRequest(String crontab, String processDefinitionCode) {
        // Get current time
        String startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        // Get the time 100 years from now
        long currentTime = System.currentTimeMillis();
        String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(currentTime + 100L * 365 * 24 * 60 * 60 * 1000));

        // Create DsSchedulerSaveReqDTO and set default value
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
     * Utility method, generates DsSchedulerUpdateReqDTO.
     *
     * @param id scheduling ID
     * @param crontab Cron expression
     * @param processDefinitionCode task code
     * @return DsSchedulerUpdateReqDTO
     */
    public static DsSchedulerUpdateReqDTO createSchedulerUpdateRequest(Long id, String crontab, String processDefinitionCode) {
        // Get current time
        String startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        // Get the time 100 years from now
        long currentTime = System.currentTimeMillis();
        String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(currentTime + 100L * 365 * 24 * 60 * 60 * 1000));

        // Create DsSchedulerUpdateReqDTO and set default value
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
        // Get the current date in the format "yyyy-MM-dd"
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        // Construct scheduleTime field, fixed format "yyyy-MM-dd 00:00:00"
        String scheduleTime = String.format("{\"complementStartDate\":\"%s 00:00:00\",\"complementEndDate\":\"%s 00:00:00\"}", currentDate, currentDate);

        // Use builder mode to create a DsStartTaskReqDTO object, and other fields are hard-coded values.
        return DsStartTaskReqDTO.builder()
                .processDefinitionCode(JSONUtils.convertToLong(processDefinitionCode))
                .failureStrategy("CONTINUE")
                .warningType("NONE")
                .processInstancePriority("MEDIUM")
                .scheduleTime(scheduleTime)
                .build();
    }
}
