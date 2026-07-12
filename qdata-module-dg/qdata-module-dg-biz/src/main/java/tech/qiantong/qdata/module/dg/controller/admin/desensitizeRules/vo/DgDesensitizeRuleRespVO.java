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

package tech.qiantong.qdata.module.dg.controller.admin.desensitizeRules.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import tech.qiantong.qdata.common.annotation.Excel;
import tech.qiantong.qdata.module.dg.dal.dataobject.desensitizeRules.DgDesensitizeIntervalDO;

import java.util.Date;
import java.io.Serializable;
import java.util.List;

/**
 * Desensitize Rule Response VO DG_DESENSITIZE_RULE
 *
 * @author qdata
 * @date 2026-04-10
 */
@Schema(description = "Desensitize Rule Response VO")
@Data
public class DgDesensitizeRuleRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "Rule name")
    @Schema(description = "Rule name", example = "")
    private String name;

    @Excel(name = "Data category ID")
    @Schema(description = "Data category ID", example = "")
    private Long dataCategoryId;
    @Excel(name = "Data category name")
    @Schema(description = "Data category name", example = "")
    private String dataCategoryName;

    @Excel(name = "Application scene; 1: Data asset 2: Data query 3: Data service")
    @Schema(description = "Application scene; 1: Data asset 2: Data query 3: Data service", example = "")
    private String applicationScene;

    @Excel(name = "Mask type; 1: Underlying mask 2: Display mask")
    @Schema(description = "Mask type; 1: Underlying mask 2: Display mask", example = "")
    private String maskType;

    @Excel(name = "Replace rule")
    @Schema(description = "Replace rule", example = "")
    private String replaceRule;

    @Excel(name = "Replace content")
    @Schema(description = "Replace content", example = "")
    private String replaceContent;

    @Excel(name = "Desensitize intervals")
    @Schema(description = "Desensitize intervals", example = "")
    private List<DgDesensitizeIntervalDO> intervalList;

    @Excel(name = "Sort order")
    @Schema(description = "Sort order", example = "")
    private Long sortOrder;

    @Excel(name = "Description")
    @Schema(description = "Description", example = "")
    private String description;

    @Excel(name = "Valid flag; 0: Invalid, 1: Valid")
    @Schema(description = "Valid flag; 0: Invalid, 1: Valid", example = "")
    private Boolean validFlag;

    @Excel(name = "Delete flag; 1: Deleted, 0: Not deleted")
    @Schema(description = "Delete flag; 1: Deleted, 0: Not deleted", example = "")
    private Boolean delFlag;

    @Excel(name = "Created by")
    @Schema(description = "Created by", example = "")
    private String createBy;

    @Excel(name = "Creator ID")
    @Schema(description = "Creator ID", example = "")
    private Long creatorId;

    @Excel(name = "Created time", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Created time", example = "")
    private Date createTime;

    @Excel(name = "Updated by")
    @Schema(description = "Updated by", example = "")
    private String updateBy;

    @Excel(name = "Updater ID")
    @Schema(description = "Updater ID", example = "")
    private Long updaterId;

    @Excel(name = "Updated time", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Updated time", example = "")
    private Date updateTime;

    @Excel(name = "Remark")
    @Schema(description = "Remark", example = "")
    private String remark;

}
