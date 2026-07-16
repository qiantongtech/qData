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
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.qa.DppQualityTaskDO;

import java.util.List;

/**
 * Data Quality Task Convert
 *
 * @author Chaos
 * @date 2025-07-21
 */
@Mapper
public interface DppQualityTaskConvert {
    DppQualityTaskConvert INSTANCE = Mappers.getMapper(DppQualityTaskConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param dppQualityTaskPageReqVO request parameters
     * @return DppQualityTaskDO
     */
     DppQualityTaskDO convertToDO(DppQualityTaskPageReqVO dppQualityTaskPageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param dppQualityTaskSaveReqVO save request parameters
     * @return DppQualityTaskDO
     */
     DppQualityTaskDO convertToDO(DppQualityTaskSaveReqVO dppQualityTaskSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param dppQualityTaskDO entity object
     * @return DppQualityTaskRespVO
     */
     DppQualityTaskRespVO convertToRespVO(DppQualityTaskDO dppQualityTaskDO);

    /**
     * Convert DO List to RespVO List
     * @param dppQualityTaskDOList entity object list
     * @return List<DppQualityTaskRespVO>
     */
     List<DppQualityTaskRespVO> convertToRespVOList(List<DppQualityTaskDO> dppQualityTaskDOList);
}
