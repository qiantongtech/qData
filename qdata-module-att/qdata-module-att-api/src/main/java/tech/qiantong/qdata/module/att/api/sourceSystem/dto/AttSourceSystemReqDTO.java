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

package tech.qiantong.qdata.module.att.api.sourceSystem.dto;

import lombok.Data;

/**
 * Source System DTO ATT_SOURCE_SYSTEM
 *
 * @author qdata
 * @date 2026-04-03
 */
@Data
public class AttSourceSystemReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** System Name */
    private String name;

    /** System Type */
    private String type;

    /** Sort Order */
    private Long sortOrder;

    /** Description */
    private String description;

    /** Valid Flag; 0: invalid, 1: valid */
    private Boolean validFlag;

    /** Person in Charge */
    private String responsiblePerson;

    /** Contact Person */
    private String contactPerson;

    /** Delete Flag; 1: deleted, 0: not deleted */
    private Boolean delFlag;


}
