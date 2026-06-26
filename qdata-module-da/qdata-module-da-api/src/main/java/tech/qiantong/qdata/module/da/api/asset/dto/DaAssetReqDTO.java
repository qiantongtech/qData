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

package tech.qiantong.qdata.module.da.api.asset.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 数据资产 DTO 对象 DA_ASSET
 *
 * @author lhs
 * @date 2025-01-21
 */
@Data
public class DaAssetReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 资产名称 */
    private String name;

    @Schema(description = "资产类型", example = "")
    private String type;

    /** 类目编码 */
    private String catCode;

    /** 数据连接id */
    private Long datasourceId;

    /** 表名称 */
    private String tableName;

    /** 表描述 */
    private String tableComment;

    /** 数据量 */
    private Long dataCount;

    /** 字段量 */
    private Long fieldCount;

    /** 来源;1:数据发现；2:数据模型； */
    private String source;

    /** 状态 */
    private String status;

    /** 描述 */
    private String description;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;

    @TableField(exist = false)
    /** 模型ID */
    private Long modelId;


    @Schema(description = "创建类型", example = "")
    private String createType;


}
