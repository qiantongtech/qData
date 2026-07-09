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
import tech.qiantong.qdata.common.core.page.PageParam;

import java.util.Date;

/**
 * Data Quality Log Request VO Object DPP_QUALITY_LOG
 *
 * @author qdata
 * @date 2025-07-19
 */
@Schema(description = "Data Quality Log Request VO")
@Data
public class DppQualityLogPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "Name", example = "")
    private String name;

    @Schema(description = "Status", example = "")
    private String successFlag;

    @Schema(description = "Start Time", example = "")
    private Date startTime;

    @Schema(description = "End Time", example = "")
    private Date endTime;

    @Schema(description = "Task ID", example = "")
    private String qualityId;

    @Schema(description = "Score", example = "")
    private Long score;

    @Schema(description = "Problem Data", example = "")
    private Long problemData;


    private String path;



}
