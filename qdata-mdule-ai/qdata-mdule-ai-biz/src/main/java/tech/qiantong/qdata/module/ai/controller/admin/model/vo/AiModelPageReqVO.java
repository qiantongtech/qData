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

package tech.qiantong.qdata.module.ai.controller.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 模型管理 Request VO 对象 AI_MODEL
 *
 * @author FXB
 * @date 2026-04-01
 */
@Schema(description = "模型管理 Request VO")
@Data
public class AiModelPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "模型名称;例如 qwen-max", example = "")
    private String name;

    @Schema(description = "平台;1:通义千问 2:DeepSeek", example = "")
    private String platform;

    @Schema(description = "API地址", example = "")
    private String apiUrl;

    @Schema(description = "API秘钥", example = "")
    private String apiKey;

    @Schema(description = "排序", example = "")
    private Long sortOrder;

    @Schema(description = "描述", example = "")
    private String description;

    @Schema(description = "是否有效;0：无效，1：有效", example = "")
    private Boolean validFlag;



}
