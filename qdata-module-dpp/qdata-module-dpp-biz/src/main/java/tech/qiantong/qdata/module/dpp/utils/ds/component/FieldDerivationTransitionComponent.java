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

package tech.qiantong.qdata.module.dpp.utils.ds.component;
import tech.qiantong.qdata.common.exception.ServiceException;

import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson2.JSON;
import org.apache.commons.collections4.MapUtils;
import tech.qiantong.qdata.common.enums.TaskComponentTypeEnum;
import tech.qiantong.qdata.common.enums.etl.transition.FieldDerivationTypeEnum;
import tech.qiantong.qdata.module.dpp.utils.model.DsResource;

import java.util.*;

/**
 * <P>
 * Purpose: Spark data cleansing component
 * </p>
 *
 * @author: FXB
 * @create: 2025-03-12 16:31
 **/
public class FieldDerivationTransitionComponent implements ComponentItem {
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
        taskParams.put("executorCores", params.getOrDefault("executorCores", DEFAULT_EXECUTOR_CORES)); // Default executor core count
        taskParams.put("sqlExecutionType", params.getOrDefault("sqlExecutionType", DEFAULT_SQL_EXECUTION_TYPE)); // Default SQL execution type is "SCRIPT"
        return taskParams;
    }

    @Override
    public String code() {
        return TaskComponentTypeEnum.FIELD_DERIVATION.getCode();
    }

    @Override
    public Map<String, Object> parse2(String nodeCode, Integer nodeVersion, TaskComponentTypeEnum componentType, Map<String, Object> taskParams, String resourceUrl, List<DsResource> resourceList) {
        // Reader config
        Map<String, Object> reader = new HashMap<>();
        reader.put("nodeCode", nodeCode);
        reader.put("nodeVersion", nodeVersion);
        reader.put("componentType", componentType.getCode());


        String fieldDerivationType = MapUtils.getString(taskParams,"fieldDerivationType");
        FieldDerivationTypeEnum typeEnum = FieldDerivationTypeEnum.fromCode(fieldDerivationType);
        // Parameters
        Map<String, Object> parameter = new HashMap<>();

        switch (typeEnum) {
            case FIELD_DERIVE_CONCAT:
                // Concat processing logic
                parameter = handleConcat(taskParams);
                break;
            case FIELD_DERIVE_SUBSTRING:
                // Substring processing logic
                parameter = handleSubstring(taskParams);
                break;
            case FIELD_DERIVE_REPLACE:
                // Replace processing logic
                parameter = handleReplace(taskParams);
                break;
            case FIELD_DERIVE_EXPRESSION:
                // Expression processing logic
                parameter = handleExpression(taskParams);
                break;
            case FIELD_DERIVE_HASH:
                // Hash processing logic
                parameter = handleHash(taskParams);
                break;
            case FIELD_DERIVE_REGEX:
                // Regex extraction processing logic
                parameter = handleRegex(taskParams);
                break;
            case FIELD_DERIVE_CONSTANT:
                // Constant assignment processing logic
                parameter = handleConstant(taskParams);
                break;
            default:
                throw new ServiceException("dpp.error.field.derivation.unknown", "Unknown field derivation type: " + fieldDerivationType);
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
