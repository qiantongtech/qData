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

import java.util.Date;

/**
 * Data Asset - Quality Alert Request VO DA_ASSET_AUDIT_ALERT
 *
 * @author qdata
 * @date 2025-05-09
 */
@Schema(description = "数据资产-质量预警 Request VO")
@Data
public class DaAssetAuditAlertPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "资产ID", example = "")
    private Long assetId;

    @Schema(description = "稽查批次号", example = "")
    private String batchNo;

    @Schema(description = "稽查时间", example = "")
    private Date auditTime;

    @Schema(description = "预警时间", example = "")
    private Date alertTime;

    @Schema(description = "预警信息", example = "")
    private String alertMessage;

    @Schema(description = "预警通道JSON", example = "")
    private String alertChannels;

    @Schema(description = "预警通道结果", example = "")
    private String alertChannelResult;




}
