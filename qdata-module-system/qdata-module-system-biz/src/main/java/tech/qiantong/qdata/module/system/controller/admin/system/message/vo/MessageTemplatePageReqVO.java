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

/**
 * Message Template Request VO
 *
 * @author qdata
 * @date 2024-10-31
 */
@Schema(description = "Message Template Request VO")
@Data
public class MessageTemplatePageReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "Message Title", example = "")
    private String title;

    @Schema(description = "Message Template Content", example = "")
    private String content;

    @Schema(description = "Message Category", example = "")
    private Integer category;

    @Schema(description = "Message Level", example = "")
    private Integer msgLevel;

    @Schema(description = "Active Status", example = "")
    private Boolean validFlag;

    @Schema(description = "Delete Flag")
    private Boolean delFlag;

}
