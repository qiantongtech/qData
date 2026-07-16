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
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlSchedulerPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlSchedulerRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlSchedulerSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlSchedulerDO;

import java.util.List;

/**
 * Data Integration Schedule Info Convert
 *
 * @author qdata
 * @date 2025-02-13
 */
@Mapper
public interface DppEtlSchedulerConvert {
    DppEtlSchedulerConvert INSTANCE = Mappers.getMapper(DppEtlSchedulerConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param dppEtlSchedulerPageReqVO request parameters
     * @return DppEtlSchedulerDO
     */
     DppEtlSchedulerDO convertToDO(DppEtlSchedulerPageReqVO dppEtlSchedulerPageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param dppEtlSchedulerSaveReqVO save request parameters
     * @return DppEtlSchedulerDO
     */
     DppEtlSchedulerDO convertToDO(DppEtlSchedulerSaveReqVO dppEtlSchedulerSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param dppEtlSchedulerDO entity object
     * @return DppEtlSchedulerRespVO
     */
     DppEtlSchedulerRespVO convertToRespVO(DppEtlSchedulerDO dppEtlSchedulerDO);

    /**
     * Convert DO List to RespVO List
     * @param dppEtlSchedulerDOList entity object list
     * @return List<DppEtlSchedulerRespVO>
     */
     List<DppEtlSchedulerRespVO> convertToRespVOList(List<DppEtlSchedulerDO> dppEtlSchedulerDOList);
}
