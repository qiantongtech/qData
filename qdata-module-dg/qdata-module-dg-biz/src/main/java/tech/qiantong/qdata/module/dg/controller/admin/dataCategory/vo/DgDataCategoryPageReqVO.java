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

package tech.qiantong.qdata.module.dg.controller.admin.dataCategory.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 数据分类 Request VO 对象 DG_DATA_CATEGORY
 *
 * @author qdata
 * @date 2026-04-07
 */
@Schema(description = "数据分类 Request VO")
@Data
public class DgDataCategoryPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
    @Schema(description = "ID", example = "")
    private Long id;
    @Schema(description = "类目id", example = "")
    private Long catId;

    @Schema(description = "类目编码", example = "")
    private String catCode;

    @Schema(description = "分类名称", example = "")
    private String name;

    /**
     * 分类名称缩写名
     */
    @Schema(description = "分类名称缩写名", example = "")
    private String shortName;

    @Schema(description = "数据分级", example = "")
    private Long dataLevelId;

    @Schema(description = "任务优先级;HIGHEST,HIGH,MEDIUM,LOW,LOWEST", example = "")
    private String priority;

    @Schema(description = "描述", example = "")
    private String description;

    /**
     * 是否有效
     */
    private Boolean validFlag;

    @Schema(description = "脱敏配置（0:否 1:是）", example = "")
    private String desensitizationRulesFlag;

    @Schema(description = "脱敏规则id")
    private Long desensitizationRulesId;

}
