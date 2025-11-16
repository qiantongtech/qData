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

package tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 数据元 Request VO 对象 DP_DATA_ELEM
 *
 * @author qdata
 * @date 2025-01-21
 */
@Schema(description = "数据元 Request VO")
@Data
public class DpDataElemPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
    @Schema(description = "ID", example = "")
    private Long id;

    @Schema(description = "名称", example = "")
    private String name;

    @Schema(description = "英文名称", example = "")
    private String engName;

    @Schema(description = "类目编码", example = "")
    private String catCode;

    @Schema(description = "类型", example = "")
    private String type;

    @Schema(description = "责任人", example = "")
    private String personCharge;

    @Schema(description = "联系电话", example = "")
    private String contactNumber;

    @Schema(description = "字段类型", example = "")
    private String columnType;

    @Schema(description = "状态", example = "")
    private String status;

    @Schema(description = "描述", example = "")
    private String description;

    private Long documentId;
}
