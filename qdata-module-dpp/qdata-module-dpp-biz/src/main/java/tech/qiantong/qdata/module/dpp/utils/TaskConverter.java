package tech.qiantong.qdata.module.dpp.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSON;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tech.qiantong.qdata.api.ds.api.etl.*;
import tech.qiantong.qdata.api.ds.api.etl.ds.*;
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

@Component
public class TaskConverter {
    @Resource
    private FlinkxJson flinkxJson;

    private static String resourceName;
    private static String defaultMainClass;
    private static String defaultMaster;
    private static String resourceUrl;
    private static RabbitmqConfig rabbitmqConfig;

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

    // 默认配置常量
    private static final long DEFAULT_ENVIRONMENT_CODE = 133155949418208L; // 默认环境编码
    private static final String DEFAULT_WORKER_GROUP = "default"; // 默认工作组
    private static final String DEFAULT_FLAG = "YES"; // 默认标志，表示节点启用
    private static final String DEFAULT_IS_CACHE = "NO"; // 默认不启用缓存
    private static final String DEFAULT_TASK_PRIORITY = "MEDIUM"; // 默认任务优先级
    private static final String DEFAULT_TASK_TYPE = "SPARK"; // 默认任务类型，SPARK或DATAX等
    private static final String DEFAULT_PROGRAM_TYPE = "JAVA"; // 默认程序类型，JAVA
    private static final String DEFAULT_MAIN_JAR = "file:/dolphinscheduler/default/resources/spart-demo-1.0.jar"; // 默认主Jar路径
    private static final String DEFAULT_DEPLOY_MODE = "client"; // 默认部署模式
    private static final int DEFAULT_DRIVER_CORES = 1; // 默认驱动核心数
    private static final String DEFAULT_DRIVER_MEMORY = "2G"; // 默认驱动内存
    private static final int DEFAULT_NUM_EXECUTORS = 1; // 默认执行器数量
    private static final String DEFAULT_EXECUTOR_MEMORY = "4G"; // 默认执行器内存
    private static final int DEFAULT_EXECUTOR_CORES = 2; // 默认执行器核心数
    private static final String DEFAULT_SQL_EXECUTION_TYPE = "SCRIPT"; // 默认SQL执行类型
    private static final String DEFAULT_CONDITION_TYPE = "NONE"; // 默认条件类型为 "NONE"

    private static final int DEFAULT_TASK_failRetryTimes = 0; // failRetryTimes失败重试次数
    private static final int DEFAULT_TASK_delayTime = 0; // delayTime延时执行时间
    private static final int DEFAULT_TASK_failRetryInterval = 1; // failRetryInterval失败重试间隔



    public static final String TASK_INSTANCE_LOG_KEY = "log:taskInstanceLog:";//任务实例日志key

    public static final String PROCESS_INSTANCE_LOG_KEY = "log:processInstanceLog:";//流程实例日志key

    public static DsTaskSaveReqDTO buildDsTaskSaveReq(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO) {//名字
        //创建返回实体
        DsTaskSaveReqDTO dsTaskSaveReqDTO = new DsTaskSaveReqDTO();
        //1、封装基础参数
        dsTaskSaveReqDTO.setName(dppEtlNewNodeSaveReqVO.getName());
        if(StringUtils.isNotEmpty(dppEtlNewNodeSaveReqVO.getCode())){
            dsTaskSaveReqDTO.setProcessDefinitionCode(Long.parseLong(dppEtlNewNodeSaveReqVO.getCode()));
        }
        dsTaskSaveReqDTO.setDescription(dppEtlNewNodeSaveReqVO.getDescription());
        dsTaskSaveReqDTO.setExecutionType(dppEtlNewNodeSaveReqVO.getExecutionType());


        //2、封装节点信息 DATAX、SPARK
        String taskDefinition = buildTaskDefinition(dppEtlNewNodeSaveReqVO.getTaskDefinitionList());

        String taskRelation = buildTaskRelationJson(dppEtlNewNodeSaveReqVO.getTaskRelationJson());

        String location = buildTaskNodeLocations(dppEtlNewNodeSaveReqVO.getLocations());


        dsTaskSaveReqDTO.setTaskDefinitionJson(taskDefinition);
        dsTaskSaveReqDTO.setTaskRelationJson(taskRelation);
        dsTaskSaveReqDTO.setLocations(location);


        return dsTaskSaveReqDTO;
    }

    private static String buildTaskNodeLocations(List<Map<String, Object>> locations) {
        // 解析输入的 JSON 字符串为 List
        List<Map<String, Object>> list = locations;

        List<Map<String, Object>> result = new ArrayList<>();

        // 遍历每个节点坐标信息
        for (Map<String, Object> location : list) {
            Map<String, Object> locationMap = new HashMap<>();

            // 填充必要字段
            locationMap.put("taskCode", Long.parseLong(String.valueOf(location.getOrDefault("taskCode", 0L)))); // 默认 taskCode 为 0
            locationMap.put("x", location.getOrDefault("x", 0)); // 默认 x 为 0
            locationMap.put("y", location.getOrDefault("y", 0)); // 默认 y 为 0

            // 将处理后的坐标信息加入结果列表
            result.add(locationMap);
        }

        // 返回处理后的 JSON 字符串
        return JSON.toJSONString(result);
    }

    private static String buildTaskRelationJson(String taskRelationJson) {
        // 解析输入的 JSON 字符串为 List
        List<Map<String, Object>> list = JSONUtils.convertTaskDefinitionJson(taskRelationJson);

        List<Map<String, Object>> result = new ArrayList<>();

        // 遍历每个关系节点
        for (Map<String, Object> relation : list) {
            Map<String, Object> relationMap = new HashMap<>();

            // 填充默认值和必要字段
            relationMap.put("id", relation.getOrDefault("dsId", null)); // 默认 id 为 0
            relationMap.put("preTaskCode", relation.getOrDefault("preTaskCode", 0L)); // 默认 preTaskCode 为 0
            relationMap.put("preTaskVersion", relation.getOrDefault("preTaskVersion", 0)); // 默认 preTaskVersion 为 0
            relationMap.put("postTaskCode", relation.getOrDefault("postTaskCode", 0L)); // 默认 postTaskCode 为 0
            relationMap.put("postTaskVersion", relation.getOrDefault("postTaskVersion", 0)); // 默认 postTaskVersion 为 0
            relationMap.put("conditionType", relation.getOrDefault("conditionType", DEFAULT_CONDITION_TYPE)); // 默认条件类型为 "NONE"

            // 将处理后的节点关系加入结果列表
            result.add(relationMap);
        }

        // 返回处理后的 JSON 字符串
        return JSON.toJSONString(result);
    }

    /**
     * 构建任务定义
     *
     * @param taskDefinitionJson 任务定义JSON字符串
     * @return 构建后的任务定义JSON字符串
     */
    public static String buildTaskDefinition(String taskDefinitionJson) {
        List<Map<String, Object>> list = JSONUtils.convertTaskDefinitionJson(taskDefinitionJson);

        List<Map<String, Object>> result = new ArrayList<>();

        //自定义参数
//        Map<String, Object> definitionJsonMap = JSONUtils.convertTaskDefinitionJsonMap(draftJson);


        // 遍历每个任务定义
        for (Map<String, Object> task : list) {
            // 处理每个 task 的默认值和必要字段
            Map<String, Object> taskMap = new HashMap<>();

            // 设置基本信息字段
            taskMap.put("id", task.getOrDefault("dsId", null)); // 默认 id 为 0
            taskMap.put("name", task.getOrDefault("name", "")); // 默认空字符串
            taskMap.put("code", task.getOrDefault("code", 0L)); // 默认 code 为 0L
            taskMap.put("version", task.getOrDefault("version", 0)); // 默认版本号为1
            taskMap.put("description", task.getOrDefault("description", "")); // 默认描述为空
            taskMap.put("workerGroup", task.getOrDefault("workerGroup", DEFAULT_WORKER_GROUP)); // 默认 workerGroup 为 "default"
            taskMap.put("environmentCode", task.getOrDefault("environmentCode", DEFAULT_ENVIRONMENT_CODE)); // 默认环境编码
            taskMap.put("flag", DEFAULT_FLAG); // 默认 flag 为 "YES"
            taskMap.put("isCache", task.getOrDefault("isCache", DEFAULT_IS_CACHE)); // 默认 isCache 为 "NO"
            taskMap.put("taskPriority", task.getOrDefault("taskPriority", DEFAULT_TASK_PRIORITY)); // 默认任务优先级为 "MEDIUM"
            taskMap.put("taskType", task.getOrDefault("taskType", DEFAULT_TASK_TYPE)); // 默认任务类型为 "SPARK"
            taskMap.put("taskExecuteType", "BATCH");

            //2025-06-25 新增配置项默认值
            taskMap.put("failRetryTimes", MapUtils.getObject(task,"failRetryTimes",DEFAULT_TASK_failRetryTimes));
            taskMap.put("delayTime", MapUtils.getObject(task,"delayTime",DEFAULT_TASK_delayTime));
            taskMap.put("failRetryInterval", MapUtils.getObject(task,"failRetryInterval",DEFAULT_TASK_failRetryInterval));

            //组件taskParams的封装
            String componentType = String.valueOf(task.get("componentType")); //组件类型
            Map<String, Object> params = (Map<String, Object>) MapUtils.getObject(task, "taskParams");

            //根据类型存入默认数据
            if (StringUtils.equals(TaskComponentTypeEnum.SPARK_CLEAN.getCode(), componentType)
                    || StringUtils.equals(TaskComponentTypeEnum.SPARK_SQL_DEV.getCode(), componentType)) {
                params.put("mainClass", defaultMainClass);
                params.put("resourceName", resourceName);
                params.put("master", defaultMaster);
            }
            // 提取参数
//            params.put("driverCores", MapUtils.getObject(definitionJsonMap, "driverCores", DEFAULT_DRIVER_CORES));
//            params.put("driverMemory", MapUtils.getObject(definitionJsonMap, "driverMemory", DEFAULT_DRIVER_MEMORY));
//            params.put("numExecutors", MapUtils.getObject(definitionJsonMap, "numExecutors", DEFAULT_NUM_EXECUTORS));
//            params.put("executorMemory", MapUtils.getObject(definitionJsonMap, "executorMemory", DEFAULT_EXECUTOR_MEMORY));
//            params.put("executorCores", MapUtils.getObject(definitionJsonMap, "executorCores", DEFAULT_EXECUTOR_CORES));
//            params.put("yarnQueue", MapUtils.getObject(definitionJsonMap, "yarnQueue", ""));

            // 将任务的taskParams加入到taskMap中
            taskMap.put("taskParams", ComponentFactory.getComponentItem(componentType).parse(params));

            // 将填充好的任务加入到结果列表
            result.add(taskMap);
        }

        // 返回处理后的JSON字符串
        return JSON.toJSONString(result);
    }


    /**
     * 将 DppEtlNewNodeSaveReqVO 和 ProcessDefinition 转换为 DppEtlTaskSaveReqVO
     *
     * @param dppEtlNewNodeSaveReqVO 外部请求的任务数据
     * @param data                   流程定义数据
     * @return 转换后的 DppEtlTaskSaveReqVO
     */
    public static DppEtlTaskSaveReqVO convertToDppEtlTaskSaveReqVO(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO, ProcessDefinition data) {
        // 创建 DppEtlTaskSaveReqVO 对象
        DppEtlTaskSaveReqVO createReqVO = new DppEtlTaskSaveReqVO();

        // 填充任务数据
        createReqVO.setType(dppEtlNewNodeSaveReqVO.getType());//任务类型
        createReqVO.setName(data.getName()); // 任务名称
        createReqVO.setCode(String.valueOf(data.getCode())); // 任务编码
        createReqVO.setVersion(data.getVersion()); // 版本号
        createReqVO.setProjectId(dppEtlNewNodeSaveReqVO.getProjectId()); // 项目ID
        createReqVO.setProjectCode(String.valueOf(data.getProjectCode())); // 项目编码
        createReqVO.setDescription(dppEtlNewNodeSaveReqVO.getDescription()); // 描述
        createReqVO.setLocations(data.getLocations()); // 节点坐标信息
        createReqVO.setLocations(data.getLocations()); // 节点坐标信息
        createReqVO.setDsId(data.getId()); // DolphinScheduler的ID

        String releaseState = dppEtlNewNodeSaveReqVO.getReleaseState();
        // 根据 releaseState 设置 status（0: 未上线, 1: 已上线）
        if (StringUtils.equals("-2", releaseState) || StringUtils.equals("-3", releaseState)) {
            createReqVO.setStatus(releaseState); // 已上线
        } else if ("offline".equalsIgnoreCase(data.getReleaseState())) {
            createReqVO.setStatus("0"); // 未上线
        } else if ("online".equalsIgnoreCase(data.getReleaseState())) {
            createReqVO.setStatus("1"); // 已上线
        } else {
            createReqVO.setStatus("0"); // 未上线
        }
        createReqVO.setRemark(""); // 默认备注（可根据需要调整）

        createReqVO.setExecutionType(data.getExecutionType());//执行执行策略
        // 填充创建者和更新时间信息
        createReqVO.setCreatorId(dppEtlNewNodeSaveReqVO.getCreatorId()); // 假设项目ID为创建者ID（根据需求调整）
        createReqVO.setCreateBy(dppEtlNewNodeSaveReqVO.getCreateBy()); // 假设任务名称为创建者（根据需求调整）
        createReqVO.setCreateTime(dppEtlNewNodeSaveReqVO.getCreateTime()); // 设置当前时间为创建时间
        createReqVO.setUpdatorId(dppEtlNewNodeSaveReqVO.getUpdatorId()); // 假设项目ID为更新者ID（根据需求调整）
        createReqVO.setUpdateBy(dppEtlNewNodeSaveReqVO.getUpdateBy()); // 假设任务名称为更新者（根据需求调整）
        createReqVO.setUpdateTime(dppEtlNewNodeSaveReqVO.getUpdateTime()); // 设置当前时间为更新时间

        createReqVO.setPersonCharge(dppEtlNewNodeSaveReqVO.getPersonCharge());
        createReqVO.setContactNumber(dppEtlNewNodeSaveReqVO.getContactNumber());
        createReqVO.setCatCode(dppEtlNewNodeSaveReqVO.getCatCode());
        createReqVO.setType(dppEtlNewNodeSaveReqVO.getType());

        //暂时没意义参数
        createReqVO.setTimeout(dppEtlNewNodeSaveReqVO.getTimeout());


        // 返回生成的 DppEtlTaskSaveReqVO
        return createReqVO;
    }

    public static DppEtlTaskLogSaveReqVO fromDppEtlTaskLogSaveReqVO(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO, ProcessDefinition processDefinition) {
        // 创建 DppEtlTaskSaveReqVO 对象
        DppEtlTaskLogSaveReqVO createReqVO = new DppEtlTaskLogSaveReqVO();
        ProcessDefinitionLog data = processDefinition.getProcessDefinitionLog();
        // 填充任务数据
        createReqVO.setType(dppEtlNewNodeSaveReqVO.getType());
        createReqVO.setName(data.getName()); // 任务名称
        createReqVO.setCode(String.valueOf(data.getCode())); // 任务编码
        createReqVO.setVersion(data.getVersion()); // 版本号
        createReqVO.setProjectId(dppEtlNewNodeSaveReqVO.getProjectId()); // 项目ID
        createReqVO.setProjectCode(String.valueOf(data.getProjectCode())); // 项目编码
        createReqVO.setDescription(dppEtlNewNodeSaveReqVO.getDescription()); // 描述
        createReqVO.setLocations(data.getLocations()); // 节点坐标信息
        createReqVO.setDsId(data.getId()); // DolphinScheduler的ID

        // 根据 releaseState 设置 status（0: 未上线, 1: 已上线）
        if ("online".equalsIgnoreCase(data.getReleaseState())) {
            createReqVO.setStatus("1"); // 已上线
        } else if ("offline".equalsIgnoreCase(data.getReleaseState())) {
            createReqVO.setStatus("0"); // 未上线
        } else {
            createReqVO.setStatus("0"); // 未上线
        }
        createReqVO.setRemark(""); // 默认备注（可根据需要调整）

        createReqVO.setExecutionType(data.getExecutionType());//执行执行策略
        // 填充创建者和更新时间信息
        createReqVO.setCreatorId(dppEtlNewNodeSaveReqVO.getCreatorId()); // 假设项目ID为创建者ID（根据需求调整）
        createReqVO.setCreateBy(dppEtlNewNodeSaveReqVO.getCreateBy()); // 假设任务名称为创建者（根据需求调整）
        createReqVO.setCreateTime(dppEtlNewNodeSaveReqVO.getCreateTime()); // 设置当前时间为创建时间
        createReqVO.setUpdatorId(dppEtlNewNodeSaveReqVO.getUpdatorId()); // 假设项目ID为更新者ID（根据需求调整）
        createReqVO.setUpdateBy(dppEtlNewNodeSaveReqVO.getUpdateBy()); // 假设任务名称为更新者（根据需求调整）
        createReqVO.setUpdateTime(dppEtlNewNodeSaveReqVO.getUpdateTime()); // 设置当前时间为更新时间

        //暂时没意义参数
        createReqVO.setPersonCharge(dppEtlNewNodeSaveReqVO.getPersonCharge());
        createReqVO.setType(dppEtlNewNodeSaveReqVO.getType());
        createReqVO.setTimeout(dppEtlNewNodeSaveReqVO.getTimeout());


        // 返回生成的 DppEtlTaskSaveReqVO
        return createReqVO;
    }

    /**
     * 将 DppEtlNewNodeSaveReqVO 和 ProcessDefinition 转换为 DppEtlTaskSaveReqVO
     *
     * @param dppEtlNewNodeSaveReqVO 外部请求的任务数据
     * @param task                   流程定义数据
     * @return 转换后的 DppEtlTaskSaveReqVO
     */
    public static DppEtlTaskLogSaveReqVO fromDppEtlTaskLogSaveReqVO(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO, DppEtlTaskSaveReqVO task) {
        // 创建 DppEtlTaskSaveReqVO 对象
        DppEtlTaskLogSaveReqVO createReqVO = new DppEtlTaskLogSaveReqVO();
        // 填充任务数据
        createReqVO.setType(dppEtlNewNodeSaveReqVO.getType());
        createReqVO.setName(task.getName()); // 任务名称
        createReqVO.setCode(task.getCode()); // 任务编码
        createReqVO.setVersion(task.getVersion()); // 版本号
        createReqVO.setProjectId(dppEtlNewNodeSaveReqVO.getProjectId()); // 项目ID
        createReqVO.setProjectCode(task.getProjectCode()); // 项目编码
        createReqVO.setDescription(dppEtlNewNodeSaveReqVO.getDescription()); // 描述
        createReqVO.setLocations(task.getLocations()); // 节点坐标信息
        createReqVO.setDsId(task.getId()); // DolphinScheduler的ID

        // 根据 releaseState 设置 status（0: 未上线, 1: 已上线）
        if ("online".equalsIgnoreCase(task.getStatus())) {
            createReqVO.setStatus("1"); // 已上线
        } else if ("offline".equalsIgnoreCase(task.getStatus())) {
            createReqVO.setStatus("0"); // 未上线
        } else {
            createReqVO.setStatus("0"); // 未上线
        }
        createReqVO.setRemark(""); // 默认备注（可根据需要调整）

        createReqVO.setExecutionType(task.getExecutionType());//执行执行策略
        // 填充创建者和更新时间信息
        createReqVO.setCreatorId(dppEtlNewNodeSaveReqVO.getCreatorId()); // 假设项目ID为创建者ID（根据需求调整）
        createReqVO.setCreateBy(dppEtlNewNodeSaveReqVO.getCreateBy()); // 假设任务名称为创建者（根据需求调整）
        createReqVO.setCreateTime(dppEtlNewNodeSaveReqVO.getCreateTime()); // 设置当前时间为创建时间
        createReqVO.setUpdatorId(dppEtlNewNodeSaveReqVO.getUpdatorId()); // 假设项目ID为更新者ID（根据需求调整）
        createReqVO.setUpdateBy(dppEtlNewNodeSaveReqVO.getUpdateBy()); // 假设任务名称为更新者（根据需求调整）
        createReqVO.setUpdateTime(dppEtlNewNodeSaveReqVO.getUpdateTime()); // 设置当前时间为更新时间

        //暂时没意义参数
        createReqVO.setPersonCharge(dppEtlNewNodeSaveReqVO.getPersonCharge());
        createReqVO.setType(dppEtlNewNodeSaveReqVO.getType());
        createReqVO.setTimeout(dppEtlNewNodeSaveReqVO.getTimeout());


        // 返回生成的 DppEtlTaskSaveReqVO
        return createReqVO;
    }

    public static DppEtlTaskLogSaveReqVO fromDppEtlTaskSaveReqVO(DppEtlTaskSaveReqVO dppEtlTaskSaveReqVO) {
        DppEtlTaskLogSaveReqVO logSaveReqVO = new DppEtlTaskLogSaveReqVO();

        // 基本字段直接映射
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


        // 填充创建者和更新时间信息
        logSaveReqVO.setCreatorId(dppEtlTaskSaveReqVO.getProjectId()); // 假设项目ID为创建者ID（根据需求调整）
        logSaveReqVO.setCreateBy(dppEtlTaskSaveReqVO.getName()); // 假设任务名称为创建者（根据需求调整）
        logSaveReqVO.setCreateTime(dppEtlTaskSaveReqVO.getCreateTime()); // 设置当前时间为创建时间
        logSaveReqVO.setUpdatorId(dppEtlTaskSaveReqVO.getProjectId()); // 假设项目ID为更新者ID（根据需求调整）
        logSaveReqVO.setUpdateBy(dppEtlTaskSaveReqVO.getName()); // 假设任务名称为更新者（根据需求调整）
        logSaveReqVO.setUpdateTime(dppEtlTaskSaveReqVO.getUpdateTime()); // 设置当前时间为更新时间

        return logSaveReqVO;
    }


    public static List<DppEtlNodeSaveReqVO> convertToDppEtlNodeSaveReqVOList(ProcessDefinition processDefinition, DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO) {
        List<DppEtlNodeSaveReqVO> resultList = new ArrayList<>();

        //取出入参数的信息
        List<Map<String, Object>> list = JSONUtils.convertTaskDefinitionJson(dppEtlNewNodeSaveReqVO.getTaskDefinitionList());

        // 遍历 ProcessDefinition 中的 taskDefinitionList
        for (TaskDefinition taskDefinition : processDefinition.getTaskDefinitionList()) {
            //获取前端前端封装的节点定义数据
            Map<String, Object> taskDefinitionMap = list.stream().filter(item -> {
                String code = MapUtils.getString(item, "code", "");
                return StringUtils.equals(taskDefinition.getCode(), code);
            }).findFirst().get();
            DppEtlNodeSaveReqVO createReqVO = new DppEtlNodeSaveReqVO();
            // 1. 任务相关信息
            createReqVO.setTaskType(dppEtlNewNodeSaveReqVO.getType());//任务类型
            createReqVO.setType(taskDefinition.getTaskType()); // 节点类型
            createReqVO.setComponentType(String.valueOf(taskDefinitionMap.get("componentType")));//组件类型
            createReqVO.setName(taskDefinition.getName()); // 任务名称
            createReqVO.setCode(String.valueOf(taskDefinition.getCode())); // 任务编码
            createReqVO.setVersion(taskDefinition.getVersion()); // 任务版本
            createReqVO.setProjectId(dppEtlNewNodeSaveReqVO.getProjectId()); // 项目ID
            createReqVO.setProjectCode(String.valueOf(taskDefinition.getProjectCode())); // 项目编码

            createReqVO.setPriority(String.valueOf(taskDefinition.getTaskPriority()));//任务优先级
            createReqVO.setFailRetryTimes((long) taskDefinition.getFailRetryTimes());
            createReqVO.setFailRetryInterval((long) taskDefinition.getFailRetryInterval());
            createReqVO.setTimeout((long) taskDefinition.getTimeout());
            createReqVO.setDelayTime((long) taskDefinition.getDelayTime());
            createReqVO.setCpuQuota((long) taskDefinition.getCpuQuota());
            createReqVO.setMemoryMax((long) taskDefinition.getMemoryMax());
            createReqVO.setDescription(taskDefinition.getDescription());
            createReqVO.setDsId(taskDefinition.getId()); // 将任务的 dsId 设置为节点的 dsId

            createReqVO.setParameters(getTaskParamsAsJson(list, String.valueOf(taskDefinition.getCode()))); // 节点参数

            // 填充创建者和更新时间信息
            createReqVO.setCreatorId(dppEtlNewNodeSaveReqVO.getCreatorId()); // 假设项目ID为创建者ID（根据需求调整）
            createReqVO.setCreateBy(dppEtlNewNodeSaveReqVO.getCreateBy()); // 假设任务名称为创建者（根据需求调整）
            createReqVO.setCreateTime(dppEtlNewNodeSaveReqVO.getCreateTime()); // 设置当前时间为创建时间
            createReqVO.setUpdatorId(dppEtlNewNodeSaveReqVO.getUpdatorId()); // 假设项目ID为更新者ID（根据需求调整）
            createReqVO.setUpdateBy(dppEtlNewNodeSaveReqVO.getUpdateBy()); // 假设任务名称为更新者（根据需求调整）
            createReqVO.setUpdateTime(dppEtlNewNodeSaveReqVO.getUpdateTime()); // 设置当前时间为更新时间

            // 添加到结果列表
            resultList.add(createReqVO);
        }
        return resultList;
    }

    public static String getTaskParamsAsJson(List<Map<String, Object>> list, String code) {
        // 查找匹配的 taskParams
        Optional<Map<String, Object>> matchingTaskParams = list.stream()
                .filter(task -> task != null && StringUtils.equals(code, MapUtils.getString(task, "code")))
                .map(task -> (Map<String, Object>) MapUtils.getObject(task, "taskParams"))
                .filter(taskParams -> taskParams != null)
                .findFirst();

        // 如果找到了匹配的 taskParams，转为 JSON 并返回
        return matchingTaskParams.map(taskParams -> JSONUtils.toJson(taskParams))  // 调用 JSONUtils 转换为 JSON
                .orElse(null);  // 如果没有找到匹配项，返回 null
    }


    public static List<DppEtlNodeLogSaveReqVO> convertToDppEtlNodeLogSaveReqVOList(ProcessDefinition processDefinition, DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO) {
        List<DppEtlNodeLogSaveReqVO> resultList = new ArrayList<>();

        //取出入参数的信息
        List<Map<String, Object>> list = JSONUtils.convertTaskDefinitionJson(dppEtlNewNodeSaveReqVO.getTaskDefinitionList());

        // 遍历 ProcessDefinition 中的 taskDefinitionList
        for (TaskDefinition taskDefinition : processDefinition.getTaskDefinitionList()) {
            //获取前端前端封装的节点定义数据
            Map<String, Object> taskDefinitionMap = list.stream().filter(item -> {
                String code = MapUtils.getString(item, "code", "");
                return StringUtils.equals(taskDefinition.getCode(), code);
            }).findFirst().get();
            DppEtlNodeLogSaveReqVO createReqVO = new DppEtlNodeLogSaveReqVO();

            // 1. 任务相关信息
            createReqVO.setTaskType(dppEtlNewNodeSaveReqVO.getType());//任务类型
            createReqVO.setType(taskDefinition.getTaskType()); // 节点类型
            createReqVO.setComponentType(String.valueOf(taskDefinitionMap.get("componentType")));//组件类型
            createReqVO.setName(taskDefinition.getName()); // 任务名称
            createReqVO.setCode(String.valueOf(taskDefinition.getCode())); // 任务编码
            createReqVO.setVersion((long) taskDefinition.getVersion()); // 任务版本
            createReqVO.setProjectId(dppEtlNewNodeSaveReqVO.getProjectId()); // 项目ID
            createReqVO.setProjectCode(String.valueOf(taskDefinition.getProjectCode())); // 项目编码

            createReqVO.setPriority(String.valueOf(taskDefinition.getTaskPriority()));//任务优先级
            createReqVO.setFailRetryTimes((long) taskDefinition.getFailRetryTimes());
            createReqVO.setFailRetryInterval((long) taskDefinition.getFailRetryInterval());
            createReqVO.setTimeout((long) taskDefinition.getTimeout());
            createReqVO.setDelayTime((long) taskDefinition.getDelayTime());
            createReqVO.setCpuQuota((long) taskDefinition.getCpuQuota());
            createReqVO.setMemoryMax((long) taskDefinition.getMemoryMax());
            createReqVO.setDescription(taskDefinition.getDescription());
            createReqVO.setDsId(taskDefinition.getId()); // 将任务的 dsId 设置为节点的 dsId

            createReqVO.setParameters(getTaskParamsAsJson(list, String.valueOf(taskDefinition.getCode()))); // 节点参数

            // 填充创建者和更新时间信息
            createReqVO.setCreatorId(dppEtlNewNodeSaveReqVO.getCreatorId()); // 假设项目ID为创建者ID（根据需求调整）
            createReqVO.setCreateBy(dppEtlNewNodeSaveReqVO.getCreateBy()); // 假设任务名称为创建者（根据需求调整）
            createReqVO.setCreateTime(dppEtlNewNodeSaveReqVO.getCreateTime()); // 设置当前时间为创建时间
            createReqVO.setUpdatorId(dppEtlNewNodeSaveReqVO.getUpdatorId()); // 假设项目ID为更新者ID（根据需求调整）
            createReqVO.setUpdateBy(dppEtlNewNodeSaveReqVO.getUpdateBy()); // 假设任务名称为更新者（根据需求调整）
            createReqVO.setUpdateTime(dppEtlNewNodeSaveReqVO.getUpdateTime()); // 设置当前时间为更新时间

            // 添加到结果列表
            resultList.add(createReqVO);
        }
        return resultList;
    }

    public static long getIdByCode(List<DppEtlNodeDO> dppEtlNodeDOList, String code, long preTaskVersion) {
        return dppEtlNodeDOList.stream()
                .filter(task -> StringUtils.equals(task.getCode(), code) && task.getVersion() == preTaskVersion)  // 匹配 code
                .map(DppEtlNodeDO::getId)  // 获取对应的 id
                .findFirst()  // 如果找到匹配项，返回第一个
                .orElse(-1L);  // 如果没有找到，返回默认值 -1
    }

    public static List<DppEtlTaskNodeRelSaveReqVO> convertToDppEtlTaskNodeRelSaveReqVOList(ProcessDefinition data, DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO, List<DppEtlNodeDO> dppEtlNodeBatch, DppEtlTaskSaveReqVO dppEtlTaskSaveReqVO) {
        List<DppEtlTaskNodeRelSaveReqVO> resultList = new ArrayList<>();

        // 遍历 data 下的 taskRelationList，生成 DppEtlTaskNodeRelSaveReqVO
        for (ProcessTaskRelation taskRelation : data.getTaskRelationList()) {
            DppEtlTaskNodeRelSaveReqVO taskNodeRelSaveReqVO = new DppEtlTaskNodeRelSaveReqVO();

            // 1. 填充任务节点关系相关字段
            taskNodeRelSaveReqVO.setProjectId(dppEtlNewNodeSaveReqVO.getProjectId()); // 项目ID
            taskNodeRelSaveReqVO.setProjectCode(String.valueOf(dppEtlNewNodeSaveReqVO.getProjectCode())); // 项目编码

            // 任务相关字段
            taskNodeRelSaveReqVO.setTaskId(dppEtlTaskSaveReqVO.getId()); // 任务ID
            taskNodeRelSaveReqVO.setTaskCode(String.valueOf(data.getCode())); // 任务编码
            taskNodeRelSaveReqVO.setTaskVersion(data.getVersion()); // 任务版本

            // 前节点相关字段
            taskNodeRelSaveReqVO.setPreNodeId(getIdByCode(dppEtlNodeBatch, String.valueOf(taskRelation.getPreTaskCode()), taskRelation.getPreTaskVersion())); // 前节点ID
            taskNodeRelSaveReqVO.setPreNodeCode(String.valueOf(taskRelation.getPreTaskCode())); // 前节点编码
            taskNodeRelSaveReqVO.setPreNodeVersion(taskRelation.getPreTaskVersion()); // 前节点版本

            // 后节点相关字段
            taskNodeRelSaveReqVO.setPostNodeId(getIdByCode(dppEtlNodeBatch, String.valueOf(data.getCode()), taskRelation.getPreTaskVersion())); // 后节点ID
            taskNodeRelSaveReqVO.setPostNodeCode(String.valueOf(taskRelation.getPostTaskCode())); // 后节点编码
            taskNodeRelSaveReqVO.setPostNodeVersion(taskRelation.getPostTaskVersion()); // 后节点版本

            // 可选字段
            taskNodeRelSaveReqVO.setRemark(null); // 备注

            // 2. 填充新增/修改相关信息
            taskNodeRelSaveReqVO.setCreatorId(dppEtlNewNodeSaveReqVO.getCreatorId()); // 假设项目ID为创建者ID（根据需求调整）
            taskNodeRelSaveReqVO.setCreateBy(dppEtlNewNodeSaveReqVO.getCreateBy()); // 假设任务名称为创建者（根据需求调整）
            taskNodeRelSaveReqVO.setCreateTime(dppEtlNewNodeSaveReqVO.getCreateTime()); // 设置当前时间为创建时间
            taskNodeRelSaveReqVO.setUpdatorId(dppEtlNewNodeSaveReqVO.getUpdatorId()); // 假设项目ID为更新者ID（根据需求调整）
            taskNodeRelSaveReqVO.setUpdateBy(dppEtlNewNodeSaveReqVO.getUpdateBy()); // 假设任务名称为更新者（根据需求调整）
            taskNodeRelSaveReqVO.setUpdateTime(dppEtlNewNodeSaveReqVO.getUpdateTime()); // 设置当前时间为更新时间

            // 添加到结果列表
            resultList.add(taskNodeRelSaveReqVO);
        }

        return resultList;
    }


    public static List<DppEtlTaskNodeRelLogSaveReqVO> convertToDppEtlTaskNodeRelLogSaveReqVOList(ProcessDefinition data, DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO, List<DppEtlNodeLogDO> dppEtlNodeBatch, DppEtlTaskLogSaveReqVO dppEtlTaskSaveReqVO) {
        List<DppEtlTaskNodeRelLogSaveReqVO> resultList = new ArrayList<>();

        // 遍历 data 下的 taskRelationList，生成 DppEtlTaskNodeRelSaveReqVO
        for (ProcessTaskRelation taskRelation : data.getTaskRelationList()) {
            DppEtlTaskNodeRelLogSaveReqVO taskNodeRelSaveReqVO = new DppEtlTaskNodeRelLogSaveReqVO();

            // 1. 填充任务节点关系相关字段
            taskNodeRelSaveReqVO.setProjectId(dppEtlNewNodeSaveReqVO.getProjectId()); // 项目ID
            taskNodeRelSaveReqVO.setProjectCode(String.valueOf(dppEtlNewNodeSaveReqVO.getProjectCode())); // 项目编码

            // 任务相关字段
            taskNodeRelSaveReqVO.setTaskId(dppEtlTaskSaveReqVO.getId()); // 任务ID
            taskNodeRelSaveReqVO.setTaskCode(String.valueOf(data.getCode())); // 任务编码
            taskNodeRelSaveReqVO.setTaskVersion(data.getVersion()); // 任务版本

            // 前节点相关字段
            taskNodeRelSaveReqVO.setPreNodeId(getDppEtlNodeLogDOIdByCode(dppEtlNodeBatch, String.valueOf(taskRelation.getPreTaskCode()), taskRelation.getPreTaskVersion())); // 前节点ID
            taskNodeRelSaveReqVO.setPreNodeCode(String.valueOf(taskRelation.getPreTaskCode())); // 前节点编码
            taskNodeRelSaveReqVO.setPreNodeVersion(taskRelation.getPreTaskVersion()); // 前节点版本

            // 后节点相关字段
            taskNodeRelSaveReqVO.setPostNodeId(getDppEtlNodeLogDOIdByCode(dppEtlNodeBatch, String.valueOf(data.getCode()), taskRelation.getPreTaskVersion())); // 后节点ID
            taskNodeRelSaveReqVO.setPostNodeCode(String.valueOf(taskRelation.getPostTaskCode())); // 后节点编码
            taskNodeRelSaveReqVO.setPostNodeVersion(taskRelation.getPostTaskVersion()); // 后节点版本

            // 可选字段
            taskNodeRelSaveReqVO.setRemark(null); // 备注

            // 2. 填充新增/修改相关信息
            taskNodeRelSaveReqVO.setCreatorId(dppEtlNewNodeSaveReqVO.getCreatorId()); // 假设项目ID为创建者ID（根据需求调整）
            taskNodeRelSaveReqVO.setCreateBy(dppEtlNewNodeSaveReqVO.getCreateBy()); // 假设任务名称为创建者（根据需求调整）
            taskNodeRelSaveReqVO.setCreateTime(dppEtlNewNodeSaveReqVO.getCreateTime()); // 设置当前时间为创建时间
            taskNodeRelSaveReqVO.setUpdatorId(dppEtlNewNodeSaveReqVO.getUpdatorId()); // 假设项目ID为更新者ID（根据需求调整）
            taskNodeRelSaveReqVO.setUpdateBy(dppEtlNewNodeSaveReqVO.getUpdateBy()); // 假设任务名称为更新者（根据需求调整）
            taskNodeRelSaveReqVO.setUpdateTime(dppEtlNewNodeSaveReqVO.getUpdateTime()); // 设置当前时间为更新时间

            // 添加到结果列表
            resultList.add(taskNodeRelSaveReqVO);
        }

        return resultList;
    }


    public static long getDppEtlNodeLogDOIdByCode(List<DppEtlNodeLogDO> dppEtlNodeDOList, String code, long preTaskVersion) {
        return dppEtlNodeDOList.stream()
                .filter(task -> StringUtils.equals(task.getCode(), code) && task.getVersion() == preTaskVersion)  // 匹配 code
                .map(DppEtlNodeLogDO::getId)  // 获取对应的 id
                .findFirst()  // 如果找到匹配项，返回第一个
                .orElse(-1L);  // 如果没有找到，返回默认值 -1
    }


    /**
     * 工具方法，生成 DsSchedulerSaveReqDTO。
     *
     * @param crontab               Cron 表达式
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
     * 将 DsSchedulerRespDTO 转换为 DppEtlSchedulerSaveReqVO
     *
     * @param dsSchedulerRespDTO DsSchedulerRespDTO
     * @param dppEtlTaskDO
     * @return DppEtlSchedulerSaveReqVO
     */
    public static DppEtlSchedulerSaveReqVO convertToDppEtlSchedulerSaveReqVO(DsSchedulerRespDTO dsSchedulerRespDTO, DppEtlTaskDO dppEtlTaskDO) {
        // 创建 DppEtlSchedulerSaveReqVO 对象
        DppEtlSchedulerSaveReqVO reqVO = new DppEtlSchedulerSaveReqVO();

        // 从 dsSchedulerRespDTO 中提取数据并填充 reqVO
        Schedule schedule = dsSchedulerRespDTO.getData();

        reqVO.setStartTime(schedule.getStartTime());
        reqVO.setEndTime(schedule.getEndTime());
        reqVO.setTimezoneId(schedule.getTimezoneId());
        reqVO.setCronExpression(schedule.getCrontab());
        reqVO.setFailureStrategy("1");

        // 可以根据需要填写默认值或处理 dsId 和备注等字段
        reqVO.setDsId(schedule.getId()); // 假设 dsId 和 id 相同
        reqVO.setRemark(null); // 备注可以根据实际需求进行修改

        return reqVO;
    }


    /**
     * 工具方法，生成 DsSchedulerUpdateReqDTO。
     *
     * @param id                    调度ID
     * @param crontab               Cron 表达式
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


    /**
     * 将 DsSchedulerSaveReqDTO 转换为 DppEtlSchedulerSaveReqVO
     *
     * @param dppEtlNewNodeSaveReqVO
     * @return DppEtlSchedulerSaveReqVO
     */
    public static DppEtlSchedulerSaveReqVO convertToDppEtlSchedulerSaveReqVO(Long taskId, String taskCode, DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO) {
        // 创建 DppEtlSchedulerSaveReqVO 对象
        DppEtlSchedulerSaveReqVO reqVO = new DppEtlSchedulerSaveReqVO();

        // 直接从 dsSchedulerSaveReqDTO 中填充字段
        reqVO.setTaskId(taskId);
        reqVO.setTaskCode(taskCode);

        // 获取当前时间
        String startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        // 获取100年后的时间
        long currentTime = System.currentTimeMillis();
        Date date = new Date(currentTime + 100L * 365 * 24 * 60 * 60 * 1000);

        reqVO.setStartTime(new Date());
        reqVO.setEndTime(date);
        reqVO.setTimezoneId("Asia/Shanghai"); // 默认时区

        reqVO.setCronExpression(dppEtlNewNodeSaveReqVO.getCrontab());
        reqVO.setFailureStrategy("1");
        reqVO.setStatus("0");


        // 填充dsId，假设dsId与ID相同
        reqVO.setDsId((long) -1);

        // 备注可以根据需求填写

        return reqVO;
    }

    // 方法解析 schedule 字段中的时间部分
    private static Date parseStartTime(String scheduleJson) {
        // 提取并解析开始时间 (假设你有方法从 JSON 解析出来)
        return new Date(); // 示例，实际需要提取出对应的时间
    }

    private static Date parseEndTime(String scheduleJson) {
        // 提取并解析结束时间 (假设你有方法从 JSON 解析出来)
        return new Date(); // 示例，实际需要提取出对应的时间
    }

    public static List<String> getPreAndPostNodeCodeList(List<DppEtlTaskNodeRelRespVO> dppEtlTaskNodeRelRespVOList) {
        List<String> result = new ArrayList<>();
        for (DppEtlTaskNodeRelRespVO vo : dppEtlTaskNodeRelRespVOList) {
            result.add(vo.getPreNodeCode());  // 添加 preNodeCode
            result.add(vo.getPostNodeCode()); // 添加 postNodeCode
        }
        return result;  // 返回 List<String>
    }


    public static DsStartTaskReqDTO createDsStartTaskReqDTO(String processDefinitionCode) {
        // 获取当前日期，格式为 "yyyy-MM-dd"
        String currentDate = DateUtil.today();
        // 构造 scheduleTime 字段，固定格式 "yyyy-MM-dd 00:00:00"
        String scheduleTime = String.format("{\"complementStartDate\":\"%s 00:00:00\",\"complementEndDate\":\"%s 00:00:00\"}", currentDate, currentDate);

        // 使用 builder 模式创建 DsStartTaskReqDTO 对象，其他字段均为写死的值
        return DsStartTaskReqDTO.builder()
                .processDefinitionCode(JSONUtils.convertToLong(processDefinitionCode))
                .failureStrategy("CONTINUE")
                .warningType(DEFAULT_CONDITION_TYPE)
                .processInstancePriority(DEFAULT_TASK_PRIORITY)
                .scheduleTime(scheduleTime)
                .build();
    }


    /**
     * 构建etl坐标信息数据
     *
     * @param locations
     * @param code
     * @return
     */
    public static String buildEtlTaskLocationsJson(List<Map<String, Object>> locations, String code) {
        List<Map<String, Object>> locationList = new ArrayList<>();

        Map<String, Object> location = locations.get(0);
        Map<String, Object> locationMap = new HashMap<>();
        // 填充必要字段
        locationMap.put("taskCode", Long.parseLong(code)); // 默认 taskCode 为 0
        locationMap.put("x", location.getOrDefault("x", 0)); // 默认 x 为 0
        locationMap.put("y", location.getOrDefault("y", 0)); // 默认 y 为 0
        locationList.add(locationMap);
        return JSON.toJSONString(locationList);
    }

    /**
     * 构建etl节点关系json数据
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
     * 构建etl节点定义json数据
     *
     * @param id        etl节点id
     * @param name      etl节点美年广场
     * @param code      etl节点编码
     * @param version   etl节点版本
     * @param mainArgs  etl节点参数
     * @param draftJson
     * @return
     */
    public static String buildEtlTaskDefinitionJson(Long id, String name, String code, Integer version, Map<String, Object> mainArgs, String draftJson) {
        List<Map<String, Object>> result = new ArrayList<>();
        //自定义参数
        Map<String, Object> definitionJsonMap = JSONUtils.convertTaskDefinitionJsonMap(draftJson);

        // 处理每个 task 的默认值和必要字段
        Map<String, Object> taskMap = new HashMap<>();

        // 设置基本信息字段
        taskMap.put("id", id); // 默认 id 为 0
        taskMap.put("name", name); // 默认空字符串
        taskMap.put("code", code); // 默认 code 为 0L
        taskMap.put("version", version); // 默认版本号为1
        taskMap.put("description", ""); // 默认描述为空
        taskMap.put("workerGroup", MapUtils.getObject(definitionJsonMap,"workerGroup",DEFAULT_WORKER_GROUP) ); // 默认 workerGroup 为 "default"
        taskMap.put("environmentCode", DEFAULT_ENVIRONMENT_CODE); // 默认环境编码
        taskMap.put("flag", DEFAULT_FLAG); // 默认 flag 为 "YES"
        taskMap.put("isCache", DEFAULT_IS_CACHE); // 默认 isCache 为 "NO"
        taskMap.put("taskPriority", MapUtils.getObject(definitionJsonMap,"taskPriority",DEFAULT_TASK_PRIORITY)); // 默认任务优先级为 "MEDIUM"
        taskMap.put("taskType", DEFAULT_TASK_TYPE); // 默认任务类型为 "SPARK"
        taskMap.put("taskExecuteType", "BATCH");

        //2025-06-25 新增配置项默认值
        taskMap.put("failRetryTimes", MapUtils.getObject(definitionJsonMap,"failRetryTimes",DEFAULT_TASK_failRetryTimes));
        taskMap.put("delayTime", MapUtils.getObject(definitionJsonMap,"delayTime",DEFAULT_TASK_delayTime));
        taskMap.put("failRetryInterval", MapUtils.getObject(definitionJsonMap,"failRetryInterval",DEFAULT_TASK_failRetryInterval));

        Map<String, Object> taskParams = new LinkedHashMap<>();

        taskParams.put("localParams", new ArrayList<>()); // 默认空列表
        taskParams.put("rawScript", ""); // 默认空字符串
        taskParams.put("resourceList", new ArrayList<>()); // 默认空列表
        taskParams.put("programType", DEFAULT_PROGRAM_TYPE); // 默认程序类型为 "JAVA"
        taskParams.put("mainClass", defaultMainClass);

        // mainJar是Map，且resourceName字段为默认值
        Map<String, Object> mainJar = new HashMap<>();
        mainJar.put("resourceName", resourceName);
        taskParams.put("mainJar", mainJar);
        taskParams.put("deployMode", DEFAULT_DEPLOY_MODE); // 默认部署模式为 "client"
        taskParams.put("mainArgs", Base64.encode(JSON.toJSONString(mainArgs))); // 默认空字符串
        taskParams.put("master", defaultMaster); // 默认Spark master URL
        taskParams.put("driverCores",MapUtils.getObject(definitionJsonMap,"driverCores",DEFAULT_DRIVER_CORES) ); // 默认驱动核心数
        taskParams.put("driverMemory",MapUtils.getObject(definitionJsonMap,"driverMemory",DEFAULT_DRIVER_MEMORY) ); // 默认驱动内存
        taskParams.put("numExecutors", MapUtils.getObject(definitionJsonMap,"numExecutors",DEFAULT_NUM_EXECUTORS)); // 默认执行器数量
        taskParams.put("executorMemory",MapUtils.getObject(definitionJsonMap,"executorMemory",DEFAULT_EXECUTOR_MEMORY) ); // 默认执行器内存
        taskParams.put("executorCores",MapUtils.getObject(definitionJsonMap,"executorCores",DEFAULT_EXECUTOR_CORES) ); // 默认执行器核心数
        taskParams.put("yarnQueue",MapUtils.getObject(definitionJsonMap,"yarnQueue","") ); // 默认执行器核心数
        taskParams.put("sqlExecutionType", DEFAULT_SQL_EXECUTION_TYPE); // 默认SQL执行类型为 "SCRIPT"

        // 将任务的taskParams加入到taskMap中
        taskMap.put("taskParams", taskParams);

        // 将填充好的任务加入到结果列表
        result.add(taskMap);
        // 返回处理后的JSON字符串
        return JSON.toJSONString(result);
    }

    public static List<DppEtlNodeSaveReqVO> convertToDppEtlNodeSaveReqVOList(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO, String taskDefinitionJson) {
        List<DppEtlNodeSaveReqVO> resultList = new ArrayList<>();

        //取出入参数的信息
        List<DppEtlNodeSaveReqVO> list = JSON.parseArray(taskDefinitionJson, DppEtlNodeSaveReqVO.class);

        // 遍历 ProcessDefinition 中的 taskDefinitionList
        for (DppEtlNodeSaveReqVO createReqVO : list) {
            // 1. 任务相关信息
            createReqVO.setType(createReqVO.getTaskType());//节点类型
            createReqVO.setTaskType(dppEtlNewNodeSaveReqVO.getType());//任务类型
            createReqVO.setVersion(1); // 任务版本
            createReqVO.setProjectId(dppEtlNewNodeSaveReqVO.getProjectId()); // 项目ID
            createReqVO.setProjectCode(String.valueOf(dppEtlNewNodeSaveReqVO.getProjectCode())); // 项目编码
            // 填充创建者和更新时间信息
            createReqVO.setCreatorId(dppEtlNewNodeSaveReqVO.getCreatorId()); // 假设项目ID为创建者ID（根据需求调整）
            createReqVO.setCreateBy(dppEtlNewNodeSaveReqVO.getCreateBy()); // 假设任务名称为创建者（根据需求调整）
            createReqVO.setCreateTime(dppEtlNewNodeSaveReqVO.getCreateTime()); // 设置当前时间为创建时间
            createReqVO.setUpdatorId(dppEtlNewNodeSaveReqVO.getUpdatorId()); // 假设项目ID为更新者ID（根据需求调整）
            createReqVO.setUpdateBy(dppEtlNewNodeSaveReqVO.getUpdateBy()); // 假设任务名称为更新者（根据需求调整）
            createReqVO.setUpdateTime(dppEtlNewNodeSaveReqVO.getUpdateTime()); // 设置当前时间为更新时间
            createReqVO.setParameters(JSON.toJSONString(createReqVO.getTaskParams()));
            // 添加到结果列表
            resultList.add(createReqVO);
        }
        return resultList;
    }

    public static List<DppEtlNodeSaveReqVO> convertToDppEtlNodeSaveReqVOList(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO, Integer nodeVersion) {
        List<DppEtlNodeSaveReqVO> resultList = new ArrayList<>();
        //取出入参数的信息
        List<DppEtlNodeSaveReqVO> list = JSON.parseArray(dppEtlNewNodeSaveReqVO.getTaskDefinitionList(), DppEtlNodeSaveReqVO.class);

        // 遍历 ProcessDefinition 中的 taskDefinitionList
        for (DppEtlNodeSaveReqVO createReqVO : list) {
            // 1. 任务相关信息
            createReqVO.setType(createReqVO.getTaskType());//节点类型
            createReqVO.setTaskType(dppEtlNewNodeSaveReqVO.getType());//任务类型
            createReqVO.setProjectId(dppEtlNewNodeSaveReqVO.getProjectId()); // 项目ID
            createReqVO.setProjectCode(String.valueOf(dppEtlNewNodeSaveReqVO.getProjectCode())); // 项目编码
            // 填充创建者和更新时间信息
            createReqVO.setCreatorId(dppEtlNewNodeSaveReqVO.getCreatorId()); // 假设项目ID为创建者ID（根据需求调整）
            createReqVO.setCreateBy(dppEtlNewNodeSaveReqVO.getCreateBy()); // 假设任务名称为创建者（根据需求调整）
            createReqVO.setCreateTime(dppEtlNewNodeSaveReqVO.getCreateTime()); // 设置当前时间为创建时间
            createReqVO.setUpdatorId(dppEtlNewNodeSaveReqVO.getUpdatorId()); // 假设项目ID为更新者ID（根据需求调整）
            createReqVO.setUpdateBy(dppEtlNewNodeSaveReqVO.getUpdateBy()); // 假设任务名称为更新者（根据需求调整）
            createReqVO.setUpdateTime(dppEtlNewNodeSaveReqVO.getUpdateTime()); // 设置当前时间为更新时间
            createReqVO.setParameters(JSON.toJSONString(createReqVO.getTaskParams()));
            // 添加到结果列表
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
        // 遍历 data 下的 taskRelationList，生成 DppEtlTaskNodeRelSaveReqVO
        for (ProcessTaskRelation taskRelation : list) {

            DppEtlTaskNodeRelSaveReqVO taskNodeRelSaveReqVO = new DppEtlTaskNodeRelSaveReqVO();

            // 1. 填充任务节点关系相关字段
            taskNodeRelSaveReqVO.setProjectId(dppEtlNewNodeSaveReqVO.getProjectId()); // 项目ID
            taskNodeRelSaveReqVO.setProjectCode(String.valueOf(dppEtlNewNodeSaveReqVO.getProjectCode())); // 项目编码

            // 任务相关字段
            taskNodeRelSaveReqVO.setTaskId(dppEtlTaskSaveReqVO.getId()); // 任务ID
            taskNodeRelSaveReqVO.setTaskCode(dppEtlTaskSaveReqVO.getCode()); // 任务编码
            taskNodeRelSaveReqVO.setTaskVersion(dppEtlTaskSaveReqVO.getVersion()); // 任务版本

            // 前节点相关字段
            taskNodeRelSaveReqVO.setPreNodeCode(String.valueOf(taskRelation.getPreTaskCode())); // 前节点编码
            taskNodeRelSaveReqVO.setPreNodeVersion(taskRelation.getPreTaskVersion()); // 前节点版本
            if (StringUtils.isNotEmpty(taskNodeRelSaveReqVO.getPreNodeCode()) && taskNodeRelSaveReqVO.getPreNodeVersion() == 0) {
                taskNodeRelSaveReqVO.setPreNodeVersion(1);
            }
            taskNodeRelSaveReqVO.setPreNodeId(getIdByCode(dppEtlNodeBatch, taskNodeRelSaveReqVO.getPreNodeCode(), taskNodeRelSaveReqVO.getPreNodeVersion())); // 前节点ID

            // 后节点相关字段
            taskNodeRelSaveReqVO.setPostNodeCode(String.valueOf(taskRelation.getPostTaskCode())); // 后节点编码
            taskNodeRelSaveReqVO.setPostNodeVersion(taskRelation.getPostTaskVersion()); // 后节点版本
            if (StringUtils.isNotEmpty(taskNodeRelSaveReqVO.getPostNodeCode()) && taskNodeRelSaveReqVO.getPostNodeVersion() == 0) {
                taskNodeRelSaveReqVO.setPostNodeVersion(1);
            }
            taskNodeRelSaveReqVO.setPostNodeId(getIdByCode(dppEtlNodeBatch, taskNodeRelSaveReqVO.getPostNodeCode(), taskNodeRelSaveReqVO.getPostNodeVersion())); // 前节点ID

            // 可选字段
            taskNodeRelSaveReqVO.setRemark(null); // 备注

            // 2. 填充新增/修改相关信息
            taskNodeRelSaveReqVO.setCreatorId(dppEtlNewNodeSaveReqVO.getCreatorId()); // 假设项目ID为创建者ID（根据需求调整）
            taskNodeRelSaveReqVO.setCreateBy(dppEtlNewNodeSaveReqVO.getCreateBy()); // 假设任务名称为创建者（根据需求调整）
            taskNodeRelSaveReqVO.setCreateTime(dppEtlNewNodeSaveReqVO.getCreateTime()); // 设置当前时间为创建时间
            taskNodeRelSaveReqVO.setUpdatorId(dppEtlNewNodeSaveReqVO.getUpdatorId()); // 假设项目ID为更新者ID（根据需求调整）
            taskNodeRelSaveReqVO.setUpdateBy(dppEtlNewNodeSaveReqVO.getUpdateBy()); // 假设任务名称为更新者（根据需求调整）
            taskNodeRelSaveReqVO.setUpdateTime(dppEtlNewNodeSaveReqVO.getUpdateTime()); // 设置当前时间为更新时间

            // 添加到结果列表
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
     * 构建etl参数数据
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
            //组件类型 本方法含有 DB_READER、EXCEL_READER、CSV_READER、SPARK_CLEAN、DB_WRITER
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
                    transitionList.add(data);
                    break;
                case DB_WRITER:
                    result.put("writer", data);
                    break;
            }
        }
        //配置config
        Map<String, Object> config = new HashMap<>();
        config.put("taskInfo", taskInfo);
        config.put("rabbitmq", rabbitmqConfig);
        config.put("resourceUrl", resourceUrl);
        result.put("transition", transitionList);
        result.put("config", config);
        return result;
    }
}
