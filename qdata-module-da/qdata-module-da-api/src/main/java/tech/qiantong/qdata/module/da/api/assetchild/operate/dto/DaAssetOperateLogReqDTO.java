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

package tech.qiantong.qdata.module.da.api.assetchild.operate.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * Data Asset Operation Log DTO DA_ASSET_OPERATE_LOG
 *
 * @author qdata
 * @date 2025-05-09
 */
@Data
public class DaAssetOperateLogReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Asset ID */
    private Long assetId;

    /** Datasource ID */
    private Long datasourceId;

    /** Table Name */
    private String tableName;

    /** Table Comment/Table Description */
    private String tableComment;

    /** Operation Type */
    private String operateType;

    /** Operation Time */
    private Date operateTime;

    /** Execute Time */
    private Date executeTime;

    /** Data Before Update (JSON) */
    private String updateBefore;

    /** Data After Update (JSON) */
    private String updateAfter;

    /** Field Names */
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
    private Boolean delFlag;

    @Schema(description = "更新条件JSON MD5字符串", example = "")
    private String updateWhereMd5;


}
