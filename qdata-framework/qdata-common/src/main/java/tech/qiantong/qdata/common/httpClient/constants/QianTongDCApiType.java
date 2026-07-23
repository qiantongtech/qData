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

package tech.qiantong.qdata.common.httpClient.constants;

/**
 * DolphinScheduler API Type
 *
 * @author QianTongDC
 * @date 2025-02-14
 */
public enum QianTongDCApiType {


    /**
     * Create process definition interface
     */
    CREATE_PROCESS_DEFINITION("1", "创建流程定义接口", "/projects/{projectCode}/process-definition", "POST"),

    /**
     * Update process definition interface
     */
    UPDATE_PROCESS_DEFINITION("2", "更新流程定义接口", "/projects/{projectCode}/process-definition/{code}", "PUT"),

    /**
     * Delete process definition interface
     */
    DELETE_PROCESS_DEFINITION("3", "通过流程定义ID删除流程定义", "/projects/{projectCode}/process-definition/{code}", "DELETE"),

    /**
     * Paging query process definition list interface
     */
    GET_PROCESS_DEFINITION_LIST("4", "分页查询流程定义列表接口", "/projects/{projectCode}/process-definition", "GET"),

    /**
     * Publish process definition interface
     */
    RELEASE_PROCESS_DEFINITION("5", "发布流程定义接口", "/projects/{projectCode}/process-definition/{code}/release", "POST"),

    /**
     * Batch deletion process definition interface
     */
    BATCH_DELETE_PROCESS_DEFINITION("6", "通过流程定义ID集合批量删除流程定义", "/projects/{projectCode}/process-definition/batch-delete", "POST"),

    /**
     * Mobile workflow definition interface
     */
    BATCH_MOVE_PROCESS_DEFINITION("7", "移动工作流定义接口", "/projects/{projectCode}/process-definition/batch-move", "POST"),

    /**
     * Copy workflow definition interface
     */
    BATCH_COPY_PROCESS_DEFINITION("8", "复制工作流定义接口", "/projects/{projectCode}/process-definition/batch-copy", "POST"),

    /**
     * Query process historical version information interface
     */
    GET_PROCESS_DEFINITION_VERSIONS("9", "查询流程历史版本信息接口", "/projects/{projectCode}/process-definition/{code}/versions", "GET"),

    /**
     * Switch process version interface
     */
    SWITCH_PROCESS_DEFINITION_VERSION("10", "切换流程版本接口", "/projects/{projectCode}/process-definition/{code}/versions/{version}", "GET"),

    /**
     * Delete process historical version interface
     */
    DELETE_PROCESS_DEFINITION_VERSION("11", "删除流程历史版本接口", "/projects/{projectCode}/process-definition/{code}/versions/{version}", "DELETE"),

    // Project-related interfaces start

    /**
     * Query project information interface through project ID
     */
    GET_PROJECT_INFO("12", "通过项目ID查询项目信息接口", "/v2/projects/{code}", "GET"),

    /**
     * Update project information interface
     */
    UPDATE_PROJECT("13", "更新项目信息接口", "/v2/projects/{code}", "PUT"),

    /**
     * Delete project interface
     */
    DELETE_PROJECT("14", "通过ID删除项目接口", "/v2/projects/{code}", "DELETE"),

    /**
     * Create project interface
     */
    CREATE_PROJECT("15", "创建项目接口", "/v2/projects", "POST"),

    // Project-related interfaces end
    /**
     * Query the access token interface of the specified user
     */
    GET_USER_ACCESS_TOKEN("16", "查询指定用户的access token接口", "/access-tokens/user/{userId}", "GET"),

    /**
     * Update timing interface
     */
    UPDATE_SCHEDULE("17", "更新定时接口", "/projects/{projectCode}/schedules/{id}", "PUT"),

    /**
     * Delete timing interface
     */
    DELETE_SCHEDULE("18", "根据定时id删除定时数据接口", "/projects/{projectCode}/schedules/{id}", "DELETE"),

    /**
     * Create a timing interface
     */
    CREATE_SCHEDULE("19", "创建定时接口", "/projects/{projectCode}/schedules", "POST"),

    /**
     * Scheduled online interface
     */
    SCHEDULE_ONLINE("20", "定时上线接口", "/projects/{projectCode}/schedules/{id}/online", "POST"),

    /**
     * Scheduled offline interface
     */
    SCHEDULE_OFFLINE("21", "定时下线接口", "/projects/{projectCode}/schedules/{id}/offline", "POST"),

    /**
     * Scheduled scheduling preview interface
     */
    SCHEDULE_PREVIEW("22", "定时调度预览接口", "/projects/{projectCode}/schedules/preview", "POST"),

    /**
     * Query process instance list interface
     */
    GET_PROCESS_INSTANCE_LIST("23", "查询流程实例列表接口", "/projects/{projectCode}/process-instances", "GET"),

    /**
     * Query the process instance interface by process instance ID
     */
    GET_PROCESS_INSTANCE_BY_ID("24", "通过流程实例ID查询流程实例接口", "/projects/{projectCode}/process-instances/{id}", "GET"),

    /**
     * Paged query task instance list interface
     */
    GET_TASK_INSTANCE_LIST("25", "分页查询任务实例列表接口", "/projects/{projectCode}/task-instances", "GET"),

    /**
     * Query task instance log interface
     */
    GET_TASK_INSTANCE_LOG("26", "查询任务实例日志接口", "/log/detail", "GET"),

    /**
     * Download task instance log interface
     */
    DOWNLOAD_TASK_INSTANCE_LOG("27", "下载任务实例日志接口", "/log/download-log", "GET"),

    //Task definition related interface starts

    /**
     * Generate task coding interface
     */
    GEN_TASK_DEFINITION_CODES("28", "分页查询任务实例列表接口", "/v2/tasks/gen-task-codes", "GET"),

    // Task-definition-related interfaces end

    /**
     * Obtain scheduling information based on process coding
     */
    GET_SCHEDULE_BY_PROCESS_CODE("29", "根据流程编码获取调度调度信息", "/projects/{projectCode}/schedules/getByProcessDefinitionCode/{code}", "GET"),


    /**
     * Manually start the process
     */
    POST_START_PROCESS("30", "手动启动流程", "/projects/{projectCode}/executors/start-process-instance", "POST"),

    /**
     * Execution process instance
     */
    POST_EXECUTORS_EXECUTE("31", "执行流程实例", "/projects/{projectCode}/executors/execute", "POST"),



    ;

    /**
     * API type number
     */
    private final String apiId;

    /**
     * Description
     */
    private final String description;

    /**
     * URL path
     */
    private final String url;

    /**
     * HTTP request method
     */
    private final String method;

    QianTongDCApiType(String apiId, String description, String url, String method) {
        this.apiId = apiId;
        this.description = description;
        this.url = url;
        this.method = method;
    }

    public String getApiId() {
        return this.apiId;
    }

    public String getDescription() {
        return this.description;
    }

    public String getUrl() {
        return this.url;
    }

    public String getMethod() {
        return this.method;
    }

    /**
     * Get API type
     *
     * @param apiId API type string
     * @return corresponding ApiType enumeration
     */
    public static QianTongDCApiType getApiType(String apiId) {
        for (QianTongDCApiType type : QianTongDCApiType.values()) {
            if (type.apiId.equals(apiId)) {
                return type;
            }
        }
        return null;
    }
}
