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

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * Data Integration Task DO - DPP_ETL_TASK
 *
 * @author qdata
 * @date 2025-02-13
 */
@Data
@TableName(value = "DPP_ETL_TASK")
// Used for auto-increment primary keys in Oracle, PostgreSQL, Kingbase, DB2, H2 databases. Can be omitted for MySQL and similar databases.
// @KeySequence("DPP_ETL_TASK_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DppEtlTaskDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** Category ID */
    private Long catId;

    /** Category code */
    private String catCode;

    @TableField(exist = false)
    private String catName;

    /** Datasource type */
    @TableField(exist = false)
    private String datasourceType;

    /** 1: Offline task 2: Real-time task 3: Data development task 4: Job task */
    private String type;

    /** Task name */
    private String name;

    /** Task code */
    private String code;

    /** Task version */
    private Long version;

    /** Project ID */
    private Long projectId;

    /** Project code */
    private String projectCode;

    /** Person in charge */
    private String personCharge;

    /** Person in charge name */
    @TableField(exist = false)
    private String personChargeName;

    /** Contact number */
    private String contactNumber;


    /** Node location info */
    private String locations;

    @Schema(description = "Task execution strategy", example = "")
    private String executionType;

    /** Description */
    private String description;

    /** Timeout */
    private Long timeout;

    /** Extraction count */
    private Long extractionCount;

    /** Write count */
    private Long writeCount;

    /** Task status */
    private String status;

    /** DolphinScheduler ID */
    private Long dsId;

    /** Valid flag */
    private Boolean validFlag;

    /** Delete flag */
    @TableLogic
    private Boolean delFlag;

    /** Cron expression */
    @TableField(exist = false)
    private String cronExpression;

    /** Scheduler state */
    @TableField(exist = false)
    private String schedulerState;

    /** Last execute time */
    @TableField(exist = false)
    private Date lastExecuteTime;

    @Schema(description = "Draft task configuration information", example = "")
    private String draftJson;
}
