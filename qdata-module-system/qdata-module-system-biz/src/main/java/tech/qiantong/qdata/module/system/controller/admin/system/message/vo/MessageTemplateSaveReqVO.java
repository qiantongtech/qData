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

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Message Template Create/Update Request VO
 *
 * @author qdata
 * @date 2024-10-31
 */
@Schema(description = "Message Template Response VO")
@Data
public class MessageTemplateSaveReqVO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;


    @Schema(description = "Message Title", example = "")
    @NotBlank(message = "Message title cannot be empty")
    @Size(max = 256, message = "Message title length cannot exceed 256 characters")
    private String title;


    @Schema(description = "Message Template Content", example = "")
    @NotBlank(message = "Message template content cannot be empty")
    @Size(max = 256, message = "Message template content length cannot exceed 256 characters")
    private String content;


    @Schema(description = "Message Category", example = "")
    @NotNull(message = "Message category cannot be empty")
    private Integer category;


    @Schema(description = "Message Level", example = "")
    @NotNull(message = "Message level cannot be empty")
    private Integer msgLevel;

}
