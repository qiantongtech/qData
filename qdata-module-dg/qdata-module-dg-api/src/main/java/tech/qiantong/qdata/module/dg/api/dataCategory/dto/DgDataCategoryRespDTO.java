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

package tech.qiantong.qdata.module.dg.api.dataCategory.dto;

import lombok.*;

/**
 * Data Category DTO Object DG_DATA_CATEGORY
 *
 * @author qdata
 * @date 2026-04-07
 */
@Data
public class DgDataCategoryRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Category ID */
    private Long catId;

    /** Category code */
    private String catCode;

    /** Category name */
    private String name;

    /** Category short name */
    private String shortName;

    /** Data level */
    private Long dataLevelId;

    /** Task priority;HIGHEST,HIGH,MEDIUM,LOW,LOWEST */
    private String priority;

    /** Description */
    private String description;

    /** Valid flag;0: invalid, 1: valid */
    private Boolean validFlag;

    /** Delete flag;1: deleted, 0: not deleted */
    private Boolean delFlag;


}
