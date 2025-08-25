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


    private static final String DEFAULT_CONDITION_TYPE = "NONE"; // 默认条件类型为 "NONE"
    private static final String DEFAULT_FLAG = "YES"; // 默认标志，表示节点启用


    // 默认配置常量
    private static final long DEFAULT_ENVIRONMENT_CODE = 133155949418208L; // 默认环境编码
    private static final String DEFAULT_WORKER_GROUP = "default"; // 默认工作组
    private static final String DEFAULT_IS_CACHE = "NO"; // 默认不启用缓存
    private static final String DEFAULT_TASK_PRIORITY = "MEDIUM"; // 默认任务优先级
    private static final String DEFAULT_TASK_TYPE = "HTTP"; // 默认任务类型，SPARK或DATAX等
    private static final String HTTP_METHOD = "PUT";
    private static final String HTTP_BODY = "";
    private static final String HTTP_CHECK_CONDITION = "STATUS_CODE_DEFAULT";
    private static final String CONDITION = "";
    private static final int CONNECT_TIMEOUT = 60000;
    private static final int SOCKET_TIMEOUT = 60000;



    public static DsTaskSaveReqDTO buildDsTaskSaveReq(TaskSaveReqInput input) {
        // 创建返回实体
        DsTaskSaveReqDTO dsTaskSaveReqDTO = new DsTaskSaveReqDTO();

        // 1. 封装基础参数
        dsTaskSaveReqDTO.setName(input.getName());  // 任务名
        dsTaskSaveReqDTO.setDescription("");  // 描述为空
        dsTaskSaveReqDTO.setExecutionType("PARALLEL");  // 写死执行类型为 "PARALLEL"

        // 2. 封装节点信息，HTTP 类型
        String taskDefinition = buildTaskDefinitionForHttp(input);

        // 3. 构建任务关系
        String taskRelation = buildTaskRelationJson(input);  // 默认关系类型为 "NONE"

        // 4. 构建任务位置
        String location = buildTaskNodeLocations(input);

        // 设置任务的各个字段
        dsTaskSaveReqDTO.setTaskDefinitionJson(taskDefinition);
        dsTaskSaveReqDTO.setTaskRelationJson(taskRelation);
        dsTaskSaveReqDTO.setLocations(location);

        return dsTaskSaveReqDTO;
    }

    private static String buildTaskDefinitionForHttp(TaskSaveReqInput input) {
        // 设置 HTTP 类型任务的参数
        Map<String, Object> taskMap = new HashMap<>();

        taskMap.put("id", input.getNodeId());  // 使用传入的任务 ID
        taskMap.put("name", input.getName());  // 写死任务名
        taskMap.put("code", input.getNodeCode());  // 使用传入的任务 code
        taskMap.put("version", 1);  // 写死版本
        taskMap.put("description", "");  // 描述为空
        taskMap.put("workerGroup",DEFAULT_WORKER_GROUP);  // 写死工作组
        taskMap.put("environmentCode", DEFAULT_ENVIRONMENT_CODE);  // 写死环境编码
        taskMap.put("flag",  DEFAULT_FLAG); // 默认 flag 为 "YES"
        taskMap.put("isCache", DEFAULT_IS_CACHE);  // 不缓存
        taskMap.put("taskPriority", DEFAULT_TASK_PRIORITY);  // 中等优先级
        taskMap.put("taskType", DEFAULT_TASK_TYPE);  // HTTP 类型任务
        taskMap.put("taskExecuteType", "BATCH");

        // 任务参数
        Map<String, Object> taskParams = new HashMap<>();
        taskParams.put("localParams", new ArrayList<>());  // 默认空列表
        taskParams.put("resourceList", new ArrayList<>());  // 默认空列表
        taskParams.put("httpMethod", HTTP_METHOD);  // 默认 HTTP 方法为 GET
        taskParams.put("httpBody", HTTP_BODY);  // 默认 HTTP body 空
        taskParams.put("httpCheckCondition", HTTP_CHECK_CONDITION);  // 默认检查条件
        taskParams.put("httpParams", input.getHttpParams());  // 使用传入的 httpParams
        taskParams.put("url", defaultURL + "/"+ String.valueOf(input.getId()));  // 默认 URL
        taskParams.put("condition", CONDITION);  // 默认条件为空
        taskParams.put("connectTimeout", CONNECT_TIMEOUT);  // 默认连接超时 60000ms
        taskParams.put("socketTimeout", SOCKET_TIMEOUT);  // 默认 socket 超时 60000ms

        // 将 taskParams 加入到 taskMap
        taskMap.put("taskParams", taskParams);

        // 返回 JSON 字符串
        return JSON.toJSONString(Collections.singletonList(taskMap));
    }

    private static String buildTaskRelationJson(TaskSaveReqInput input) {
        // 构建任务关系
        Map<String, Object> relationMap = new HashMap<>();
        relationMap.put("id", input.getNodeId());  // 写死关系 ID
        relationMap.put("preTaskCode", 0L);  // 前任务 code 写死为 0
        relationMap.put("preTaskVersion", 0);  // 前任务版本号写死为 0
        relationMap.put("postTaskCode", input.getNodeCode());  // 后任务 code 写死
        relationMap.put("postTaskVersion", 1);  // 后任务版本号写死
        relationMap.put("conditionType", DEFAULT_CONDITION_TYPE);  // 使用入参 conditionType

        // 返回 JSON 字符串
        return JSON.toJSONString(Collections.singletonList(relationMap));
    }

    private static String buildTaskNodeLocations(TaskSaveReqInput input) {
        // 写死任务节点位置
        Map<String, Object> locationMap = new HashMap<>();
        locationMap.put("taskCode", input.getNodeCode());  // 任务 ID
        locationMap.put("x", 138.4886474609375);  // 写死坐标 X
        locationMap.put("y", 184.9232940673828);  // 写死坐标 Y

        // 返回位置的 JSON 字符串
        return JSON.toJSONString(Collections.singletonList(locationMap));
    }

    public static Long stringToLong(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            // 如果转换失败，返回 null 或根据需要返回默认值
            System.err.println("Invalid number format: " + value);
            return null; // 或者可以返回默认值，如 0L
        }
    }

    public static String longToString(Long value) {
        if (value == null) {
            return null; // 或者返回空字符串 ""，根据需求调整
        }
        return value.toString();
    }
    public static TaskDefinition getFirstTaskDefinition(ProcessDefinition definition) {
        // 获取任务定义列表
        List<TaskDefinition> taskDefinitionList = definition.getTaskDefinitionList();

        // 判断列表是否为空，防止 IndexOutOfBoundsException
        if (taskDefinitionList != null && !taskDefinitionList.isEmpty()) {
            // 返回第一个任务定义
            return taskDefinitionList.get(0);
        }

        // 如果列表为空，返回 null 或根据需要返回其他默认值
        throw new ServiceException("创建调度器，失败！");
    }

    /**
     * 工具方法，生成 DsSchedulerSaveReqDTO。
     *
     * @param crontab Cron 表达式
     * @param processDefinitionCode 任务编码
     * @return DsSchedulerSaveReqDTO
     */
    public static DsSchedulerSaveReqDTO createSchedulerRequest(String crontab, String processDefinitionCode) {
        // 获取当前时间
        String startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        // 获取100年后的时间
        long currentTime = System.currentTimeMillis();
        String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(currentTime + 100L * 365 * 24 * 60 * 60 * 1000));

        // 创建 DsSchedulerSaveReqDTO 并设置默认值
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
     * 工具方法，生成 DsSchedulerUpdateReqDTO。
     *
     * @param id 调度ID
     * @param crontab Cron 表达式
     * @param processDefinitionCode 任务编码
     * @return DsSchedulerUpdateReqDTO
     */
    public static DsSchedulerUpdateReqDTO createSchedulerUpdateRequest(Long id, String crontab, String processDefinitionCode) {
        // 获取当前时间
        String startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        // 获取100年后的时间
        long currentTime = System.currentTimeMillis();
        String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(currentTime + 100L * 365 * 24 * 60 * 60 * 1000));

        // 创建 DsSchedulerUpdateReqDTO 并设置默认值
        DsSchedulerUpdateReqDTO dto = new DsSchedulerUpdateReqDTO();
        dto.setId(id); // 设置调度ID
        dto.setSchedule(String.format("{\"startTime\":\"%s\",\"endTime\":\"%s\",\"crontab\":\"%s\",\"timezoneId\":\"Asia/Shanghai\"}",
                startTime, endTime, crontab));
        dto.setProcessDefinitionCode(processDefinitionCode);
        dto.setFailureStrategy("CONTINUE");
        dto.setWorkerGroup("default");
        dto.setTenantCode("default");

        return dto;
    }

    public static DsStartTaskReqDTO createDsStartTaskReqDTO(String processDefinitionCode) {
        // 获取当前日期，格式为 "yyyy-MM-dd"
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        // 构造 scheduleTime 字段，固定格式 "yyyy-MM-dd 00:00:00"
        String scheduleTime = String.format("{\"complementStartDate\":\"%s 00:00:00\",\"complementEndDate\":\"%s 00:00:00\"}", currentDate, currentDate);

        // 使用 builder 模式创建 DsStartTaskReqDTO 对象，其他字段均为写死的值
        return DsStartTaskReqDTO.builder()
                .processDefinitionCode(JSONUtils.convertToLong(processDefinitionCode))
                .failureStrategy("CONTINUE")
                .warningType("NONE")
                .processInstancePriority("MEDIUM")
                .scheduleTime(scheduleTime)
                .build();
    }
}
