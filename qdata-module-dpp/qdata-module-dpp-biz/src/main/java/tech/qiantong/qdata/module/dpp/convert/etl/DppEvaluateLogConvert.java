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

package tech.qiantong.qdata.module.dpp.convert.etl;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEvaluateLogPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEvaluateLogRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEvaluateLogSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEvaluateLogDO;

import java.util.List;

/**
 * Evaluation Rule Result Convert
 *
 * @author qdata
 * @date 2025-07-21
 */
@Mapper
public interface DppEvaluateLogConvert {
    DppEvaluateLogConvert INSTANCE = Mappers.getMapper(DppEvaluateLogConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param dppEvaluateLogPageReqVO request parameters
     * @return DppEvaluateLogDO
     */
     DppEvaluateLogDO convertToDO(DppEvaluateLogPageReqVO dppEvaluateLogPageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param dppEvaluateLogSaveReqVO save request parameters
     * @return DppEvaluateLogDO
     */
     DppEvaluateLogDO convertToDO(DppEvaluateLogSaveReqVO dppEvaluateLogSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param dppEvaluateLogDO entity object
     * @return DppEvaluateLogRespVO
     */
     DppEvaluateLogRespVO convertToRespVO(DppEvaluateLogDO dppEvaluateLogDO);

    /**
     * Convert DO List to RespVO List
     * @param dppEvaluateLogDOList entity object list
     * @return List<DppEvaluateLogRespVO>
     */
     List<DppEvaluateLogRespVO> convertToRespVOList(List<DppEvaluateLogDO> dppEvaluateLogDOList);
}
