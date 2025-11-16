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

package tech.qiantong.qdata.module.dp.api.model.dto;

import lombok.Data;

/**
 * 逻辑模型属性信息 DTO 对象 DP_MODEL_COLUMN
 *
 * @author qdata
 * @date 2025-01-21
 */
@Data
public class DpModelColumnRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 逻辑模型表ID */
    private Long modelId;

    /** 英文名称 */
    private String engName;

    /** 中文名称 */
    private String cnName;

    /** 数据类型 */
    private String columnType;

    /** 属性长度 */
    private Long columnLength;

    /** 小数长度 */
    private Long columnScale;

    /** 默认值 */
    private String defaultValue;

    /** 是否主键 */
    private String pkFlag;

    /** 是否必填 */
    private String nullableFlag;

    /** 排序 */
    private Long sortOrder;

    /** 权威部门 */
    private String authorityDept;

    /** 数据元id */
    private Long dataElemId;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
