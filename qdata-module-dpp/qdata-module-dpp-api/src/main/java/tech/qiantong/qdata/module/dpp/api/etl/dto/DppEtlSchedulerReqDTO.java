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

package tech.qiantong.qdata.module.dpp.api.etl.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * Data Integration Scheduler DTO - DPP_ETL_SCHEDULER
 *
 * @author qdata
 * @date 2025-02-13
 */
@Data
public class DppEtlSchedulerReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Task ID */
    private Long taskId;

    /** Task Code */
    private String taskCode;

    /** Start Time */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /** End Time */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** Timezone */
    private String timezoneId;

    @Schema(description = "Task Status", example = "")
    private String status;

    /** Cron Expression */
    private String cronExpression;

    /** Failure Strategy */
    private String failureStrategy;

    /** DolphinScheduler ID */
    private Long dsId;

    /** Valid Flag */
    private Boolean validFlag;

    /** Delete Flag */
    private Boolean delFlag;


}
