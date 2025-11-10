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
 * For brand customization, please contact the official team for authorization.
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
 * 如需定制品牌，请通过官方渠道申请品牌授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.module.da.controller.admin.asset.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.qiantong.qdata.common.core.page.PageParam;

import java.util.List;

/**
 * 数据资产 Request VO 对象 DA_ASSET
 *
 * @author lhs
 * @date 2025-01-21
 */
@Schema(description = "数据资产 Request VO")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DaAssetPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
    @Schema(description = "ID", example = "")
    private Long id;
    @Schema(description = "资产名称", example = "")
    private String name;

    @Schema(description = "类目编码", example = "")
    private String catCode;

    @Schema(description = "资产类型", example = "")
    private String type;

    private List<String> themeIdList;

    @Schema(description = "是申请过来的资产还是项目自己生成的资产0：申请，1：自创", example = "")
    private String sourceType;

    @Schema(description = "数据连接id", example = "")
    private String datasourceId;

    @Schema(description = "表名称", example = "")
    private String tableName;

    @Schema(description = "表描述", example = "")
    private String tableComment;

    @Schema(description = "数据量", example = "")
    private Long dataCount;

    @Schema(description = "字段量", example = "")
    private Long fieldCount;

    /** 来源;1:数据发现；2:数据模型； */
    @Schema(description = "来源", example = "")
    private String source;

    @Schema(description = "状态", example = "")
    private String status;

    @Schema(description = "描述", example = "")
    private String description;

    @Schema(description = "项目id", example = "")
    private Long projectId;

    @Schema(description = "项目编码", example = "")
    private String projectCode;

    @Schema(description = "资产id集合", example = "")
    private List<Long> assetIdList;

    @Schema(description = "资产id集合(主题筛选)", example = "")
    private List<Long> themeAssetIdList;

    @Schema(description = "创建类型", example = "")
    private String createType;
}
