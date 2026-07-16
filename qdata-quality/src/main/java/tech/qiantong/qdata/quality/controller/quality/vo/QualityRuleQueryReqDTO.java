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

import lombok.Data;
import tech.qiantong.qdata.quality.dal.dataobject.datasource.DaDatasourceDO;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class QualityRuleQueryReqDTO implements Serializable {


    private String id;

    private String tableName;

    private String ruleColumn;

    private List<String> ruleColumns;

    private List<String> showErrorColumns;

    private String dataId;

    private DaDatasourceDO daDatasourceById;

    private String evaColumn;

    private String whereClause;

    private String ruleType;

    /**
     * User parameters
     */
    private String inputValue;

    /**
     * Rule configuration parameters, such as time range, whether to ignore null, whether to ignore case, etc.
     */
    private Map<String, Object> config;

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

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
        computeLimitAndOffset();
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        computeLimitAndOffset();
    }

    private void computeLimitAndOffset() {
        if (this.pageNum != null && this.pageSize != null) {
            this.offset = (this.pageNum - 1) * this.pageSize;
            this.limit = this.pageSize;
        }
    }
}
