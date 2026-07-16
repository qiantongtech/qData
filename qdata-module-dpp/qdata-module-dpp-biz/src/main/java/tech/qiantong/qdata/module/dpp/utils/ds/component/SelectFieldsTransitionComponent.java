package tech.qiantong.qdata.module.dpp.utils.ds.component;

import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson2.JSON;
import tech.qiantong.qdata.common.enums.TaskComponentTypeEnum;
import tech.qiantong.qdata.module.dpp.utils.model.DsResource;

import java.util.*;

/**
 * <P>
 * Purpose: Field selection component
 * </p>
 *
 * @author: FXB
 * @create: 2025-03-12 16:31
 **/
public class SelectFieldsTransitionComponent implements ComponentItem {
    @Override
    public Map<String, Object> parse(Map<String, Object> params) {
        Map<String, Object> taskParams = new LinkedHashMap<>();

        taskParams.put("localParams", params.getOrDefault("localParams", new ArrayList<>())); // Default empty list
        taskParams.put("rawScript", params.getOrDefault("rawScript", "")); // Default empty string
        taskParams.put("resourceList", params.getOrDefault("resourceList", new ArrayList<>())); // Default empty list
        taskParams.put("programType", params.getOrDefault("programType", DEFAULT_PROGRAM_TYPE)); // Default program type is "JAVA"
        taskParams.put("mainClass", params.get("mainClass")); // Default main class

        // mainJar is a Map, and the resourceName field is the default value
        Map<String, Object> mainJar = new HashMap<>();
        mainJar.put("resourceName", params.get("resourceName"));
        taskParams.put("mainJar", mainJar);

        taskParams.put("deployMode", params.getOrDefault("deployMode", DEFAULT_DEPLOY_MODE)); // Default deploy mode is "client"
        taskParams.put("mainArgs", Base64.encode(JSON.toJSONString(params.getOrDefault("mainArgs", new HashMap<>())))); // Default empty string
        taskParams.put("master", params.get("master")); // Default Spark master URL
        taskParams.put("driverCores", params.getOrDefault("driverCores", DEFAULT_DRIVER_CORES)); // Default driver cores
        taskParams.put("driverMemory", params.getOrDefault("driverMemory", DEFAULT_DRIVER_MEMORY)); // Default driver memory
        taskParams.put("numExecutors", params.getOrDefault("numExecutors", DEFAULT_NUM_EXECUTORS)); // Default number of executors
        taskParams.put("executorMemory", params.getOrDefault("executorMemory", DEFAULT_EXECUTOR_MEMORY)); // Default executor memory
        taskParams.put("executorCores", params.getOrDefault("executorCores", DEFAULT_EXECUTOR_CORES)); // Default executor cores
        taskParams.put("sqlExecutionType", params.getOrDefault("sqlExecutionType", DEFAULT_SQL_EXECUTION_TYPE)); // Default SQL execution type is "SCRIPT"
        return taskParams;
    }

    @Override
    public String code() {
        return TaskComponentTypeEnum.SELECT_FIELDS.getCode();
    }

    @Override
    public Map<String, Object> parse2(String nodeCode, Integer nodeVersion, TaskComponentTypeEnum componentType, Map<String, Object> taskParams, String resourceUrl, List<DsResource> resourceList) {
        // reader configuration
        Map<String, Object> reader = mapNode(nodeCode, nodeVersion, componentType);
        // parameters
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("tableFields",taskParams.get("tableFields"));
        parameter.put("removeFields",taskParams.get("removeFields"));
        reader.put("parameter", parameter);
        return reader;
    }

}
