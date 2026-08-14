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

package tech.qiantong.qdata.module.system.controller.admin.updater.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * 版本跟踪 Create/Modify Request VO SYSTEM_VERSION_TRACK
 *
 * @author qdata
 * @date 2026-08-12
 */
@Schema(description = "版本跟踪 Response VO")
@Data
public class SystemVersionTrackSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "项目名称", example = "")
    @Size(max = 256, message = "{length.not.valid}")
    private String name;

    @Schema(description = "项目版本号", example = "")
    @Size(max = 256, message = "{length.not.valid}")
    private String currVersion;

    @Schema(description = "描述", example = "")
    @Size(max = 256, message = "{length.not.valid}")
    private String description;

    @Schema(description = "作者", example = "")
    @Size(max = 256, message = "{length.not.valid}")
    private String author;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "{length.not.valid}")
    private String remark;


}
