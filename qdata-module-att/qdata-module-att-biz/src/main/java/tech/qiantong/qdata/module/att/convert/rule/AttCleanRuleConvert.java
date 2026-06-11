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

package tech.qiantong.qdata.module.att.convert.rule;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.att.controller.admin.rule.vo.AttCleanRulePageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.rule.vo.AttCleanRuleRespVO;
import tech.qiantong.qdata.module.att.controller.admin.rule.vo.AttCleanRuleSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.rule.AttCleanRuleDO;

import java.util.List;

/**
 * 清洗规则 Convert
 *
 * @author qdata
 * @date 2025-01-20
 */
@Mapper
public interface AttCleanRuleConvert {
    AttCleanRuleConvert INSTANCE = Mappers.getMapper(AttCleanRuleConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param attCleanRulePageReqVO 请求参数
     * @return AttCleanRuleDO
     */
     AttCleanRuleDO convertToDO(AttCleanRulePageReqVO attCleanRulePageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param attCleanRuleSaveReqVO 保存请求参数
     * @return AttCleanRuleDO
     */
     AttCleanRuleDO convertToDO(AttCleanRuleSaveReqVO attCleanRuleSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param attCleanRuleDO 实体对象
     * @return AttCleanRuleRespVO
     */
     AttCleanRuleRespVO convertToRespVO(AttCleanRuleDO attCleanRuleDO);

    /**
     * DOList 转换为 RespVOList
     * @param attCleanRuleDOList 实体对象列表
     * @return List<AttCleanRuleRespVO>
     */
     List<AttCleanRuleRespVO> convertToRespVOList(List<AttCleanRuleDO> attCleanRuleDOList);
}
