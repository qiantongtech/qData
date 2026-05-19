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

package tech.qiantong.qdata.module.dg.controller.admin.desensitizeRules.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 脱敏规则 Request VO 对象 DG_DESENSITIZE_RULE
 *
 * @author qdata
 * @date 2026-04-10
 */
@Schema(description = "脱敏规则 Request VO")
@Data
public class DgDesensitizeRulePageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "分级名称", example = "")
    private String name;

    @Schema(description = "数据分类ID", example = "")
    private Long dataCategoryId;

    @Schema(description = "应用场景;1：数据资产  2：数据查询  3：数据服务", example = "")
    private String applicationScene;

    @Schema(description = "脱敏方式;1：底层脱敏  2：展示脱敏", example = "")
    private String maskType;

    @Schema(description = "替换规则", example = "")
    private String replaceRule;

    @Schema(description = "替换内容", example = "")
    private String replaceContent;

    @Schema(description = "排序", example = "")
    private Long sortOrder;

    @Schema(description = "描述", example = "")
    private String description;

    @Schema(description = "是否有效;0：无效，1：有效", example = "")
    private Boolean validFlag;



}
