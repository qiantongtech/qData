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

package tech.qiantong.qdata.module.att.convert.rule;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.att.controller.admin.rule.vo.AttAuditRulePageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.rule.vo.AttAuditRuleRespVO;
import tech.qiantong.qdata.module.att.controller.admin.rule.vo.AttAuditRuleSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.rule.AttAuditRuleDO;

import java.util.List;

/**
 * Audit Rule Convert
 *
 * @author qdata
 * @date 2025-01-20
 */
@Mapper
public interface AttAuditRuleConvert {
    AttAuditRuleConvert INSTANCE = Mappers.getMapper(AttAuditRuleConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param attAuditRulePageReqVO Request parameters
     * @return AttAuditRuleDO
     */
     AttAuditRuleDO convertToDO(AttAuditRulePageReqVO attAuditRulePageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param attAuditRuleSaveReqVO Save request parameters
     * @return AttAuditRuleDO
     */
     AttAuditRuleDO convertToDO(AttAuditRuleSaveReqVO attAuditRuleSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param attAuditRuleDO Entity object
     * @return AttAuditRuleRespVO
     */
     AttAuditRuleRespVO convertToRespVO(AttAuditRuleDO attAuditRuleDO);

    /**
     * Convert DOList to RespVOList
     * @param attAuditRuleDOList Entity object list
     * @return List<AttAuditRuleRespVO>
     */
     List<AttAuditRuleRespVO> convertToRespVOList(List<AttAuditRuleDO> attAuditRuleDOList);
}
