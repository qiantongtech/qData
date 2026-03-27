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

package tech.qiantong.qdata.module.dm.controller.admin.dm.vo;

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
 * 数仓分层管理 Response VO 对象 DM_DATA_LAYER
 *
 * @author FXB
 * @date 2026-03-24
 */
@Schema(description = "数仓分层管理 Response VO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DmDataLayerTreeRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    /** parentId */
    private Long parentId;

    @Excel(name = "名称")
    @Schema(description = "名称", example = "")
    private String name;

    @Excel(name = "英文缩写")
    @Schema(description = "英文缩写", example = "")
    private String engName;

    @Excel(name = "负责人ID")
    @Schema(description = "负责人ID", example = "")
    private Long ownerUserId;

    @Excel(name = "分类")
    @Schema(description = "分类", example = "")
    private String category;

    @Excel(name = "描述")
    @Schema(description = "描述", example = "")
    private String description;

    @Schema(description = "子列表", example = "")
    private List<DmDataLayerTreeRespVO> children;
}
