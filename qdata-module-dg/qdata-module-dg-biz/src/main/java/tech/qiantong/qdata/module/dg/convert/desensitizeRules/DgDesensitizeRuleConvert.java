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
