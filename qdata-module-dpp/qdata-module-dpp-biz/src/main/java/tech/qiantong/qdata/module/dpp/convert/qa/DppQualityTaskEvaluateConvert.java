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

package tech.qiantong.qdata.module.dpp.convert.qa;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskEvaluatePageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskEvaluateRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskEvaluateSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.qa.DppQualityTaskEvaluateDO;

import java.util.List;

/**
 * Data Quality Task - Evaluation Rule Convert
 *
 * @author Chaos
 * @date 2025-07-21
 */
@Mapper
public interface DppQualityTaskEvaluateConvert {
    DppQualityTaskEvaluateConvert INSTANCE = Mappers.getMapper(DppQualityTaskEvaluateConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param dppQualityTaskEvaluatePageReqVO request parameters
     * @return DppQualityTaskEvaluateDO
     */
     DppQualityTaskEvaluateDO convertToDO(DppQualityTaskEvaluatePageReqVO dppQualityTaskEvaluatePageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param dppQualityTaskEvaluateSaveReqVO save request parameters
     * @return DppQualityTaskEvaluateDO
     */
     DppQualityTaskEvaluateDO convertToDO(DppQualityTaskEvaluateSaveReqVO dppQualityTaskEvaluateSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param dppQualityTaskEvaluateDO entity object
     * @return DppQualityTaskEvaluateRespVO
     */
     DppQualityTaskEvaluateRespVO convertToRespVO(DppQualityTaskEvaluateDO dppQualityTaskEvaluateDO);

    /**
     * Convert DO List to RespVO List
     * @param dppQualityTaskEvaluateDOList entity object list
     * @return List<DppQualityTaskEvaluateRespVO>
     */
     List<DppQualityTaskEvaluateRespVO> convertToRespVOList(List<DppQualityTaskEvaluateDO> dppQualityTaskEvaluateDOList);
}
