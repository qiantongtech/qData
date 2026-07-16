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

import java.util.Date;

/**
 * Message Response VO for message
 *
 * @author qdata
 * @date 2024-10-31
 */
@Schema(description = "Message Response VO")
@Data
public class MessageRespVO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Excel(name = "Sender")
    @Schema(description = "Sender", example = "")
    private Long senderId;

    @Excel(name = "Receiver")
    @Schema(description = "Receiver", example = "")
    private Long receiverId;

    @Excel(name = "Message Title")
    @Schema(description = "Message title", example = "")
    private String title;

    @Excel(name = "Message Template Content")
    @Schema(description = "Message template content", example = "")
    private String content;

    @Excel(name = "Message Category")
    @Schema(description = "Message category", example = "")
    private Integer category;

    @Excel(name = "Message Level")
    @Schema(description = "Message level", example = "")
    private Integer msgLevel;

    @Excel(name = "Message Module")
    @Schema(description = "Message module", example = "")
    private Integer module;

    @Excel(name = "Entity Type")
    @Schema(description = "Entity type", example = "")
    private Integer entityType;

    @Excel(name = "Entity ID")
    @Schema(description = "Entity ID", example = "")
    private Long entityId;

    @Excel(name = "Message Link")
    @Schema(description = "Message link", example = "")
    private String entityUrl;

    @Excel(name = "Whether Read")
    @Schema(description = "Whether read", example = "")
    private Integer hasRead;

    @Excel(name = "Whether Retracted")
    @Schema(description = "Whether retracted", example = "")
    private Integer hasRetraction;

    @Excel(name = "Whether Valid")
    @Schema(description = "Whether valid", example = "")
    private Boolean validFlag;

    @Schema(description = "Delete flag")
    private Integer delFlag;

    @Excel(name = "Creator")
    @Schema(description = "Creator", example = "")
    private String createBy;

    @Excel(name = "Creator ID")
    @Schema(description = "Creator ID", example = "")
    private Long creatorId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "Create Time", width = 30, dateFormat = "yyyy-MM-dd")
    @Schema(description = "Create time", example = "")
    private Date createTime;

    @Excel(name = "Updater")
    @Schema(description = "Updater", example = "")
    private String updateBy;

    @Excel(name = "Updater ID")
    @Schema(description = "Updater ID", example = "")
    private Long updaterId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "Update Time", width = 30, dateFormat = "yyyy-MM-dd")
    @Schema(description = "Update time", example = "")
    private Date updateTime;

    @Excel(name = "Remark")
    @Schema(description = "Remark", example = "")
    private String remark;

}
