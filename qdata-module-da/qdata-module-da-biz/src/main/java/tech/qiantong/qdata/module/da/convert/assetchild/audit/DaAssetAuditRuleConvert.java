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

package tech.qiantong.qdata.module.da.convert.assetchild.audit;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditRulePageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditRuleRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditRuleSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.audit.DaAssetAuditRuleDO;

import java.util.List;

/**
 * 数据资产质量结果记录 Convert
 *
 * @author qdata
 * @date 2025-05-09
 */
@Mapper
public interface DaAssetAuditRuleConvert {
    DaAssetAuditRuleConvert INSTANCE = Mappers.getMapper(DaAssetAuditRuleConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param daAssetAuditRulePageReqVO 请求参数
     * @return DaAssetAuditRuleDO
     */
     DaAssetAuditRuleDO convertToDO(DaAssetAuditRulePageReqVO daAssetAuditRulePageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param daAssetAuditRuleSaveReqVO 保存请求参数
     * @return DaAssetAuditRuleDO
     */
     DaAssetAuditRuleDO convertToDO(DaAssetAuditRuleSaveReqVO daAssetAuditRuleSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param daAssetAuditRuleDO 实体对象
     * @return DaAssetAuditRuleRespVO
     */
     DaAssetAuditRuleRespVO convertToRespVO(DaAssetAuditRuleDO daAssetAuditRuleDO);

    /**
     * DOList 转换为 RespVOList
     * @param daAssetAuditRuleDOList 实体对象列表
     * @return List<DaAssetAuditRuleRespVO>
     */
     List<DaAssetAuditRuleRespVO> convertToRespVOList(List<DaAssetAuditRuleDO> daAssetAuditRuleDOList);
}
