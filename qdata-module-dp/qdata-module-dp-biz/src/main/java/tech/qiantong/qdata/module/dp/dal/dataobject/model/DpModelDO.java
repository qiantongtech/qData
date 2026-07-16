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

package tech.qiantong.qdata.module.dp.dal.dataobject.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * Logical Model DO - DP_MODEL
 *
 * @author qdata
 * @date 2025-01-21
 */
@Data
@TableName(value = "DP_MODEL")
// Used for auto-increment primary keys in Oracle, PostgreSQL, Kingbase, DB2, H2 databases. Can be omitted for MySQL and similar databases.
// @KeySequence("DP_MODEL_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DpModelDO extends BaseEntity {
    @TableField(exist = false)
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
    @Schema(name = "表类型 1:明细表 2:汇总表 3:维度表 4:应用表")
    private String tableType;
    /**
     * Data Warehouse Layer ID
     */
    @Schema(name = "数仓分层id ")
    private Long dataLayerId;
    /**
     * Business Category ID; only has value when table type is non-application table
     */
    @Schema(name = "业务分类id 只有表类型为非应用表是才有值")
    private Long businessCategoryId;
    /**
     * Business Category Level Code
     */
    @Schema(name = "业务分类层级编码 ")
    private String businessCategoryCode;
    /**
     * Data Domain ID; only has value when table type is non-application table
     */
    @Schema(name = "数据分域id 只有表类型为非应用表是才有值")
    private Long dataDomainId;
    /**
     * Theme Domain ID (Theme Planning); only has value when table type is application table
     */
    @Schema(name = "所属主题id（主题规划） 只有表类型为应用表是才有值")
    private Long themeDomainId;
    /**
     * Theme Domain Level Code
     */
    @Schema(name = "所属主题层级编码 ")
    private String themeDomainCode;
    /**
     * Table Name Case; 1: Uppercase 2: Lowercase
     */
    @Schema(name = "表名大小写 1：大写 2：小写")
    private String tableCase;

    /**
     * Status
     */
    private String status;

    /**
     * Creation Method
     */
    private String createType;

    /**
     * Datasource ID
     */
    private Long datasourceId;

    private Long documentId;

    /**
     * Contact Person
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
     * Datasource Config (JSON String)
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
     * Name
     */
    @TableField(exist = false)
    private String documentCode;

    /**
     * Document standard type field
     */
    @TableField(exist = false)
    private String documentType;

    @Schema(name = "数仓分层名称")
    @TableField(exist = false)
    private String dataLayerName;

    @Schema(name = "数仓分层英文缩写")
    @TableField(exist = false)
    private String dataLayerEngName;

    @Schema(name = "业务名称")
    @TableField(exist = false)
    private String businessCategoryName;

    @Schema(name = "业务英文缩写")
    @TableField(exist = false)
    private String businessCategoryEngName;

    @Schema(name = "数据分域名称")
    @TableField(exist = false)
    private String dataDomainName;

    @Schema(name = "数据分域英文缩写")
    @TableField(exist = false)
    private String dataDomainEngName;

    @Schema(name = "所属主题名称")
    @TableField(exist = false)
    private String themeDomainName;

    @Schema(name = "所属主题英文缩写")
    @TableField(exist = false)
    private String themeDomainEngName;

    @Schema(name = "发布状态 1:未发布 3:发布成功 4:发布失败")
    @TableField(exist = false)
    private String releaseStatus;

    @Schema(name = "发布发布数据源列表")
    @TableField(exist = false, typeHandler = JacksonTypeHandler.class)
    private String releaseDatabaseList;

    @Schema(name = "创建人联系电话")
    @TableField(exist = false)
    private String createUserPhoneNumber;


    @Schema(name = "修改人联系电话")
    @TableField(exist = false)
    private String updateUserPhoneNumber;

    /**
     * Get table name (concatenated with table naming convention)
     * Rules:
     * 1. Data Warehouse Layer + Business Category + Data Domain + Model Code (non-application table)
     * 2. Data Warehouse Layer + Theme Domain + Model Code (application table)
     * 3. Convert to uppercase(1) or lowercase(2) based on tableCase
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
            // Application table: add theme domain
            if (themeDomainEngName != null && !themeDomainEngName.isEmpty()) {
                parts.add(themeDomainEngName);
            }
        } else {
            // Non-application table: add business category and data domain
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
            // tableCase: 1-uppercase, 2-lowercase
            if ("2".equals(tableCase)) {
                return tableName.toLowerCase();
            } else {
                return tableName.toUpperCase();
            }
        }

        return tableName;
    }

}
