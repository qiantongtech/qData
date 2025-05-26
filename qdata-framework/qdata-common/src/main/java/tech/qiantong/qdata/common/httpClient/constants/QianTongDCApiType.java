package tech.qiantong.qdata.common.httpClient.constants;

/**
 * DolphinScheduler API 类型
 *
 * @author QianTongDC
 * @date 2025-02-14
 */
public enum QianTongDCApiType {


    /**
     * 创建流程定义接口
     */
    CREATE_PROCESS_DEFINITION("1", "创建流程定义接口", "/projects/{projectCode}/process-definition", "POST"),

    /**
     * 更新流程定义接口
     */
    UPDATE_PROCESS_DEFINITION("2", "更新流程定义接口", "/projects/{projectCode}/process-definition/{code}", "PUT"),

    /**
     * 删除流程定义接口
     */
    DELETE_PROCESS_DEFINITION("3", "通过流程定义ID删除流程定义", "/projects/{projectCode}/process-definition/{code}", "DELETE"),

    /**
     * 分页查询流程定义列表接口
     */
    GET_PROCESS_DEFINITION_LIST("4", "分页查询流程定义列表接口", "/projects/{projectCode}/process-definition", "GET"),

    /**
     * 发布流程定义接口
     */
    RELEASE_PROCESS_DEFINITION("5", "发布流程定义接口", "/projects/{projectCode}/process-definition/{code}/release", "POST"),

    /**
     * 批量删除流程定义接口
     */
    BATCH_DELETE_PROCESS_DEFINITION("6", "通过流程定义ID集合批量删除流程定义", "/projects/{projectCode}/process-definition/batch-delete", "POST"),

    /**
     * 移动工作流定义接口
     */
    BATCH_MOVE_PROCESS_DEFINITION("7", "移动工作流定义接口", "/projects/{projectCode}/process-definition/batch-move", "POST"),

    /**
     * 复制工作流定义接口
     */
    BATCH_COPY_PROCESS_DEFINITION("8", "复制工作流定义接口", "/projects/{projectCode}/process-definition/batch-copy", "POST"),

    /**
     * 查询流程历史版本信息接口
     */
    GET_PROCESS_DEFINITION_VERSIONS("9", "查询流程历史版本信息接口", "/projects/{projectCode}/process-definition/{code}/versions", "GET"),

    /**
     * 切换流程版本接口
     */
    SWITCH_PROCESS_DEFINITION_VERSION("10", "切换流程版本接口", "/projects/{projectCode}/process-definition/{code}/versions/{version}", "GET"),

    /**
     * 删除流程历史版本接口
     */
    DELETE_PROCESS_DEFINITION_VERSION("11", "删除流程历史版本接口", "/projects/{projectCode}/process-definition/{code}/versions/{version}", "DELETE"),

    //项目相关接口开始》》》》》》》》》

    /**
     * 通过项目ID查询项目信息接口
     */
    GET_PROJECT_INFO("12", "通过项目ID查询项目信息接口", "/v2/projects/{code}", "GET"),

    /**
     * 更新项目信息接口
     */
    UPDATE_PROJECT("13", "更新项目信息接口", "/v2/projects/{code}", "PUT"),

    /**
     * 删除项目接口
     */
    DELETE_PROJECT("14", "通过ID删除项目接口", "/v2/projects/{code}", "DELETE"),

    /**
     * 创建项目接口
     */
    CREATE_PROJECT("15", "创建项目接口", "/v2/projects", "POST"),

    //项目相关接口结束》》》》》》》》》
    /**
     * 查询指定用户的access token接口
     */
    GET_USER_ACCESS_TOKEN("16", "查询指定用户的access token接口", "/access-tokens/user/{userId}", "GET"),

    /**
     * 更新定时接口
     */
    UPDATE_SCHEDULE("17", "更新定时接口", "/projects/{projectCode}/schedules/{id}", "PUT"),

    /**
     * 删除定时接口
     */
    DELETE_SCHEDULE("18", "根据定时id删除定时数据接口", "/projects/{projectCode}/schedules/{id}", "DELETE"),

    /**
     * 创建定时接口
     */
    CREATE_SCHEDULE("19", "创建定时接口", "/projects/{projectCode}/schedules", "POST"),

    /**
     * 定时上线接口
     */
    SCHEDULE_ONLINE("20", "定时上线接口", "/projects/{projectCode}/schedules/{id}/online", "POST"),

    /**
     * 定时下线接口
     */
    SCHEDULE_OFFLINE("21", "定时下线接口", "/projects/{projectCode}/schedules/{id}/offline", "POST"),

    /**
     * 定时调度预览接口
     */
    SCHEDULE_PREVIEW("22", "定时调度预览接口", "/projects/{projectCode}/schedules/preview", "POST"),

    /**
     * 查询流程实例列表接口
     */
    GET_PROCESS_INSTANCE_LIST("23", "查询流程实例列表接口", "/projects/{projectCode}/process-instances", "GET"),

    /**
     * 通过流程实例ID查询流程实例接口
     */
    GET_PROCESS_INSTANCE_BY_ID("24", "通过流程实例ID查询流程实例接口", "/projects/{projectCode}/process-instances/{id}", "GET"),

    /**
     * 分页查询任务实例列表接口
     */
    GET_TASK_INSTANCE_LIST("25", "分页查询任务实例列表接口", "/projects/{projectCode}/task-instances", "GET"),

    /**
     * 查询任务实例日志接口
     */
    GET_TASK_INSTANCE_LOG("26", "查询任务实例日志接口", "/log/detail", "GET"),

    /**
     * 下载任务实例日志接口
     */
    DOWNLOAD_TASK_INSTANCE_LOG("27", "下载任务实例日志接口", "/log/download-log", "GET"),

    //任务定义相关接口开始

    /**
     * 生成任务编码接口
     */
    GEN_TASK_DEFINITION_CODES("28", "分页查询任务实例列表接口", "/v2/tasks/gen-task-codes", "GET"),

    //任务定义相关接口接受结束》》》》》》》》》

    /**
     * 根据流程编码获取调度调度信息
     */
    GET_SCHEDULE_BY_PROCESS_CODE("29", "根据流程编码获取调度调度信息", "/projects/{projectCode}/schedules/getByProcessDefinitionCode/{code}", "GET"),


    /**
     * 手动启动流程
     */
    POST_START_PROCESS("30", "手动启动流程", "/projects/{projectCode}/executors/start-process-instance", "POST"),

    /**
     * 执行流程实例
     */
    POST_EXECUTORS_EXECUTE("31", "执行流程实例", "/projects/{projectCode}/executors/execute", "POST"),



    ;

    /**
     * API 类型的编号
     */
    private final String apiId;

    /**
     * 描述
     */
    private final String description;

    /**
     * URL 路径
     */
    private final String url;

    /**
     * HTTP 请求方法
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
     * 获取 API 类型
     *
     * @param apiId API 类型字符串
     * @return 对应的 ApiType 枚举
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
