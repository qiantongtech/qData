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

package tech.qiantong.qdata.module.dpp.controller.admin.qa.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CheckErrorDataReqDTO implements Serializable {

    /**
     * Report ID
     */
    private String reportId;

    /**
     * 1-Modify Data
     * 2-Modify Remark
     * 3-Modify Status (Ignore)
     */
    private String updateType;
    private String remark;

    private Integer pageNum;
    private Integer pageSize;


    private Integer repair;

    private Map<String,Object> oldData;

    private Map<String,Object> updatedData;

    private Long datasourceId;
    private String tableName;

    private String dataIssueId;

    private String id;

    private List<String> errorDataId;

    /**
     * Query parameters
     */
    private Map<String,Object> keyWordData;

}
