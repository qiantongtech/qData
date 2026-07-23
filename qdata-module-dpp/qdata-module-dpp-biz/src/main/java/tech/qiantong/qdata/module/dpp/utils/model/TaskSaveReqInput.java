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

package tech.qiantong.qdata.module.dpp.utils.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class TaskSaveReqInput extends BaseEntity {

    @Schema(description = "Task name", example = "")
    private String name;
    private Long id;
    /** Node ID */
    @Schema(description = "Node ID", example = "")
    private Long nodeId;

    /** Node code */
    @Schema(description = "Node code", example = "")
    private String nodeCode;

    /** Task ID */
    @Schema(description = "Task ID", example = "")
    private Long taskId;

    /** Task code */
    @Schema(description = "Task code", example = "")
    private String taskCode;

    /**
     * {
     *   "prop": "id",
     *   "httpParametersType": "PARAMETER",
     *   "value": "111111"
     * }
     *
     *  1. PARAMETER: indicates that the parameter is passed as a URL parameter.
     *  2. BODY: indicates that the parameter is passed as the request body, typically used in POST requests.
     *  3. HEADER: indicates that the parameter is passed as part of the HTTP request header.
     */
    private List<Map<String, Object>> httpParams;



    // Constructor
    public TaskSaveReqInput() {
        this.httpParams = new ArrayList<>(); // Initialize httpParams
    }

    // Method: dynamically add httpParams
    public void addHttpParam(String prop, String httpParametersType, Object value) {
        Map<String, Object> param = new HashMap<>();
        param.put("prop", prop);
        param.put("httpParametersType", httpParametersType);
        param.put("value", value);
        this.httpParams.add(param); // Add new parameter to the httpParams list
    }
}
