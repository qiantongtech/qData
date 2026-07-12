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

package tech.qiantong.qdata.module.att.controller.admin.cat.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * Data Discovery Task Category Management Request VO ATT_DISCOVER_TASK_CAT
 *
 * @author qdata
 * @date 2025-01-20
 */
@Schema(description = "数据发现任务类目管理 Request VO")
@Data
public class AttDiscoverTaskCatPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
    @Schema(description = "项目id")
    private Long projectId;

    @Schema(description = "项目code")
    private String projectCode;
    @Schema(description = "类别名称", example = "")
    private String name;
    @Schema(description = "关联上级ID", example = "")
    private Long parentId;
    @Schema(description = "类别编码", example = "")
    private String code;








}
