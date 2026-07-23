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

package tech.qiantong.qdata.module.da.api.discovery.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * Data Discovery Task DTO DA_DISCOVERY_TASK
 *
 * @author qdata
 * @date 2025-02-11
 */
@Data
public class DaDiscoveryTaskReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Task Name */
    private String name;

    /** Datasource ID */
    private Long datasourceId;

    /** Task Status */
    private String status;

    /** Cron Expression */
    private String cronExpression;

    /** Contact */
    private String contact;

    @Schema(description = "邮箱", example = "")
    private String email;

    /** Contact ID */
    private Long contactId;

    /** Last Changed Table Count */
    private Long lastTableCount;

    /** Contact Number */
    private String contactNumber;

    /** Category Code */
    private String catCode;

    /** Description */
    private String description;

    /** Scheduled Job ID */
    private Long systemJobId;

    /** Last Execute Time */
    private Date lastExecuteTime;

    /** Node ID */
    @Schema(description = "节点id", example = "")
    private Long nodeId;

    /** Node Code */
    @Schema(description = "节点编码", example = "")
    private String nodeCode;

    /** Task ID */
    @Schema(description = "任务id", example = "")
    private Long taskId;

    /** Task Code */
    @Schema(description = "任务编码", example = "")
    private String taskCode;

    /** Valid Flag */
    private Boolean validFlag;

    /** Delete Flag */
    private Boolean delFlag;


}
