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

package tech.qiantong.qdata.module.da.api.assetchild.audit.dto;

import lombok.Data;

/**
 * Asset Audit Schedule DTO DA_ASSET_AUDIT_SCHEDULE
 *
 * @author qdata
 * @date 2025-05-09
 */
@Data
public class DaAssetAuditScheduleRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Asset ID */
    private Long assetId;

    /** Audit Schedule Flag (0: No, 1: Yes) */
    private String scheduleFlag;

    /** Cron Expression */
    private String cronExpression;

    /** Node ID */
    private Long nodeId;

    /** Node Code */
    private String nodeCode;

    /** Task ID */
    private Long taskId;

    /** Task Code */
    private String taskCode;

    /** Scheduled Job ID */
    private Long systemJobId;

    /** Valid Flag */
    private Boolean validFlag;

    /** Delete Flag */
    private Boolean delFlag;


}
