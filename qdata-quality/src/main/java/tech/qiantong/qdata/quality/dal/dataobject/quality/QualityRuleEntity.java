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

package tech.qiantong.qdata.quality.dal.dataobject.quality;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.qiantong.qdata.common.utils.JSONUtils;
import tech.qiantong.qdata.module.da.api.datasource.dto.DaDatasourceRespDTO;
import tech.qiantong.qdata.quality.controller.quality.vo.QualityRuleQueryReqDTO;
import tech.qiantong.qdata.quality.dal.dataobject.datasource.DaDatasourceDO;
import tech.qiantong.qdata.quality.dal.dataobject.qa.DppQualityTaskEvaluateDO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// QualityRuleEntity example definition (for policy classes)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QualityRuleEntity {

    //Table name
    //Field name
    /** Audit rule number */
    private String ruleCode;
    /** Audit rule name */
    private String ruleName;
    /** Quality Dimension*/
    private String dimensionType;
    /** Rule description */
    private String ruleDescription;
    private Long evaluateId;

    private Long taskId;
    private Long taskLogId;

    private String id;
    private String tableName;
    private String whereClause;
    private String ruleColumn;

    private String dataId;
    private DaDatasourceDO daDatasourceById;

    private String ruleType;

    /**
     * {
     *   "conditions": [
     *     {
     *       "leftField": "time1",
     *       "operator": "<=",
     *       "rightField": "time2"
     *     },
     *     {
     *       "leftField": "time2",
     *       "operator": "<",
     *       "rightField": "time3"
     *     }
     *   ],
     *   "allowPartialNull": true
     * }
     *
         * * Whether to ignore null values
         * * true means ignore; false means not ignore
        private Boolean ignoreNullValue;
         *
          * * Whether to ignore case
          * * true means ignore; false means not ignore
        private Boolean ignoreCase;
         *
          * *Field filling strategy:
          * * 1 means that all fields must be filled in (partially empty is an exception)
          * * 2 means that the fields are either all empty or all filled in (partially filled in is an exception)
        private Integer fillStrategy;
         *
          * * Whether to include the maximum and minimum values
          * * true means including (>=, <=); false means not including (>, <)
        private Boolean includeRangeBound;

        Whether to ignore integer values, * true means ignore; false means not ignore
        private Boolean skipInteger;
     *
     *
     */
    private Map<String, Object> config;

    // Optional: Combined uniqueness supports multiple columns
    private List<String> ruleColumns;
    private List<String> showErrorColumns;



    public QualityRuleEntity(Map<String, Object> stringObjectMap){

        // TODO
        //Table fields need to be compatible

    }

    public QualityRuleEntity(QualityRuleQueryReqDTO queryReqDTO) {
        this.tableName = queryReqDTO.getTableName();
        this.config = queryReqDTO.getConfig();
        this.whereClause = queryReqDTO.getWhereClause();
        this.ruleColumn = queryReqDTO.getEvaColumn(); // If it is a single field rule, take evaColumn

        ruleColumns = new ArrayList<>();
        if (queryReqDTO.getEvaColumn() != null && !queryReqDTO.getEvaColumn().trim().isEmpty()) {
            String[] columns = queryReqDTO.getEvaColumn().split(",");
            for (String col : columns) {
                if (col != null && !col.trim().isEmpty()) {
                    this.ruleColumns.add(col.trim());
                }
            }
        }
    }

    public QualityRuleEntity(DppQualityTaskEvaluateDO dppQualityTaskEvaluateDO) {
        this.ruleCode = dppQualityTaskEvaluateDO.getRuleCode();
        this.ruleName = dppQualityTaskEvaluateDO.getRuleName();
        this.dimensionType = dppQualityTaskEvaluateDO.getDimensionType();
        this.ruleDescription = dppQualityTaskEvaluateDO.getRuleDescription();
        this.evaluateId = dppQualityTaskEvaluateDO.getId();
        this.taskId = dppQualityTaskEvaluateDO.getTaskId();

        this.id = String.valueOf(dppQualityTaskEvaluateDO.getId());

        this.ruleType = dppQualityTaskEvaluateDO.getRuleType();
        this.tableName = dppQualityTaskEvaluateDO.getTableName();
        this.whereClause = dppQualityTaskEvaluateDO.getWhereClause();
        this.ruleColumn = dppQualityTaskEvaluateDO.getEvaColumn(); // If it is a single field rule, take evaColumn

        Map<String, Object> map = JSONUtils.convertTaskDefinitionJsonMap(dppQualityTaskEvaluateDO.getRule());

        map.put("errDescription",dppQualityTaskEvaluateDO.getErrDescription());
        map.put("suggestion",dppQualityTaskEvaluateDO.getSuggestion());
        map.put("warningLevel",dppQualityTaskEvaluateDO.getWarningLevel());
        map.put("evaluateName",dppQualityTaskEvaluateDO.getName());

        this.config = map;

         ruleColumns = new ArrayList<>();
        if (dppQualityTaskEvaluateDO.getEvaColumn() != null && !dppQualityTaskEvaluateDO.getEvaColumn().trim().isEmpty()) {
            String[] columns = dppQualityTaskEvaluateDO.getEvaColumn().split(",");
            for (String col : columns) {
                if (col != null && !col.trim().isEmpty()) {
                    this.ruleColumns.add(col.trim());
                }
            }
        }
    }
}
