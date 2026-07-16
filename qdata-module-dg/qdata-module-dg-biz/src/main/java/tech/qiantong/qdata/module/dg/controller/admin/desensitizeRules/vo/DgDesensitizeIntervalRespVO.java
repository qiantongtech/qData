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
import java.util.Date;
import java.io.Serializable;

/**
 * Desensitize Interval Response VO DG_DESENSITIZE_INTERVAL
 *
 * @author qdata
 * @date 2026-04-10
 */
@Schema(description = "Desensitize Interval Response VO")
@Data
public class DgDesensitizeIntervalRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "Desensitize rule ID")
    @Schema(description = "Desensitize rule ID", example = "")
    private Long desensitizeRuleId;

    @Excel(name = "Interval number")
    @Schema(description = "Interval number", example = "")
    private Long intervalNo;

    @Excel(name = "Start value")
    @Schema(description = "Start value", example = "")
    private Long startNum;

    @Excel(name = "End value")
    @Schema(description = "End value", example = "")
    private Long endNum;

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
