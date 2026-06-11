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
