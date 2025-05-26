package tech.qiantong.qdata.module.att.api.rule.dto;

import lombok.Data;

/**
 * 清洗规则 DTO 对象 ATT_CLEAN_RULE
 *
 * @author qdata
 * @date 2025-01-20
 */
@Data
public class AttCleanRuleReqDTO {

    private static final long serialVersionUID = 1L;

    /** 规则ID */
    private Long id;

    /** 规则名称 */
    private String name;

    /** 规则类型 */
    private String type;

    /** 规则级别 */
    private String level;

    /** 规则描述 */
    private String description;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
