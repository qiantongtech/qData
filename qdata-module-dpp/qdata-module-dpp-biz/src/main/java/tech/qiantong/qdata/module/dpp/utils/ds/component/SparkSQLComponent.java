package tech.qiantong.qdata.module.dpp.utils.ds.component;

import tech.qiantong.qdata.common.enums.TaskComponentTypeEnum;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <P>
 * 用途:sparksql开发 组件
 * </p>
 **/
public class SparkSQLComponent implements ComponentItem {

    /**
     *
     * taskParams SPARK（sparksql开发）
     * {
     *     "localParams": [],//默认 []
     *     "rawScript": "脚本",//脚本
     *     "resourceList": [],//默认 []
     *     "programType": "SQL",//默认"SQL"
     *     "mainClass": "",//默认 ""
     *     "deployMode": "client",//默认 "client"
     *     "yarnQueue":"",//默认 ""
     *     "master": "",//默认""
     *     "driverCores": 1,//默认 1
     *     "driverMemory": "512M",//默认 "512M"
     *     "numExecutors": 1,//默认 1
     *     "executorMemory": "1G",//默认 1G
     *     "executorCores": 1,//默认 1
     *     "sqlExecutionType": "SCRIPT"//默认"SCRIPT"
     * }
     *
     * @param params
     * @return
     */
    @Override
    public Map<String, Object> parse(Map<String, Object> params) {
        Map<String, Object> taskParams = new LinkedHashMap<>();

        taskParams.put("localParams", params.getOrDefault("localParams", new ArrayList<>())); // 默认空列表
        taskParams.put("rawScript", params.getOrDefault("rawScript", "")); // 默认空字符串
        taskParams.put("resourceList", params.getOrDefault("resourceList", new ArrayList<>())); // 默认空列表
        taskParams.put("programType", params.getOrDefault("programType", DEFAULT_PROGRAM_TYPE)); // 默认程序类型为 "JAVA"
        taskParams.put("mainClass", params.get("mainClass")); // 默认主类
        taskParams.put("deployMode", params.getOrDefault("deployMode", DEFAULT_DEPLOY_MODE)); // 默认部署模式为 "client"
        taskParams.put("yarnQueue", params.getOrDefault("yarnQueue", "")); // 默认空字符串
        taskParams.put("master", params.get("master")); // 默认Spark master URL
        taskParams.put("driverCores", params.getOrDefault("driverCores", DEFAULT_DRIVER_CORES)); // 默认驱动核心数
        taskParams.put("driverMemory", params.getOrDefault("driverMemory", DEFAULT_DRIVER_MEMORY)); // 默认驱动内存
        taskParams.put("numExecutors", params.getOrDefault("numExecutors", DEFAULT_NUM_EXECUTORS)); // 默认执行器数量
        taskParams.put("executorMemory", params.getOrDefault("executorMemory", DEFAULT_EXECUTOR_MEMORY)); // 默认执行器内存
        taskParams.put("executorCores", params.getOrDefault("executorCores", DEFAULT_EXECUTOR_CORES)); // 默认执行器核心数
        taskParams.put("sqlExecutionType", params.getOrDefault("sqlExecutionType", DEFAULT_SQL_EXECUTION_TYPE)); // 默认SQL执行类型为 "SCRIPT"
        return taskParams;
    }

    @Override
    public String code() {
        return TaskComponentTypeEnum.SPARK_SQL_DEV.getCode();
    }
}
