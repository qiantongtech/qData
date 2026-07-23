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

/**
 * Online Form Designer Request VO Object DPP_ONL_DESFORM
 *
 * @author qdata
 * @date 2025-04-09
 */
@Schema(description = "Online Form Designer Request VO")
@Data
public class DppOnlDesformPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Form Name", example = "")
    private String desformName;

    @Schema(description = "Form Code", example = "")
    private String desformCode;

    @Schema(description = "Form JSON", example = "")
    private String desformJson;

    @Schema(description = "Save to Specified Table", example = "")
    private String saveTableFlag;

    @Schema(description = "Datasource ID", example = "")
    private Long datasourceId;

    @Schema(description = "Database Name", example = "")
    private String databaseName;

    @Schema(description = "Table Name", example = "")
    private String tableName;

    @Schema(description = "Column", example = "")
    private String columnName;

    @Schema(description = "Primary Key Column", example = "")
    private String pkColumnName;

    @Schema(description = "Generate PK Value by Program", example = "")
    private String createPkDataFlag;




}
