package tech.qiantong.qdata.module.dpp.utils.ds.component;

import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson2.JSON;
import org.apache.commons.collections4.MapUtils;
import tech.qiantong.qdata.common.enums.TaskComponentTypeEnum;
import tech.qiantong.qdata.common.enums.etl.transition.FieldDerivationTypeEnum;
import tech.qiantong.qdata.module.dpp.utils.model.DsResource;

import java.util.*;

/**
 * <P>
 * 用途:spark清洗组件
 * </p>
 *
 * @author: FXB
 * @create: 2025-03-12 16:31
 **/
public class FieldDerivationTransitionComponent implements ComponentItem {
    @Override
    public Map<String, Object> parse(Map<String, Object> params) {
        Map<String, Object> taskParams = new LinkedHashMap<>();

        taskParams.put("localParams", params.getOrDefault("localParams", new ArrayList<>())); // 默认空列表
        taskParams.put("rawScript", params.getOrDefault("rawScript", "")); // 默认空字符串
        taskParams.put("resourceList", params.getOrDefault("resourceList", new ArrayList<>())); // 默认空列表
        taskParams.put("programType", params.getOrDefault("programType", DEFAULT_PROGRAM_TYPE)); // 默认程序类型为 "JAVA"
        taskParams.put("mainClass", params.get("mainClass")); // 默认主类

        // mainJar是Map，且resourceName字段为默认值
        Map<String, Object> mainJar = new HashMap<>();
        mainJar.put("resourceName", params.get("resourceName"));
        taskParams.put("mainJar", mainJar);

        taskParams.put("deployMode", params.getOrDefault("deployMode", DEFAULT_DEPLOY_MODE)); // 默认部署模式为 "client"
        taskParams.put("mainArgs", Base64.encode(JSON.toJSONString(params.getOrDefault("mainArgs", new HashMap<>())))); // 默认空字符串
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
        return TaskComponentTypeEnum.FIELD_DERIVATION.getCode();
    }

    @Override
    public Map<String, Object> parse2(String nodeCode, Integer nodeVersion, TaskComponentTypeEnum componentType, Map<String, Object> taskParams, String resourceUrl, List<DsResource> resourceList) {
        // reader 配置
        Map<String, Object> reader = new HashMap<>();
        reader.put("nodeCode", nodeCode);
        reader.put("nodeVersion", nodeVersion);
        reader.put("componentType", componentType.getCode());


        String fieldDerivationType = MapUtils.getString(taskParams,"fieldDerivationType");
        FieldDerivationTypeEnum typeEnum = FieldDerivationTypeEnum.fromCode(fieldDerivationType);
        //参数
        Map<String, Object> parameter = new HashMap<>();

        switch (typeEnum) {
            case FIELD_DERIVE_CONCAT:
                // 拼接处理逻辑
                parameter = handleConcat(taskParams);
                break;
            case FIELD_DERIVE_SUBSTRING:
                // 截取处理逻辑
                parameter = handleSubstring(taskParams);
                break;
            case FIELD_DERIVE_REPLACE:
                // 替换处理逻辑
                parameter = handleReplace(taskParams);
                break;
            case FIELD_DERIVE_EXPRESSION:
                // 表达式处理逻辑
                parameter = handleExpression(taskParams);
                break;
            case FIELD_DERIVE_HASH:
                // 哈希处理逻辑
                parameter = handleHash(taskParams);
                break;
            case FIELD_DERIVE_REGEX:
                // 正则提取处理逻辑
                parameter = handleRegex(taskParams);
                break;
            case FIELD_DERIVE_CONSTANT:
                // 常量赋值处理逻辑
                parameter = handleConstant(taskParams);
                break;
            default:
                throw new IllegalArgumentException("未知的字段派生类型: " + fieldDerivationType);
        }

        reader.put("parameter", parameter);
        return reader;
    }

    private Map<String, Object> handleConstant(Map<String, Object> taskParams) {
        return null;
    }

    private Map<String, Object> handleRegex(Map<String, Object> taskParams) {
        return null;
    }

    private Map<String, Object> handleHash(Map<String, Object> taskParams) {
        return null;
    }

    private Map<String, Object> handleExpression(Map<String, Object> taskParams) {
        return null;
    }

    private Map<String, Object> handleReplace(Map<String, Object> taskParams) {
        return null;
    }

    private Map<String, Object> handleSubstring(Map<String, Object> taskParams) {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("fieldDerivationType",taskParams.get("fieldDerivationType"));
        parameter.put("fieldDerivationName",taskParams.get("fieldDerivationName"));
        parameter.put("direction",taskParams.get("direction"));
        parameter.put("tableFields",taskParams.get("tableFields"));
        parameter.put("startIndex",taskParams.get("startIndex"));
        parameter.put("endIndex",taskParams.get("endIndex"));
        return null;
    }

    private Map<String, Object> handleConcat(Map<String, Object> taskParams) {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("fieldDerivationType",taskParams.get("fieldDerivationType"));
        parameter.put("fieldDerivationName",taskParams.get("fieldDerivationName"));
        parameter.put("delimiter",taskParams.get("delimiter"));
        parameter.put("tableFields",taskParams.get("tableFields"));
        parameter.put("fieldDerivationPrefix",taskParams.get("fieldDerivationPrefix"));
        parameter.put("fieldDerivationSuffix",taskParams.get("fieldDerivationSuffix"));
        return parameter;
    }
}
