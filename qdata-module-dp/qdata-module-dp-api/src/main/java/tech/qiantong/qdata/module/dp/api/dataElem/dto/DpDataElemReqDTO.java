/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 * You may use qData for commercial purposes, but you may not remove, hide,
 * modify, or replace the qData logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qData as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qdata.module.dp.api.dataElem.dto;

import lombok.Data;

/**
 * 数据元 DTO 对象 DP_DATA_ELEM
 *
 * @author qdata
 * @date 2025-01-21
 */
@Data
public class DpDataElemReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 编码 */
    private String code;

    /** 名称 */
    private String name;

    /** 英文名称 */
    private String engName;

    /** 类目编码 */
    private String catCode;

    /** 类型 */
    private String type;

    /** 责任人 */
    private String personCharge;

    /** 联系电话 */
    private String contactNumber;

    /** 字段类型 */
    private String columnType;

    /** 状态 */
    private String status;

    /** 描述 */
    private String description;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;

    private Long documentId;
}
