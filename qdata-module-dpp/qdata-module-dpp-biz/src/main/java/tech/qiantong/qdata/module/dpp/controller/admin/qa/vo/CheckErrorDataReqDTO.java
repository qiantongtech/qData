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
     * 报告id
     */
    private String reportId;

    /**
     * 1-修改数据
     * 2-修改备注
     * 3-修改状态（忽略）
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
     * 查询参数
     */
    private Map<String,Object> keyWordData;

}
