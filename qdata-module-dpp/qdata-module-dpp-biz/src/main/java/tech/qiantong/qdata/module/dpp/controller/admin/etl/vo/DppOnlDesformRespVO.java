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

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * Online Form Designer Response VO Object DPP_ONL_DESFORM
 *
 * @author qdata
 * @date 2025-04-09
 */
@Schema(description = "Online Form Designer Response VO")
@Data
public class DppOnlDesformRespVO implements Serializable {

    private static final long serialVersionUID = 1L;


    @Excel(name = "ID")
    @Schema(description = "ID", example = "")
    private Long id;

    @Excel(name = "Form Name")
    @Schema(description = "Form Name", example = "")
    private String desformName;

    @Excel(name = "Form Code")
    @Schema(description = "Form Code", example = "")
    private String desformCode;

    @Excel(name = "Form JSON")
    @Schema(description = "Form JSON", example = "")
    private String desformJson;

    @Excel(name = "Save to Specified Table")
    @Schema(description = "Save to Specified Table", example = "")
    private String saveTableFlag;

    @Excel(name = "Datasource ID")
    @Schema(description = "Datasource ID", example = "")
    private Long datasourceId;

    @Excel(name = "Database Name")
    @Schema(description = "Database Name", example = "")
    private String databaseName;

    @Excel(name = "Table Name")
    @Schema(description = "Table Name", example = "")
    private String tableName;

    @Excel(name = "Column")
    @Schema(description = "Column", example = "")
    private String columnName;

    @Excel(name = "Primary Key Column")
    @Schema(description = "Primary Key Column", example = "")
    private String pkColumnName;

    @Excel(name = "Generate PK Value by Program")
    @Schema(description = "Generate PK Value by Program", example = "")
    private String createPkDataFlag;

    @Excel(name = "Valid")
    @Schema(description = "Valid", example = "")
    private Boolean validFlag;

    @Excel(name = "Delete Flag")
    @Schema(description = "Delete Flag", example = "")
    private Boolean delFlag;

    @Excel(name = "Created By")
    @Schema(description = "Created By", example = "")
    private String createBy;

    @Excel(name = "Creator ID")
    @Schema(description = "Creator ID", example = "")
    private Long creatorId;

    @Excel(name = "Create Time", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Create Time", example = "")
    private Date createTime;

    @Excel(name = "Updated By")
    @Schema(description = "Updated By", example = "")
    private String updateBy;

    @Excel(name = "Updater ID")
    @Schema(description = "Updater ID", example = "")
    private Long updaterId;

    @Excel(name = "Update Time", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Update Time", example = "")
    private Date updateTime;

    @Excel(name = "Remark")
    @Schema(description = "Remark", example = "")
    private String remark;

}
