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

package tech.qiantong.qdata.module.system.api.message.dto;

import lombok.Data;

/**
 * Message Template DTO object message_template
 *
 * @author qdata
 * @date 2024-10-31
 */
@Data
public class MessageTemplateRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Message title */
    private String title;

    /** Message template content */
    private String content;

    /** Message category */
    private Integer category;

    /** Message level */
    private Integer msgLevel;

    /** Is active */
    private Boolean validFlag;

    /** Delete flag */
    private Boolean delFlag;


}
