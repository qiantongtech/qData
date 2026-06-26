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
 * 消息模板 Request VO 对象 message_template
 *
 * @author qdata
 * @date 2024-10-31
 */
@Schema(description = "消息模板 Request VO")
@Data
public class MessageTemplatePageReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "消息标题", example = "")
    private String title;

    @Schema(description = "消息模板内容", example = "")
    private String content;

    @Schema(description = "消息类别", example = "")
    private Integer category;

    @Schema(description = "消息等级", example = "")
    private Integer msgLevel;

    @Schema(description = "是否有效", example = "")
    private Boolean validFlag;

    @Schema(description = "删除标识")
    private Boolean delFlag;

}
