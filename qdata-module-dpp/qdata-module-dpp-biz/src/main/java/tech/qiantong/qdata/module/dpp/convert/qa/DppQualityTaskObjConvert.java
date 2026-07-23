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
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskObjPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskObjRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskObjSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.qa.DppQualityTaskObjDO;

import java.util.List;

/**
 * Data Quality Task - Inspection Object Convert
 *
 * @author Chaos
 * @date 2025-07-21
 */
@Mapper
public interface DppQualityTaskObjConvert {
    DppQualityTaskObjConvert INSTANCE = Mappers.getMapper(DppQualityTaskObjConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param dppQualityTaskObjPageReqVO request parameters
     * @return DppQualityTaskObjDO
     */
     DppQualityTaskObjDO convertToDO(DppQualityTaskObjPageReqVO dppQualityTaskObjPageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param dppQualityTaskObjSaveReqVO save request parameters
     * @return DppQualityTaskObjDO
     */
     DppQualityTaskObjDO convertToDO(DppQualityTaskObjSaveReqVO dppQualityTaskObjSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param dppQualityTaskObjDO entity object
     * @return DppQualityTaskObjRespVO
     */
     DppQualityTaskObjRespVO convertToRespVO(DppQualityTaskObjDO dppQualityTaskObjDO);

    /**
     * Convert DO List to RespVO List
     * @param dppQualityTaskObjDOList entity object list
     * @return List<DppQualityTaskObjRespVO>
     */
     List<DppQualityTaskObjRespVO> convertToRespVOList(List<DppQualityTaskObjDO> dppQualityTaskObjDOList);
}
