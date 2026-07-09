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

package tech.qiantong.qdata.module.dp.api.dataElem.dto;

import lombok.Data;

/**
 * Data Element DTO - DP_DATA_ELEM
 *
 * @author qdata
 * @date 2025-01-21
 */
@Data
public class DpDataElemReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Code */
    private String code;

    /** Name */
    private String name;

    /** English Name */
    private String engName;

    /** Category Code */
    private String catCode;

    /** Type */
    private String type;

    /** Person in Charge */
    private String personCharge;

    /** Contact Number */
    private String contactNumber;

    /** Column Type */
    private String columnType;

    /** Status */
    private String status;

    /** Description */
    private String description;

    /** Valid Flag */
    private Boolean validFlag;

    /** Delete Flag */
    private Boolean delFlag;

    private Long documentId;
}
