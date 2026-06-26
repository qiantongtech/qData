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

package tech.qiantong.qdata.module.da.controller.admin.assetColumn.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.qiantong.qdata.common.core.domain.BaseEntity;
import tech.qiantong.qdata.module.da.dal.dataobject.discovery.DaDiscoveryColumnDO;

import javax.validation.constraints.Size;
import java.util.Set;

/**
 * 数据资产字段 创建/修改 Request VO DA_ASSET_COLUMN
 *
 * @author lhs
 * @date 2025-01-21
 */
@Schema(description = "数据资产字段 Response VO")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DaAssetColumnSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "资产id", example = "")
    @Size(max = 256, message = "资产id长度不能超过256个字符")
    private String assetId;

    @Schema(description = "字段名称/英文名称", example = "")
    @Size(max = 256, message = "字段名称/英文名称长度不能超过256个字符")
    private String columnName;

    @Schema(description = "字段注释/中文名称", example = "")
    @Size(max = 256, message = "字段注释/中文名称长度不能超过256个字符")
    private String columnComment;

    @Schema(description = "数据类型", example = "")
    @Size(max = 256, message = "数据类型长度不能超过256个字符")
    private String columnType;

    @Schema(description = "长度", example = "")
    private Long columnLength;

    @Schema(description = "小数位", example = "")
    private Long columnScale;

    @Schema(description = "是否必填", example = "")
    @Size(max = 256, message = "是否必填长度不能超过256个字符")
    private String nullableFlag;

    @Schema(description = "是否主键", example = "")
    @Size(max = 256, message = "是否主键长度不能超过256个字符")
    private String pkFlag;

    @Schema(description = "默认值", example = "")
    @Size(max = 256, message = "默认值长度不能超过256个字符")
    private String defaultValue;

    @Schema(description = "是否代码", example = "")
    @Size(max = 256, message = "是否代码长度不能超过256个字符")
    private String dataElemCodeFlag;

    @Schema(description = "代码id", example = "")
    @Size(max = 256, message = "代码id长度不能超过256个字符")
    private String dataElemCodeId;

    @Schema(description = "敏感等级id", example = "")
    @Size(max = 256, message = "敏感等级id长度不能超过256个字符")
    private String sensitiveLevelId;

    @Schema(description = "关联数据元", example = "")
    @Size(max = 256, message = "关联数据元长度不能超过256个字符")
    private String relDataElmeFlag;

    @Schema(description = "关联清洗规则", example = "")
    @Size(max = 256, message = "关联清洗规则长度不能超过256个字符")
    private String relCleanFlag;

    @Schema(description = "关联稽查规则", example = "")
    @Size(max = 256, message = "关联稽查规则长度不能超过256个字符")
    private String relAuditFlag;

    @Schema(description = "描述", example = "")
    @Size(max = 256, message = "描述长度不能超过256个字符")
    private String description;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;

    /**
     * 数据元id
     */
    @TableField(exist = false)
    private Set<Long> elementId;

    /**
     * 构造函数，根据 DaDiscoveryColumnDO 对象初始化数据资产字段保存 VO
     *
     * @param discoveryColumnDO 数据发现字段 DO 对象
     */
    public DaAssetColumnSaveReqVO(DaDiscoveryColumnDO discoveryColumnDO) {
        if (discoveryColumnDO != null) {
//            this.id = discoveryColumnDO.getId();
            // assetId 的赋值需根据实际业务逻辑处理，此处暂未映射
            this.columnName = discoveryColumnDO.getColumnName();
            this.columnComment = discoveryColumnDO.getColumnComment();
            this.columnType = discoveryColumnDO.getColumnType();
            this.columnLength = discoveryColumnDO.getColumnLength();
            this.columnScale = discoveryColumnDO.getColumnScale();
            this.nullableFlag = discoveryColumnDO.getNullableFlag();
            this.pkFlag = discoveryColumnDO.getPkFlag();
            this.defaultValue = discoveryColumnDO.getDefaultValue();
            // 其他字段，如 dataElemCodeFlag、dataElemCodeId、sensitiveLevelId、relDataElmeFlag、
            // relCleanFlag、relAuditFlag、description、remark、elementId 等，可根据业务需求补充映射逻辑
        }
    }
}
