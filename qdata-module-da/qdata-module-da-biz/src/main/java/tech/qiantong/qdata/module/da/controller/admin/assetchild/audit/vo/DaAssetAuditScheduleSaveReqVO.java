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
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;

/**
 * 资产稽查调度 创建/修改 Request VO DA_ASSET_AUDIT_SCHEDULE
 *
 * @author qdata
 * @date 2025-05-09
 */
@Schema(description = "资产稽查调度 Response VO")
@Data
public class DaAssetAuditScheduleSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "资产ID", example = "")
    private Long assetId;

    @Schema(description = "是否开启稽查调度", example = "")
    @Size(max = 256, message = "是否开启稽查调度长度不能超过256个字符")
    private String scheduleFlag;

    @Schema(description = "cron执行表达式", example = "")
    @Size(max = 256, message = "cron执行表达式长度不能超过256个字符")
    private String cronExpression;

    @Schema(description = "节点id", example = "")
    private Long nodeId;

    @Schema(description = "节点编码", example = "")
    @Size(max = 256, message = "节点编码长度不能超过256个字符")
    private String nodeCode;

    @Schema(description = "任务id", example = "")
    private Long taskId;

    @Schema(description = "任务编码", example = "")
    @Size(max = 256, message = "任务编码长度不能超过256个字符")
    private String taskCode;

    @Schema(description = "定时任务调度表id", example = "")
    private Long systemJobId;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
