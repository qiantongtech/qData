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

package tech.qiantong.qdata.module.dp.api.model.dto;

import lombok.Data;

/**
 * Logical Model DTO - DP_MODEL
 *
 * @author qdata
 * @date 2025-01-21
 */
@Data
public class DpModelReqDTO {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long ID;

    /**
     * Model Code
     */
    private String modelName;

    /**
     * Model Name
     */
    private String modelComment;

    /**
     * Category Code
     */
    private String catCode;

    /**
     * Table Type; 1: Detail Table 2: Summary Table 3: Dimension Table 4: Application Table
     */
    private String tableType;
    /**
     * Data Warehouse Layer ID
     */
    private Long dataLayerId;
    /**
     * Business Category ID; Only available when table type is not Application Table
     */
    private Long businessCategoryId;
    /**
     * Business Category Hierarchy Code
     */
    private String businessCategoryCode;
    /**
     * Data Domain ID; Only available when table type is not Application Table
     */
    private Long dataDomainId;
    /**
     * Theme Domain ID (Theme Planning); Only available when table type is Application Table
     */
    private Long themeDomainId;
    /**
     * Theme Domain Hierarchy Code
     */
    private String themeDomainCode;
    /**
     * Table Name Case; 1: Uppercase 2: Lowercase
     */
    private String tableCase;

    /**
     * Status
     */
    private String status;

    /**
     * Creation Type
     */
    private String createType;

    /**
     * Datasource ID
     */
    private Long datasourceId;

    private Long documentId;

    /**
     * Contact
     */
    private String contact;

    /**
     * Contact Number
     */
    private String contactNumber;

    /**
     * Description
     */
    private String description;

    /**
     * Valid Flag
     */
    private Boolean validFlag;

    /**
     * Delete Flag
     */
    private Boolean delFlag;


}
