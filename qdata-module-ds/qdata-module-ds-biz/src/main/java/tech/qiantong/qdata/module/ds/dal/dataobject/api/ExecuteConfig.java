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

package tech.qiantong.qdata.module.ds.dal.dataobject.api;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
public class ExecuteConfig implements Serializable {

    private static final long serialVersionUID=1L;

    @NotBlank(message = "数据源不能为空")
    private String sourceId;

    @NotNull(message = "配置方式不能为空")
    private String apiServiceType;

    private String tableId;

    private String tableName;

    private String dbName;

    private String sid;

    private String dbType;


    @Valid
    private List<FieldParam> fieldParams;

    /**
     * 解析SQL
     */
    private String sqlText;
}
