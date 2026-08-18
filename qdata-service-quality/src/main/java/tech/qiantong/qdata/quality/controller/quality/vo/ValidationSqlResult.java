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

package tech.qiantong.qdata.quality.controller.quality.vo;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;
import tech.qiantong.qdata.common.database.core.DbColumn;

import java.io.Serializable;
import java.util.List;

@Data
public class ValidationSqlResult  implements Serializable {
    private List<DbColumn> showErrorColumns;
    List<JSONObject> errorList;
    List<JSONObject> dataList;


    /**
     * Pagination parameters (optional)
     */
    private Integer pageNum;

    private Integer pageSize;



    /**
     * Offset (starting at row number), automatically calculated
     */
    private Integer offset;

    /**
     * Limit number of rows (per page size), set automatically
     */
    private Integer limit;
}
