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

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * Message Request VO for message
 *
 * @author qdata
 * @date 2024-10-31
 */
@Schema(description = "Message Request VO")
@Data
public class MessagePageReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "Sender", example = "")
    private Long senderId;

    @Schema(description = "Receiver", example = "")
    private Long receiverId;

    @Schema(description = "Message title", example = "")
    private String title;

    @Schema(description = "Message template content", example = "")
    private String content;

    @Schema(description = "Message category", example = "")
    private Integer category;

    @Schema(description = "Message level", example = "")
    private Integer msgLevel;

    @Schema(description = "Message module", example = "")
    private Integer module;

    @Schema(description = "Entity type", example = "")
    private Integer entityType;

    @Schema(description = "Entity ID", example = "")
    private Long entityId;

    @Schema(description = "Message link", example = "")
    private String entityUrl;

    @Schema(description = "Whether read", example = "")
    private Integer hasRead;

    @Schema(description = "Whether retracted", example = "")
    private Integer hasRetraction;

    @Schema(description = "Whether valid", example = "")
    private Boolean validFlag;

    @Schema(description = "Delete flag")
    private Integer delFlag;

    private Date startTime;
    private Date endTime;

}
