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

package tech.qiantong.qdata.module.ds.dal.dataobject.dto;

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
     * 参数名称
     */
    @NotBlank(message = "参数名称不能为空")
    private String paramName;

    /**
     * 是否为空
     */
    @NotNull(message = "是否为空不能为空")
    private String nullable;

    /**
     * 描述
     */
    @NotBlank(message = "描述不能为空")
    private String paramComment;

    /**
     * 操作符
     */
    @NotNull(message = "操作符不能为空")
    private String whereType;

    /**
     * 参数类型
     */
    @NotBlank(message = "参数类型不能为空")
    private String paramType;

    /**
     * 示例值
     */
    private String exampleValue;

    /**
     * 默认值
     */
    private String defaultValue;


    /**
     * 默认值
     */
    private List<ReqParam> ReqParamlist;

}
