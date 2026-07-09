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

package tech.qiantong.qdata.module.dg.dal.dataobject.dataCategory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * Data Category DO entity DG_DATA_CATEGORY
 *
 * @author qdata
 * @date 2026-04-07
 */
@Data
@TableName(value = "DG_DATA_CATEGORY")
// Used for auto-increment primary keys in Oracle, PostgreSQL, Kingbase, DB2, H2 databases. Can be omitted for MySQL and similar databases.
// @KeySequence("DG_DATA_CATEGORY_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DgDataCategoryDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * Category ID
     */
    private Long catId;

    /**
     * Category Code
     */
    private String catCode;

    /**
     * Category Name
     */
    private String name;

    /** Category Short Name */
    private String shortName;

    /**
     * Data Level ID
     */
    private Long dataLevelId;

    /**
     * Task Priority; HIGHEST, HIGH, MEDIUM, LOW, LOWEST
     */
    private String priority;

    /**
     * Description
     */
    private String description;

    /**
     * Valid Flag; 0: invalid, 1: valid
     */
    private Boolean validFlag;

    /**
     * Delete Flag; 1: deleted, 0: not deleted
     */
    @TableLogic
    private Boolean delFlag;

    /**
     * Category Name
     */
    @TableField(exist = false)
    private String catName;

    /**
     * Data Level Short Name
     */
    @TableField(exist = false)
    private String dataLevelShortName;

    /**
     * Desensitization Rules Flag (0: No, 1: Yes)
     */
    @TableField(exist = false)
    private String desensitizationRulesFlag;

    @TableField(exist = false)
    @Schema(description = "Desensitization Rule ID")
    private Long desensitizationRulesId;

}
