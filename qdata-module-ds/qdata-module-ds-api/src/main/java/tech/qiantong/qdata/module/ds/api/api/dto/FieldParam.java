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

package tech.qiantong.qdata.module.ds.api.api.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class FieldParam implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 列名
     */
    private String columnName;

    /**
     * 数据类型
     */
    private String dataType;

    /**
     * 数据长度
     */
    private Long dataLength;

    /**
     * 数据精度
     */
    private Long dataPrecision;

    /**
     * 数据小数位
     */
    private Long dataScale;

    /**
     * 是否主键
     */
    private String columnKey;

    /**
     * 是否允许为空
     */
    private String columnNullable;

    /**
     * 列的序号
     */
    private Long columnPosition;

    /**
     * 列默认值
     */
    private String dataDefault;

    /**
     * 列注释
     */
    private String columnComment;

    /**
     * 作为请求参数
     */
    private String reqable;

    /**
     * 作为返回参数
     */
    private String resable;
}
