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

package tech.qiantong.qdata.module.dpp.dal.dataobject.etl;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * Data Integration Scheduler Info DO - DPP_ETL_SCHEDULER
 *
 * @author qdata
 * @date 2025-02-13
 */
@Data
@TableName(value = "DPP_ETL_SCHEDULER")
// Used for auto-increment primary keys in Oracle, PostgreSQL, Kingbase, DB2, H2 databases. Can be omitted for MySQL and similar databases.
// @KeySequence("DPP_ETL_SCHEDULER_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DppEtlSchedulerDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** Task ID */
    private Long taskId;

    /** Task code */
    private String taskCode;

    /** Start time */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /** End time */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** Timezone */
    private String timezoneId;

    @Schema(description = "Task status", example = "")
    private String status;

    /** Cron expression */
    private String cronExpression;

    /** Failure strategy */
    private String failureStrategy;

    /** DolphinScheduler ID */
    private Long dsId;

    /** Scheduling engine */
    private String taskScheduler;

    /** Execution engine */
    private String taskActuator;

    /** Quartz scheduled task ID */
    private Long quartzId;

    /** Valid flag */
    private Boolean validFlag;

    /** Delete flag */
    @TableLogic
    private Boolean delFlag;

}
