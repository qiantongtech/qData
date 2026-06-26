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

package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import com.alibaba.fastjson.JSONArray;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * <P>
 * 用途:
 * </p>
 *
 * @author: FXB
 * @create: 2024-05-24 16:01
 **/
@Data
public class DppOnlDesformDataPageRequestVO extends DppOnlDesformDataBaseRequestVO implements Serializable {

//    @ApiModelProperty(value = "第几页")
    private Integer pageNum;

//    @ApiModelProperty(value = "每页数量")
    private Integer pageSize;

    @Builder(toBuilder = true)
    public DppOnlDesformDataPageRequestVO(Integer datasourceId, String databaseName, String tableNames, JSONArray fieldName, String uniFieldName, Integer pageNum, Integer pageSize) {
        super(datasourceId, databaseName, tableNames, fieldName, uniFieldName);
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }
}
