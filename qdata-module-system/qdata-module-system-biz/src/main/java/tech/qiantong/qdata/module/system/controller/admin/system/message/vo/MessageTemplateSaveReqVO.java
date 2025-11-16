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

package tech.qiantong.qdata.module.system.controller.admin.system.message.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 消息模板 创建/修改 Request VO message_template
 *
 * @author qdata
 * @date 2024-10-31
 */
@Schema(description = "消息模板 Response VO")
@Data
public class MessageTemplateSaveReqVO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;


    @Schema(description = "消息标题", example = "")
    @NotBlank(message = "消息标题不能为空")
    @Size(max = 256, message = "消息标题长度不能超过256个字符")
    private String title;


    @Schema(description = "消息模板内容", example = "")
    @NotBlank(message = "消息模板内容不能为空")
    @Size(max = 256, message = "消息模板内容长度不能超过256个字符")
    private String content;


    @Schema(description = "消息类别", example = "")
    @NotNull(message = "消息类别不能为空")
    private Integer category;


    @Schema(description = "消息等级", example = "")
    @NotNull(message = "消息等级不能为空")
    private Integer msgLevel;

}
