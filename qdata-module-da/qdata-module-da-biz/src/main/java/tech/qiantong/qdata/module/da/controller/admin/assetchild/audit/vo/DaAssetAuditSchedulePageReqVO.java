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

package tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 资产稽查调度 Request VO 对象 DA_ASSET_AUDIT_SCHEDULE
 *
 * @author qdata
 * @date 2025-05-09
 */
@Schema(description = "资产稽查调度 Request VO")
@Data
public class DaAssetAuditSchedulePageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "资产ID", example = "")
    private Long assetId;

    @Schema(description = "是否开启稽查调度", example = "")
    private String scheduleFlag;

    @Schema(description = "cron执行表达式", example = "")
    private String cronExpression;

    @Schema(description = "节点id", example = "")
    private Long nodeId;

    @Schema(description = "节点编码", example = "")
    private String nodeCode;

    @Schema(description = "任务id", example = "")
    private Long taskId;

    @Schema(description = "任务编码", example = "")
    private String taskCode;

    @Schema(description = "定时任务调度表id", example = "")
    private Long systemJobId;




}
