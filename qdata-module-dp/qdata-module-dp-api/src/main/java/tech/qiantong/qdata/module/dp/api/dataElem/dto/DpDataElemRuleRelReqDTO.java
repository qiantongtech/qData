package tech.qiantong.qdata.module.dp.api.dataElem.dto;

import lombok.Data;

/**
 * 数据元数据规则关联信息 DTO 对象 DP_DATA_ELEM_RULE_REL
 *
 * @author qdata
 * @date 2025-01-21
 */
@Data
public class DpDataElemRuleRelReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 数据元id */
    private String dataElemId;

    /** 规则类型 */
    private String ruleType;

    /** 规则id */
    private String ruleId;

    /** 规则配置 */
    private String ruleConfig;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
