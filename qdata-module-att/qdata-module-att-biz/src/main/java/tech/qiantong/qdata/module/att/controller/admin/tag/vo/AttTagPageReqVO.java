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
