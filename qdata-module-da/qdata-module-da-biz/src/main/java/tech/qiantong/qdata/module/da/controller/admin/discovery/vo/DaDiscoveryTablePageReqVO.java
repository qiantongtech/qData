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
 * 数据发现库信息 Request VO 对象 DA_DISCOVERY_TABLE
 *
 * @author qdata
 * @date 2025-02-11
 */
@Schema(description = "数据发现库信息 Request VO")
@Data
public class DaDiscoveryTablePageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "任务id", example = "")
    private Long taskId;

    @Schema(description = "表名称", example = "")
    private String tableName;

    @Schema(description = "表描述", example = "")
    private String tableComment;

    @Schema(description = "数据量", example = "")
    private Long dataCount;

    @Schema(description = "字段量", example = "")
    private Long fieldCount;

    @Schema(description = "表结构标识", example = "")
    private String changeFlag;

    @Schema(description = "状态", example = "")
    private String status;

    @Schema(description = "是否忽略", example = "")
    private String ignoreFlag;

    private String keyword;




}
