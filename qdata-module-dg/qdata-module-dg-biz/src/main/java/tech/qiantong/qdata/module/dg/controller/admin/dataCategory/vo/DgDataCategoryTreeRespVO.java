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

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 数据分类 Response VO 对象 DG_DATA_CATEGORY
 *
 * @author qdata
 * @date 2026-04-07
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "数据分类 Response selectTree VO")
@Data
public class DgDataCategoryTreeRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "父id，只有类目有")
    private Long parentId;

    @Schema(description = "类目编码，只有类目有")
    private String catCode;

    @Schema(description = "类型 1:类目 2:分类")
    private String type;

    @Schema(description = "名称")
    private String name;

    /**
     * 脱敏配置（0:否 1:是）
     */
    @Schema(description = "脱敏配置（0:否 1:是）", example = "")
    private String desensitizationRulesFlag;

    @Schema(description = "子")
    private List<DgDataCategoryTreeRespVO> children;
}
