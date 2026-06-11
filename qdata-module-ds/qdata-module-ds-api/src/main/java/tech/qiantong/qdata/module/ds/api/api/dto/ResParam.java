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

package tech.qiantong.qdata.module.ds.api.api.dto;


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

    private String exampleValue;

//    @ApiModelProperty(value = "示例值")
//    @NotBlank(message = "示例值不能为空")
//    private String exampleValue;

    private String fieldAliasName;

    private List<ResParam> resParamList;

}
