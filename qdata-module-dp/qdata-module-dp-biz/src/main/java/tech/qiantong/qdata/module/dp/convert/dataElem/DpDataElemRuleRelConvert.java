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
