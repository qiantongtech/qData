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
