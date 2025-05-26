package tech.qiantong.qdata.module.att.convert.rule;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.att.controller.admin.rule.vo.AttAuditRulePageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.rule.vo.AttAuditRuleRespVO;
import tech.qiantong.qdata.module.att.controller.admin.rule.vo.AttAuditRuleSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.rule.AttAuditRuleDO;

import java.util.List;

/**
 * 稽查规则 Convert
 *
 * @author qdata
 * @date 2025-01-20
 */
@Mapper
public interface AttAuditRuleConvert {
    AttAuditRuleConvert INSTANCE = Mappers.getMapper(AttAuditRuleConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param attAuditRulePageReqVO 请求参数
     * @return AttAuditRuleDO
     */
     AttAuditRuleDO convertToDO(AttAuditRulePageReqVO attAuditRulePageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param attAuditRuleSaveReqVO 保存请求参数
     * @return AttAuditRuleDO
     */
     AttAuditRuleDO convertToDO(AttAuditRuleSaveReqVO attAuditRuleSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param attAuditRuleDO 实体对象
     * @return AttAuditRuleRespVO
     */
     AttAuditRuleRespVO convertToRespVO(AttAuditRuleDO attAuditRuleDO);

    /**
     * DOList 转换为 RespVOList
     * @param attAuditRuleDOList 实体对象列表
     * @return List<AttAuditRuleRespVO>
     */
     List<AttAuditRuleRespVO> convertToRespVOList(List<AttAuditRuleDO> attAuditRuleDOList);
}
