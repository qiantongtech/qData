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

import tech.qiantong.qdata.common.enums.TaskComponentTypeEnum;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <P>
 * Purpose: SparkSQL development component
 * </p>
 **/
public class SparkSQLComponent implements ComponentItem {

    /**
     *
     * taskParams SPARK (SparkSQL development)
     * {
     *     "localParams": [],//Default []
     *     "rawScript": "Script",//Script
     *     "resourceList": [],//Default []
     *     "programType": "SQL",//Default "SQL"
     *     "mainClass": "",//Default ""
     *     "deployMode": "client",//Default "client"
     *     "yarnQueue":"",//Default ""
     *     "master": "",//Default ""
     *     "driverCores": 1,//Default 1
     *     "driverMemory": "512M",//Default "512M"
     *     "numExecutors": 1,//Default 1
     *     "executorMemory": "1G",//Default 1G
     *     "executorCores": 1,//Default 1
     *     "sqlExecutionType": "SCRIPT"//Default "SCRIPT"
     * }
     *
     * @param params
     * @return
     */
    @Override
    public Map<String, Object> parse(Map<String, Object> params) {
        Map<String, Object> taskParams = new LinkedHashMap<>();

        taskParams.put("localParams", params.getOrDefault("localParams", new ArrayList<>())); // Default empty list
        taskParams.put("rawScript", params.getOrDefault("rawScript", "")); // Default empty string
        taskParams.put("resourceList", params.getOrDefault("resourceList", new ArrayList<>())); // Default empty list
        taskParams.put("programType", params.getOrDefault("programType", DEFAULT_PROGRAM_TYPE)); // Default program type is "JAVA"
        taskParams.put("mainClass", params.get("mainClass")); // Default main class
        taskParams.put("deployMode", params.getOrDefault("deployMode", DEFAULT_DEPLOY_MODE)); // Default deploy mode is "client"
        taskParams.put("yarnQueue", params.getOrDefault("yarnQueue", "")); // Default empty string
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
        return TaskComponentTypeEnum.SPARK_SQL_DEV.getCode();
    }
}
