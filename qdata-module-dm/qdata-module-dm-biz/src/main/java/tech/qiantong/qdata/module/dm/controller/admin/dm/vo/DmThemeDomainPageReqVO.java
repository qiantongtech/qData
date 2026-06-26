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

package tech.qiantong.qdata.module.dm.controller.admin.dm.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 主题域管理 Request VO 对象 DM_THEME_DOMAIN
 *
 * @author FXB
 * @date 2026-03-24
 */
@Schema(description = "主题域管理 Request VO")
@Data
public class DmThemeDomainPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "层级编码", example = "")
    private String code;

    @Schema(description = "名称", example = "")
    private String name;

    @Schema(description = "英文缩写", example = "")
    private String engName;

    @Schema(description = "关联上级ID", example = "")
    private Long parentId;

    @Schema(description = "负责人ID", example = "")
    private Long ownerUserId;

    @Schema(description = "数仓分层ID", example = "")
    private Long dataLayerId;

    @Schema(description = "描述", example = "")
    private String description;




}
