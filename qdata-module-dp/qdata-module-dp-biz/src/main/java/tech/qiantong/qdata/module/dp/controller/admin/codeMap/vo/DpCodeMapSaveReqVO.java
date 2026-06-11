/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.module.dp.controller.admin.codeMap.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;

/**
 * 数据元代码映射 创建/修改 Request VO DP_CODE_MAP
 *
 * @author qdata
 * @date 2025-01-21
 */
@Schema(description = "数据元代码映射 Response VO")
@Data
public class DpCodeMapSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "数据元id", example = "")
    @Size(max = 256, message = "数据元id长度不能超过256个字符")
    private String dataElemId;

    @Schema(description = "原始值", example = "")
    @Size(max = 256, message = "原始值长度不能超过256个字符")
    private String originalValue;

    @Schema(description = "代码名", example = "")
    @Size(max = 256, message = "代码名长度不能超过256个字符")
    private String codeName;

    @Schema(description = "代码值", example = "")
    @Size(max = 256, message = "代码值长度不能超过256个字符")
    private String codeValue;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
