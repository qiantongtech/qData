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

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
public class ReqParam implements Serializable {

    private static final long serialVersionUID=1L;

    private String paramId;

    /**
     * Parameter name
     */
    @NotBlank(message = "参数名称不能为空")
    private String paramName;

    /**
     * Whether the value is empty
     */
    @NotNull(message = "是否为空不能为空")
    private String nullable;

    /**
     * Description
     */
    @NotBlank(message = "描述不能为空")
    private String paramComment;

    /**
     * Operator
     */
    @NotNull(message = "操作符不能为空")
    private String whereType;

    /**
     * Parameter type
     */
    @NotBlank(message = "参数类型不能为空")
    private String paramType;

    /**
     * Example value
     */
    private String exampleValue;

    /**
     * Default value
     */
    private String defaultValue;


    /**
     * Default value
     */
    private List<ReqParam> ReqParamlist;

}
