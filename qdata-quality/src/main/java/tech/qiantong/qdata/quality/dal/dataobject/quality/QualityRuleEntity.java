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

// QualityRuleEntity 示例定义（用于策略类）
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QualityRuleEntity {

    //表名称
    //字段名
    /** 稽查规则编号 */
    private String ruleCode;
    /** 稽查规则名称 */
    private String ruleName;
    /** 质量维度*/
    private String dimensionType;
    /** 规则描述 */
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
         *      * 是否忽略空值
         *      * true 表示忽略；false 表示不忽略
        private Boolean ignoreNullValue;
         *
          *      * 是否忽略大小写
          *      * true 表示忽略；false 表示不忽略
        private Boolean ignoreCase;
         *
          *      * 字段填写策略：
          *      * 1 表示字段必须全部填写（部分为空为异常）
          *      * 2 表示字段要么全部为空，要么全部填写（部分填写为异常）
        private Integer fillStrategy;
         *
          *      * 是否包含最大最小值
          *      * true 表示包含（>=、<=）；false 表示不包含（>、<）
        private Boolean includeRangeBound;

        是否忽略整数值，* true 表示忽略；false 表示不忽略
        private Boolean skipInteger;
     *
     *
     */
    private Map<String, Object> config;

    // 可选：组合唯一性支持多个列
    private List<String> ruleColumns;
    private List<String> showErrorColumns;



    public QualityRuleEntity(Map<String, Object> stringObjectMap){

        // TODO
        //表字段需要兼容

    }

    public QualityRuleEntity(QualityRuleQueryReqDTO queryReqDTO) {
        this.tableName = queryReqDTO.getTableName();
        this.config = queryReqDTO.getConfig();
        this.whereClause = queryReqDTO.getWhereClause();
        this.ruleColumn = queryReqDTO.getEvaColumn(); // 若是单字段规则，取 evaColumn

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

//        this.id = String.valueOf(dppQualityTaskEvaluateDO.getId());

        this.ruleType = dppQualityTaskEvaluateDO.getRuleType();
        this.tableName = dppQualityTaskEvaluateDO.getTableName();
        this.whereClause = dppQualityTaskEvaluateDO.getWhereClause();
        this.ruleColumn = dppQualityTaskEvaluateDO.getEvaColumn(); // 若是单字段规则，取 evaColumn

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
