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
 * Business License: https://docs.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qdata.module.dg.controller.admin.standard.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * Data Element Response VO Object DG_DATA_ELEM
 *
 * @author qdata
 * @date 2025-01-21
 */
@Schema(description = "Data Element Response VO")
@Data
public class DgDataElemRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "Code")
    @Schema(description = "Code", example = "")
    private String code;

    @Excel(name = "Name")
    @Schema(description = "Name", example = "")
    private String name;

    @Excel(name = "English name")
    @Schema(description = "English name", example = "")
    private String engName;

    @Excel(name = "Category code")
    @Schema(description = "Category code", example = "")
    private String catCode;

    @Excel(name = "Category name")
    @Schema(description = "Category name", example = "")
    private String catName;

    @Excel(name = "Type")
    @Schema(description = "Type", example = "")
    private String type;

    @Excel(name = "Person in charge")
    @Schema(description = "Person in charge", example = "")
    private String personCharge;

    /** Person in Charge Name */
    private String personChargeName;

    @Excel(name = "Contact number")
    @Schema(description = "Contact number", example = "")
    private String contactNumber;

    @Excel(name = "Column type")
    @Schema(description = "Column type", example = "")
    private String columnType;

    @Excel(name = "Status")
    @Schema(description = "Status", example = "")
    private String status;

    @Excel(name = "Description")
    @Schema(description = "Description", example = "")
    private String description;

    @Excel(name = "Valid flag")
    @Schema(description = "Valid flag", example = "")
    private Boolean validFlag;

    @Excel(name = "Delete flag")
    @Schema(description = "Delete flag", example = "")
    private Boolean delFlag;

    @Excel(name = "Created by")
    @Schema(description = "Created by", example = "")
    private String createBy;

    @Excel(name = "Creator ID")
    @Schema(description = "Creator ID", example = "")
    private Long creatorId;

    @Excel(name = "Create time", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Create time", example = "")
    private Date createTime;

    @Excel(name = "Updated by")
    @Schema(description = "Updated by", example = "")
    private String updateBy;

    @Excel(name = "Updater ID")
    @Schema(description = "Updater ID", example = "")
    private Long updaterId;

    @Excel(name = "Update time", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Update time", example = "")
    private Date updateTime;

    @Excel(name = "Remark")
    @Schema(description = "Remark", example = "")
    private String remark;

    private Long documentId;

    /** Name */
    @TableField(exist = false)
    private String documentName;

    /** Code */
    @TableField(exist = false)
    private String documentCode;

    /** Document Standard Type Field */
    @TableField(exist = false)
    private String documentType;
}
