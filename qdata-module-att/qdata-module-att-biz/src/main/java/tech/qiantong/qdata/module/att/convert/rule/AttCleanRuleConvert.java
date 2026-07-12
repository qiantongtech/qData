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
import tech.qiantong.qdata.module.att.controller.admin.rule.vo.AttCleanRulePageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.rule.vo.AttCleanRuleRespVO;
import tech.qiantong.qdata.module.att.controller.admin.rule.vo.AttCleanRuleSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.rule.AttCleanRuleDO;

import java.util.List;

/**
 * Cleaning Rule Convert
 *
 * @author qdata
 * @date 2025-01-20
 */
@Mapper
public interface AttCleanRuleConvert {
    AttCleanRuleConvert INSTANCE = Mappers.getMapper(AttCleanRuleConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param attCleanRulePageReqVO Request parameters
     * @return AttCleanRuleDO
     */
     AttCleanRuleDO convertToDO(AttCleanRulePageReqVO attCleanRulePageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param attCleanRuleSaveReqVO Save request parameters
     * @return AttCleanRuleDO
     */
     AttCleanRuleDO convertToDO(AttCleanRuleSaveReqVO attCleanRuleSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param attCleanRuleDO Entity object
     * @return AttCleanRuleRespVO
     */
     AttCleanRuleRespVO convertToRespVO(AttCleanRuleDO attCleanRuleDO);

    /**
     * Convert DOList to RespVOList
     * @param attCleanRuleDOList Entity object list
     * @return List<AttCleanRuleRespVO>
     */
     List<AttCleanRuleRespVO> convertToRespVOList(List<AttCleanRuleDO> attCleanRuleDOList);
}
