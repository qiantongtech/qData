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

package tech.qiantong.qdata.module.att.api.cat.dto;

import lombok.Data;

/**
 * Data Integration Task Category Management DTO ATT_TASK_CAT
 *
 * @author qdata
 * @date 2025-03-11
 */
@Data
public class AttTaskCatRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Category Name */
    private String name;

    /** Parent ID */
    private Long parentId;

    /** Sort Order */
    private Long sortOrder;

    /** Description */
    private String description;

    /** Level Code */
    private String code;

    /** Valid Flag */
    private Boolean validFlag;

    /** Delete Flag */
    private Boolean delFlag;


}
