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

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Message Create/Update Request VO
 *
 * @author qdata
 * @date 2024-10-31
 */
@Schema(description = "Message Response VO")
@Data
public class MessageSaveReqVO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Sender", example = "")
    private Long senderId;


    @Schema(description = "Recipient", example = "")
    private Long receiverId;

    @Schema(description = "Message Module", example = "")
    @NotNull(message = "Message module cannot be empty")
    private Integer module;


    @Schema(description = "Entity Type", example = "")
    private Integer entityType;


    @Schema(description = "Entity ID", example = "")
    private Long entityId;

    @Schema(description = "Message Link", example = "")
    @Size(max = 256, message = "Message link length cannot exceed 256 characters")
    private String entityUrl;

    private Integer delFlag;
    private String id;
    private Integer hasRead;

    @Schema(description = "Creator ID", example = "")
    @TableField(fill = FieldFill.INSERT)
    private Long creatorId;

    /**
     * Creator
     */
    @Schema(description = "Creator", example = "")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

}
