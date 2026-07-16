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

package tech.qiantong.qdata.module.da.dal.dataobject.assetchild.operate;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * Data Asset Operation Log DO - DA_ASSET_OPERATE_LOG
 *
 * @author qdata
 * @date 2025-05-09
 */
@Data
@TableName(value = "DA_ASSET_OPERATE_LOG")
// Used for auto-increment primary keys in Oracle, PostgreSQL, Kingbase, DB2, H2 databases. Not needed for MySQL and similar databases.
// @KeySequence("DA_ASSET_OPERATE_LOG_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DaAssetOperateLogDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** Asset ID */
    private Long assetId;

    /** Datasource ID */
    private Long datasourceId;

    /** Table Name */
    private String tableName;

    /** Table Comment / Table Description */
    private String tableComment;

    /** Operation Type */
    private String operateType;

    /** Operation Time */
    private Date operateTime;

    /** Execution Time */
    private Date executeTime;

    /** Data Before Update (JSON) */
    private String updateBefore;

    /** Data After Update (JSON) */
    private String updateAfter;

    /** Fields */
    private String fieldNames;

    /** Import File URL */
    private String fileUrl;

    /** Import File Name */
    private String fileName;

    /** Status */
    private String status;

    /** Valid Flag */
    private Boolean validFlag;

    /** Delete Flag */
    @TableLogic
    private Boolean delFlag;

    @Schema(description = "更新条件JSON MD5字符串", example = "")
    private String updateWhereMd5;



    /** User Name */
    @TableField(exist = false)
    private String userName;

    /** Phone Number */
    @TableField(exist = false)
    private String phoneNumber;

    /** Nickname */
    @TableField(exist = false)
    private String nickName;
}
