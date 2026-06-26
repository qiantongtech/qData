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

package tech.qiantong.qdata.module.dp.convert.dataElem;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemRuleRelPageReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemRuleRelRespVO;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemRuleRelSaveReqVO;
import tech.qiantong.qdata.module.dp.dal.dataobject.dataElem.DpDataElemRuleRelDO;

import java.util.List;

/**
 * 数据元数据规则关联信息 Convert
 *
 * @author qdata
 * @date 2025-01-21
 */
@Mapper
public interface DpDataElemRuleRelConvert {
    DpDataElemRuleRelConvert INSTANCE = Mappers.getMapper(DpDataElemRuleRelConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param dpDataElemRuleRelPageReqVO 请求参数
     * @return DpDataElemRuleRelDO
     */
     DpDataElemRuleRelDO convertToDO(DpDataElemRuleRelPageReqVO dpDataElemRuleRelPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param dpDataElemRuleRelSaveReqVO 保存请求参数
     * @return DpDataElemRuleRelDO
     */
     DpDataElemRuleRelDO convertToDO(DpDataElemRuleRelSaveReqVO dpDataElemRuleRelSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param dpDataElemRuleRelDO 实体对象
     * @return DpDataElemRuleRelRespVO
     */
     DpDataElemRuleRelRespVO convertToRespVO(DpDataElemRuleRelDO dpDataElemRuleRelDO);

    /**
     * DOList 转换为 RespVOList
     * @param dpDataElemRuleRelDOList 实体对象列表
     * @return List<DpDataElemRuleRelRespVO>
     */
     List<DpDataElemRuleRelRespVO> convertToRespVOList(List<DpDataElemRuleRelDO> dpDataElemRuleRelDOList);
}
