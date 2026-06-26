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

package tech.qiantong.qdata.module.dg.convert.desensitizeRules;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeRules.vo.DgDesensitizeRulePageReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeRules.vo.DgDesensitizeRuleRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeRules.vo.DgDesensitizeRuleSaveReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.desensitizeRules.DgDesensitizeRuleDO;

/**
 * 脱敏规则 Convert
 *
 * @author qdata
 * @date 2026-04-10
 */
@Mapper
public interface DgDesensitizeRuleConvert {
    DgDesensitizeRuleConvert INSTANCE = Mappers.getMapper(DgDesensitizeRuleConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param dgDesensitizeRulePageReqVO 请求参数
     * @return DgDesensitizeRuleDO
     */
     DgDesensitizeRuleDO convertToDO(DgDesensitizeRulePageReqVO dgDesensitizeRulePageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param dgDesensitizeRuleSaveReqVO 保存请求参数
     * @return DgDesensitizeRuleDO
     */
     DgDesensitizeRuleDO convertToDO(DgDesensitizeRuleSaveReqVO dgDesensitizeRuleSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param dgDesensitizeRuleDO 实体对象
     * @return DgDesensitizeRuleRespVO
     */
     DgDesensitizeRuleRespVO convertToRespVO(DgDesensitizeRuleDO dgDesensitizeRuleDO);

    /**
     * DOList 转换为 RespVOList
     * @param dgDesensitizeRuleDOList 实体对象列表
     * @return List<DgDesensitizeRuleRespVO>
     */
     List<DgDesensitizeRuleRespVO> convertToRespVOList(List<DgDesensitizeRuleDO> dgDesensitizeRuleDOList);
}
