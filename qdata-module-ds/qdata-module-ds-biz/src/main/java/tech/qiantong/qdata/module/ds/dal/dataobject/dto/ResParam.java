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

package tech.qiantong.qdata.module.ds.dal.dataobject.dto;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;


@Data
public class ResParam implements Serializable {

    private static final long serialVersionUID=1L;

    private String fieldId;
    /**
     * 字段名称
     */
    @NotBlank(message = "字段名称不能为空")
    private String fieldName;

    /**
     * 描述
     */
    @NotBlank(message = "描述不能为空")
    private String fieldComment;

    /**
     * 数据类型
     */
    @NotBlank(message = "数据类型不能为空")
    private String dataType;

    /**
     * 时间格式 例如 yyyy-MM-dd HH:mm:ss
     */
    private String dateFormat;

    private String exampleValue;

//    @ApiModelProperty(value = "示例值")
//    @NotBlank(message = "示例值不能为空")
//    private String exampleValue;

    private String fieldAliasName;

    private List<ResParam> resParamList;

}
