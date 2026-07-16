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

package tech.qiantong.qdata.module.system.controller.admin.system.message.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * Message Template Response VO
 *
 * @author qdata
 * @date 2024-10-31
 */
@Schema(description = "Message Template Response VO")
@Data
public class MessageTemplateRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Excel(name = "Message Title")
    @Schema(description = "Message Title", example = "")
    private String title;

    @Excel(name = "Message Template Content")
    @Schema(description = "Message Template Content", example = "")
    private String content;

    @Excel(name = "Message Category")
    @Schema(description = "Message Category", example = "")
    private Integer category;

    @Excel(name = "Message Level")
    @Schema(description = "Message Level", example = "")
    private Integer msgLevel;

    @Excel(name = "Active Status")
    @Schema(description = "Active Status", example = "")
    private Boolean validFlag;

    @Schema(description = "Delete Flag")
    private Boolean delFlag;

    @Excel(name = "Created By")
    @Schema(description = "Created By", example = "")
    private String createBy;

    @Excel(name = "Created By ID")
    @Schema(description = "Created By ID", example = "")
    private Long creatorId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "Created Time", width = 30, dateFormat = "yyyy-MM-dd")
    @Schema(description = "Created Time", example = "")
    private Date createTime;

    @Excel(name = "Updated By")
    @Schema(description = "Updated By", example = "")
    private String updateBy;

    @Excel(name = "Updated By ID")
    @Schema(description = "Updated By ID", example = "")
    private Long updaterId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "Updated Time", width = 30, dateFormat = "yyyy-MM-dd")
    @Schema(description = "Updated Time", example = "")
    private Date updateTime;

    @Excel(name = "Remark")
    @Schema(description = "Remark", example = "")
    private String remark;

}
