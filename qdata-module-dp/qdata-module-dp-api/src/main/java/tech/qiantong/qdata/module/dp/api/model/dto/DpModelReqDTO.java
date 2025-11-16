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
 * 逻辑模型 DTO 对象 DP_MODEL
 *
 * @author qdata
 * @date 2025-01-21
 */
@Data
public class DpModelReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long ID;

    /** 模型编码 */
    private String modelName;

    /** 模型名称 */
    private String modelComment;

    /** 类目编码 */
    private String catCode;

    /** 状态 */
    private String status;

    /** 创建方式 */
    private String createType;

    /** 数据源id */
    private Long datasourceId;

    private Long documentId;

    /** 联系人 */
    private String contact;

    /** 联系电话 */
    private String contactNumber;

    /** 描述 */
    private String description;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
