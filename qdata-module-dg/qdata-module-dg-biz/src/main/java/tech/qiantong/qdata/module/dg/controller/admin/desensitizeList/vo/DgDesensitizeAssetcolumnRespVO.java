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

package tech.qiantong.qdata.module.dg.controller.admin.desensitizeList.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import tech.qiantong.qdata.common.annotation.Excel;
import java.util.Date;
import java.io.Serializable;

/**
 * Desensitize List Relationship Response VO DG_DESENSITIZE_ASSETCOLUMN
 *
 * @author qdata
 * @date 2026-04-12
 */
@Schema(description = "Desensitize List Association Response VO")
@Data
public class DgDesensitizeAssetcolumnRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "Asset ID")
    @Schema(description = "Asset ID", example = "")
    private Long assetId;

    @Excel(name = "Asset Name")
    @Schema(description = "Asset Name", example = "")
    private String assetName;

    @Excel(name = "Asset Description")
    @Schema(description = "Asset Description", example = "")
    private String assetDescription;

    private String assetTableName;
    private String assetTableComment;


    @Excel(name = "Asset Column ID")
    @Schema(description = "Asset Column ID", example = "")
    private Long assetcolumnId;

    @Excel(name = "Asset Column Name")
    @Schema(description = "Asset Column Name", example = "")
    private String assetcolumnName;
    @Excel(name = "Asset Column Description")
    @Schema(description = "Asset Column Description", example = "")
    private String assetcolumnComment;

    @Excel(name = "Data Category ID")
    @Schema(description = "Data Category ID", example = "")
    private Long dataCategoryId;

    @Excel(name = "Data Category Name")
    @Schema(description = "Data Category Name", example = "")
    private String dataCategoryName;
    private String dataLevelName;
    private String desensitizeRuleName;

    @Excel(name = "Sort Order")
    @Schema(description = "Sort Order", example = "")
    private Long sortOrder;

    @Excel(name = "Description")
    @Schema(description = "Description", example = "")
    private String description;

    @Excel(name = "Valid Flag; 0: Invalid, 1: Valid")
    @Schema(description = "Valid Flag; 0: Invalid, 1: Valid", example = "")
    private Boolean validFlag;

    @Excel(name = "Deleted Flag; 1: Deleted, 0: Not Deleted")
    @Schema(description = "Deleted Flag; 1: Deleted, 0: Not Deleted", example = "")
    private Boolean delFlag;

    @Excel(name = "Created By")
    @Schema(description = "Created By", example = "")
    private String createBy;

    @Excel(name = "Creator ID")
    @Schema(description = "Creator ID", example = "")
    private Long creatorId;

    @Excel(name = "Created Time", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Created Time", example = "")
    private Date createTime;

    @Excel(name = "Updated By")
    @Schema(description = "Updated By", example = "")
    private String updateBy;

    @Excel(name = "Updater ID")
    @Schema(description = "Updater ID", example = "")
    private Long updaterId;

    @Excel(name = "Updated Time", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Updated Time", example = "")
    private Date updateTime;

    @Excel(name = "Remark")
    @Schema(description = "Remark", example = "")
    private String remark;

}
