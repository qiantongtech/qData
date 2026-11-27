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
 * Business License: https://docs.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qdata.module.mc.api.task.dto;

import lombok.Data;

/**
 * Collection scope DTO object MC_TASK_SCOPE
 *
 * @author qdata
 * @date 2025-12-16
 */
@Data
public class McTaskScopeReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** task id */
    private Long taskId;

    /** Database name */
    private String dbName;

    /** Pattern name */
    private String schemaName;

    /** Is it valid */
    private Boolean validFlag;

    /** Delete flag */
    private Boolean delFlag;

    /** Description */
    private String description;


}
