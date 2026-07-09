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

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Logical Model DTO - DP_MODEL
 *
 * @author qdata
 * @date 2025-01-21
 */
@Data
public class DpModelRespDTO {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

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

    @TableField(exist = false)
    private String catName;

    /**
     * Table Type; 1: Detail Table 2: Summary Table 3: Dimension Table 4: Application Table
     */
    @Schema(name = "Table Type 1: Detail Table 2: Summary Table 3: Dimension Table 4: Application Table")
    private String tableType;
    /**
     * Data Warehouse Layer ID
     */
    @Schema(name = "Data Warehouse Layer ID ")
    private Long dataLayerId;
    /**
     * Business Category ID; Only available when table type is not Application Table
     */
    @Schema(name = "Business Category ID Only available when table type is not Application Table")
    private Long businessCategoryId;
    /**
     * Business Category Hierarchy Code
     */
    @Schema(name = "Business Category Hierarchy Code ")
    private String businessCategoryCode;
    /**
     * Data Domain ID; Only available when table type is not Application Table
     */
    @Schema(name = "Data Domain ID Only available when table type is not Application Table")
    private Long dataDomainId;
    /**
     * Theme Domain ID (Theme Planning); Only available when table type is Application Table
     */
    @Schema(name = "Theme Domain ID (Theme Planning) Only available when table type is Application Table")
    private Long themeDomainId;
    /**
     * Theme Domain Hierarchy Code
     */
    @Schema(name = "Theme Domain Hierarchy Code ")
    private String themeDomainCode;
    /**
     * Table Name Case; 1: Uppercase 2: Lowercase
     */
    @Schema(name = "Table Name Case 1: Uppercase 2: Lowercase")
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
    @TableLogic
    private Boolean delFlag;

    @TableField(exist = false)
    private long columnCount;

    /**
     * Datasource Name
     */
    @TableField(exist = false)
    private String datasourceName;

    /**
     * Datasource Type
     */
    @TableField(exist = false)
    private String datasourceType;

    /**
     * Datasource Config (JSON string)
     */
    @TableField(exist = false)
    private String datasourceConfig;

    /**
     * IP
     */
    @TableField(exist = false)
    private String ip;

    /**
     * Port
     */
    @TableField(exist = false)
    private Long port;

    /**
     * Name
     */
    @TableField(exist = false)
    private String documentName;

    /**
     * Code
     */
    @TableField(exist = false)
    private String documentCode;

    /**
     * Document Standard Type
     */
    @TableField(exist = false)
    private String documentType;

    @Schema(name = "Data Warehouse Layer Name")
    @TableField(exist = false)
    private String dataLayerName;

    @Schema(name = "Data Warehouse Layer English Abbreviation")
    @TableField(exist = false)
    private String dataLayerEngName;

    @Schema(name = "Business Name")
    @TableField(exist = false)
    private String businessCategoryName;

    @Schema(name = "Business English Abbreviation")
    @TableField(exist = false)
    private String businessCategoryEngName;

    @Schema(name = "Data Domain Name")
    @TableField(exist = false)
    private String dataDomainName;

    @Schema(name = "Data Domain English Abbreviation")
    @TableField(exist = false)
    private String dataDomainEngName;

    @Schema(name = "Theme Domain Name")
    @TableField(exist = false)
    private String themeDomainName;

    @Schema(name = "Theme Domain English Abbreviation")
    @TableField(exist = false)
    private String themeDomainEngName;

    @Schema(name = "Release Status 1: Not Released 3: Release Success 4: Release Failed")
    @TableField(exist = false)
    private String releaseStatus;

    @Schema(name = "Release Database List")
    @TableField(exist = false, typeHandler = JacksonTypeHandler.class)
    private String releaseDatabaseList;

    @Schema(name = "Creator Phone Number")
    @TableField(exist = false, typeHandler = JacksonTypeHandler.class)
    private String createUserPhoneNumber;

    /**
     * Get table name (concatenated with table naming convention)
     * Rules:
     * 1. Data Warehouse Layer + Business Category + Data Domain + Model Code (non-Application Table)
     * 2. Data Warehouse Layer + Theme Domain + Model Code (Application Table)
     * 3. Convert to Uppercase (1) or Lowercase (2) based on tableCase
     *
     * @return Concatenated table name
     */
    public String getTableName() {
        java.util.List<String> parts = new java.util.ArrayList<>();

        // 1. Data Warehouse Layer
        if (dataLayerEngName != null && !dataLayerEngName.isEmpty()) {
            parts.add(dataLayerEngName);
        }

        // 2. Add different levels based on table type
        if ("4".equals(tableType)) {
            // Application Table: add theme domain
            if (themeDomainEngName != null && !themeDomainEngName.isEmpty()) {
                parts.add(themeDomainEngName);
            }
        } else {
            // Non-Application Table: add business category and data domain
            if (businessCategoryEngName != null && !businessCategoryEngName.isEmpty()) {
                parts.add(businessCategoryEngName);
            }
            if (dataDomainEngName != null && !dataDomainEngName.isEmpty()) {
                parts.add(dataDomainEngName);
            }
        }

        // 3. Add model code
        if (modelName != null && !modelName.isEmpty()) {
            parts.add(modelName);
        }

        // 4. Concatenate and convert case
        String tableName = String.join("_", parts);
        if (tableName != null && !tableName.isEmpty()) {
            // tableCase: 1-Uppercase, 2-Lowercase
            if ("2".equals(tableCase)) {
                return tableName.toLowerCase();
            } else {
                return tableName.toUpperCase();
            }
        }

        return tableName;
    }

}
