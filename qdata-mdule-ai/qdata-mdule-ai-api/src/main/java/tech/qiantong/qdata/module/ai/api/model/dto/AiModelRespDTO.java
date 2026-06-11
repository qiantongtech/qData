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

package tech.qiantong.qdata.module.ai.api.model.dto;

import lombok.*;

/**
 * 模型管理 DTO 对象 AI_MODEL
 *
 * @author FXB
 * @date 2026-04-01
 */
@Data
public class AiModelRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 模型名称;例如 qwen-max */
    private String name;

    /** 平台;1:通义千问 2:DeepSeek */
    private String platform;

    /** API地址 */
    private String apiUrl;

    /** API秘钥 */
    private String apiKey;

    /** 排序 */
    private Long sortOrder;

    /** 描述 */
    private String description;

    /** 是否有效;0：无效，1：有效 */
    private Boolean validFlag;

    /** 删除标志;1：已删除，0：未删除 */
    private Boolean delFlag;


}
