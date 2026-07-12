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

package tech.qiantong.qdata.module.dg.api.dataCategoryCat.dto;

import lombok.*;

/**
 * Data Category-Category DTO Object DG_DATA_CATEGORY_CAT
 *
 * @author FXB
 * @date 2026-04-07
 */
@Data
public class DgDataCategoryCatRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Category Name */
    private String name;

    /** Parent ID */
    private Long parentId;

    /** Category Sort Order */
    private Long sortOrder;

    /** Hierarchy Code */
    private String code;

    /** Description */
    private String description;

    /** Valid Flag; 0: Invalid, 1: Valid */
    private Boolean validFlag;

    /** Delete Flag; 1: Deleted, 0: Not Deleted */
    private Boolean delFlag;


}
