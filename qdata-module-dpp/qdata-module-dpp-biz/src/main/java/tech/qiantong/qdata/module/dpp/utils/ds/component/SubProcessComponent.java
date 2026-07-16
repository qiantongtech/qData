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

import org.apache.commons.collections4.MapUtils;
import tech.qiantong.qdata.common.enums.TaskComponentTypeEnum;
import tech.qiantong.qdata.common.utils.JSONUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <P>
 * Purpose: Development task component
 * </p>
 **/
public class SubProcessComponent implements ComponentItem {

    /**
     *
     * taskParams SUB_PROCESS (subtask, same for development tasks)
     * {
     *     "localParams": [],//default []
     *     "resourceList": [],//default []
     *     "processDefinitionCode": 135576103357024//subtask code
     * }
     * @param params
     * @return
     */
    @Override
    public Map<String, Object> parse(Map<String, Object> params) {
        Map<String, Object> taskParams = new LinkedHashMap<>();
        taskParams.put("localParams", params.getOrDefault("localParams", new ArrayList<>())); // default empty list
        taskParams.put("resourceList", params.getOrDefault("resourceList", new ArrayList<>())); // default empty list
        String processDefinitionCode = MapUtils.getString(params,"processDefinitionCode", "");
        taskParams.put("processDefinitionCode", JSONUtils.convertToLong(processDefinitionCode)); // default empty string
        return taskParams;
    }

    @Override
    public String code() {
        return TaskComponentTypeEnum.SUB_PROCESS.getCode();
    }


    /**
     * Convert string to long type
     *
     * @param processDefinitionCode string to convert
     * @return converted long value, returns -1 if conversion fails
     */
    public static long convertToLong(String processDefinitionCode) {
        if (processDefinitionCode == null || processDefinitionCode.trim().isEmpty()) {
            return -1;
        }
        try {
            return Long.parseLong(processDefinitionCode.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
