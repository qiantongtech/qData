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
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppQualityLogPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppQualityLogRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppQualityLogSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppQualityLogDO;

import java.util.List;

/**
 * Data Quality Log Convert
 *
 * @author qdata
 * @date 2025-07-19
 */
@Mapper
public interface DppQualityLogConvert {
    DppQualityLogConvert INSTANCE = Mappers.getMapper(DppQualityLogConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param dppQualityLogPageReqVO request parameters
     * @return DppQualityLogDO
     */
     DppQualityLogDO convertToDO(DppQualityLogPageReqVO dppQualityLogPageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param dppQualityLogSaveReqVO save request parameters
     * @return DppQualityLogDO
     */
     DppQualityLogDO convertToDO(DppQualityLogSaveReqVO dppQualityLogSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param dppQualityLogDO entity object
     * @return DppQualityLogRespVO
     */
     DppQualityLogRespVO convertToRespVO(DppQualityLogDO dppQualityLogDO);

    /**
     * Convert DO List to RespVO List
     * @param dppQualityLogDOList entity object list
     * @return List<DppQualityLogRespVO>
     */
     List<DppQualityLogRespVO> convertToRespVOList(List<DppQualityLogDO> dppQualityLogDOList);
}
