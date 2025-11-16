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
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.module.da.controller.admin.sensitiveLevel.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;

/**
 * 敏感等级 创建/修改 Request VO DA_SENSITIVE_LEVEL
 *
 * @author qdata
 * @date 2025-01-21
 */
@Schema(description = "敏感等级 Response VO")
@Data
public class DaSensitiveLevelSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "敏感级别", example = "")
    @Size(max = 256, message = "敏感级别长度不能超过256个字符")
    private String sensitiveLevel;

    @Schema(description = "敏感规则", example = "")
    @Size(max = 256, message = "敏感规则长度不能超过256个字符")
    private String sensitiveRule;

    @Schema(description = "起始字符位置", example = "")
    private Long startCharLoc;

    @Schema(description = "截止字符位置", example = "")
    private Long endCharLoc;

    @Schema(description = "遮盖字符", example = "")
    @Size(max = 256, message = "遮盖字符长度不能超过256个字符")
    private String maskCharacter;

    @Schema(description = "上下线标识", example = "")
    @Size(max = 256, message = "上下线标识长度不能超过256个字符")
    private String onlineFlag;

    @Schema(description = "描述", example = "")
    @Size(max = 256, message = "描述长度不能超过256个字符")
    private String description;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
