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

package tech.qiantong.qdata.module.att.controller.admin.tag.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 标签管理 Request VO 对象 ATT_TAG
 *
 * @author qdata
 * @date 2025-07-11
 */
@Schema(description = "标签管理 Request VO")
@Data
public class AttTagPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
        private Long [] ids;

    @Schema(description = "名称", example = "")
    private String name;

    @Schema(description = "描述", example = "")
    private String description;

    @Schema(description = "类目编码", example = "")
    private String catCode;

   @Schema(description = "类目名称", example = "")
    private String catName;

    @Schema(description = "资产数量", example = "")
    private Long aeestCount;
    private Long aeestId;

    @Schema(description = "状态", example = "")
    private String status;

    @Schema(description = "扩展信息别名", example = "")
    private String alias;

    @Schema(description = "近义词", example = "")
    private String nearSynonyms;

    @Schema(description = "同义词", example = "")
    private String synonyms;




}
