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
 * 资产稽查调度 DTO 对象 DA_ASSET_AUDIT_SCHEDULE
 *
 * @author qdata
 * @date 2025-05-09
 */
@Data
public class DaAssetAuditScheduleReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 资产ID */
    private Long assetId;

    /** 是否开启稽查调度（0否1是） */
    private String scheduleFlag;

    /** cron执行表达式 */
    private String cronExpression;

    /** 节点id */
    private Long nodeId;

    /** 节点编码 */
    private String nodeCode;

    /** 任务id */
    private Long taskId;

    /** 任务编码 */
    private String taskCode;

    /** 定时任务调度表id */
    private Long systemJobId;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
