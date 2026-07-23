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

package tech.qiantong.qdata.module.da.dal.dataobject.discovery;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * Data Discovery Task DO - DA_DISCOVERY_TASK
 *
 * @author qdata
 * @date 2025-02-11
 */
@Data
@TableName(value = "DA_DISCOVERY_TASK")
// Used for auto-increment primary keys in Oracle, PostgreSQL, Kingbase, DB2, H2 databases. Not needed for MySQL and similar databases.
// @KeySequence("DA_DISCOVERY_TASK_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DaDiscoveryTaskDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
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

    /** Email */
    private String email;

    /** Contact ID */
    private Long contactId;

    /** Last Changed Table Count */
    private Long lastTableCount;

    /** Contact Number */
    private String contactNumber;

    /** Category Code */
    private String catCode;

    @TableField(exist = false)
    private String catName;

    /** Description */
    private String description;

    /** Scheduled Task Scheduler Table ID */
    private Long systemJobId;

    /** Last Execution Time */
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
    @TableLogic
    private Boolean delFlag;


}
