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

package tech.qiantong.qdata.module.da.dal.dataobject.assetchild.geo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * Data Asset - Vector DO - DA_ASSET_GEO
 *
 * @author qdata
 * @date 2025-04-14
 */
@Data
@TableName(value = "DA_ASSET_GEO")
// Used for auto-increment primary keys in Oracle, PostgreSQL, Kingbase, DB2, H2 databases. Not needed for MySQL and similar databases.
// @KeySequence("DA_ASSET_GEO_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DaAssetGeoDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** Asset ID */
    private Long assetId;

    /** File Name */
    private String fileName;

    /** File Path */
    private String fileUrl;

    /** Element Type */
    private String elementType;

    /** Coordinate System */
    private String coordinateSystem;

    @Schema(description = "文件类型", example = "")
    private String fileType;

    /** Valid Flag */
    private Boolean validFlag;

    /** Delete Flag */
    @TableLogic
    private Boolean delFlag;


}
