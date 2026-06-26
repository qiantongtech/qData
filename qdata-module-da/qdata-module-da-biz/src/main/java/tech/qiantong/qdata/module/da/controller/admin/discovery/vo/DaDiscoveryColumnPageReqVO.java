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

package tech.qiantong.qdata.module.da.controller.admin.discovery.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 数据发现字段 Request VO 对象 DA_DISCOVERY_COLUMN
 *
 * @author qdata
 * @date 2025-02-11
 */
@Schema(description = "数据发现字段 Request VO")
@Data
public class DaDiscoveryColumnPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "任务id", example = "")
    private Long taskId;

    @Schema(description = "数据发现库表id", example = "")
    private Long tableId;

    @Schema(description = "字段名称/英文名称", example = "")
    private String columnName;

    @Schema(description = "字段注释/中文名称", example = "")
    private String columnComment;

    @Schema(description = "数据类型", example = "")
    private String columnType;

    @Schema(description = "长度", example = "")
    private Long columnLength;

    @Schema(description = "小数位", example = "")
    private Long columnScale;

    @Schema(description = "是否必填", example = "")
    private String nullableFlag;

    @Schema(description = "是否主键", example = "")
    private String pkFlag;

    @Schema(description = "默认值", example = "")
    private String defaultValue;




}
