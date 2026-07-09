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

package tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * Data Asset - External API - Parameter Create/Update Request VO DA_ASSET_API_PARAM
 *
 * @author qdata
 * @date 2025-04-14
 */
@Schema(description = "数据资产-外部API-参数 Response VO")
@Data
public class DaAssetApiParamSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "API id", example = "")
    private Long apiId;

    @Schema(description = "父级id", example = "")
    private Long parentId;

    @Schema(description = "参数名称", example = "")
    @Size(max = 256, message = "参数名称长度不能超过256个字符")
    private String name;

    @Schema(description = "参数类型", example = "")
    @Size(max = 256, message = "参数类型长度不能超过256个字符")
    private String type;

    @Schema(description = "数据默认值", example = "")
    private String defaultValue;
    @Schema(description = "示例值", example = "")
    private String exampleValue;
    @Schema(description = "描述", example = "")
    private String description;

    @Schema(description = "是否必填", example = "")
    @Size(max = 256, message = "是否必填长度不能超过256个字符")
    private String requestFlag;

    @Schema(description = "字段类型", example = "")
    @Size(max = 256, message = "字段类型长度不能超过256个字符")
    private String columnType;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;

    @TableField(exist = false)
    private List<DaAssetApiParamSaveReqVO> daAssetApiParamList;

}
