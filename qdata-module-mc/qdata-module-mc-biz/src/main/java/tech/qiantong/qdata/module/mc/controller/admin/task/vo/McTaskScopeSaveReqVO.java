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
 * Business License: https://docs.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qdata.module.mc.controller.admin.task.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;

/**
 * Collection Scope Create/Modify Request VO MC_TASK_SCOPE
 *
 * @author qdata
 * @date 2025-12-16
 */
@Schema(description = "采集范围 Response VO")
@Data
public class McTaskScopeSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "任务id", example = "")
    private Long taskId;

    @Schema(description = "数据库名称", example = "")
    @Size(max = 256, message = "数据库名称长度不能超过256个字符")
    private String dbName;

    @Schema(description = "模式名", example = "")
    @Size(max = 256, message = "模式名长度不能超过256个字符")
    private String schemaName;

    @Schema(description = "备注", example = "")
    @Size(max = 3000, message = "备注长度不能超过3000个字符")
    private String remark;

    @Schema(description = "描述", example = "")
    @Size(max = 3000, message = "描述长度不能超过3000个字符")
    private String description;


}
