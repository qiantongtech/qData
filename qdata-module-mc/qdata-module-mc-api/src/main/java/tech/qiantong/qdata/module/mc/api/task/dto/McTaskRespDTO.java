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

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Collection task DTO object MC_TASK
 *
 * @author qdata
 * @date 2025-12-16
 */
@Data
public class McTaskRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Source system ID */
    private Long sourceSystemId;

    /** Source system name */
    private String sourceSystemName;

    /** Task name */
    private String name;

    /** Data connection id */
    private Long datasourceId;

    /** Database type */
    private String dbType;

    /** Responsible person */
    private Long leader;

    /** Responsible person’s phone number */
    private String leaderPhone;

    /** Collection mode */
    private String collectionMode;

    /** Collection range */
    private String collectionScope;

    /** Task status */
    private String status;

    /** Whether the record is active */
    private Boolean validFlag;

    /** Delete flag */
    private Boolean delFlag;

    /** Description */
    private String description;

    /**
     * Collection task type: 1-Collection, 2-DDL
     */
    @Schema(description = "采集任务类型：1-采集，2-DDL", example = "1")
    private String collectType;

    /**
     * Collection blacklist
     */
    @Schema(description = "采集黑名单", example = "")
    private String blacklist;


}
