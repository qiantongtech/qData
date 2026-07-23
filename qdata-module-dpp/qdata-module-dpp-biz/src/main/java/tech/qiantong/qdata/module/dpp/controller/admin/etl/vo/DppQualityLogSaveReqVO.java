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

package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Data Quality Log Create/Update Request VO DPP_QUALITY_LOG
 *
 * @author qdata
 * @date 2025-07-19
 */
@Schema(description = "Data Quality Log Response VO")
@Data
public class DppQualityLogSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "Name", example = "")
    @Size(max = 256, message = "Name length cannot exceed 256 characters")
    private String name;

    @Schema(description = "Status", example = "")
    @Size(max = 256, message = "Status length cannot exceed 256 characters")
    private String successFlag;

    @Schema(description = "Start Time", example = "")
    private Date startTime;

    @Schema(description = "End Time", example = "")
    private Date endTime;

    @Schema(description = "Task ID", example = "")
    @Size(max = 256, message = "Task ID length cannot exceed 256 characters")
    private String qualityId;

    @Schema(description = "Score", example = "")
    private Long score;

    @Schema(description = "Problem Data", example = "")
    private Long problemData;

    @Schema(description = "Remark", example = "")
    @Size(max = 256, message = "Remark length cannot exceed 256 characters")
    private String remark;


    private String path;

}
