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
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskLogPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskLogRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskLogSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskLogDO;

import java.util.List;

/**
 * Data Integration Task Log Convert
 *
 * @author qdata
 * @date 2025-02-13
 */
@Mapper
public interface DppEtlTaskLogConvert {
    DppEtlTaskLogConvert INSTANCE = Mappers.getMapper(DppEtlTaskLogConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param dppEtlTaskLogPageReqVO request parameters
     * @return DppEtlTaskLogDO
     */
     DppEtlTaskLogDO convertToDO(DppEtlTaskLogPageReqVO dppEtlTaskLogPageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param dppEtlTaskLogSaveReqVO save request parameters
     * @return DppEtlTaskLogDO
     */
     DppEtlTaskLogDO convertToDO(DppEtlTaskLogSaveReqVO dppEtlTaskLogSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param dppEtlTaskLogDO entity object
     * @return DppEtlTaskLogRespVO
     */
     DppEtlTaskLogRespVO convertToRespVO(DppEtlTaskLogDO dppEtlTaskLogDO);

    /**
     * Convert DO List to RespVO List
     * @param dppEtlTaskLogDOList entity object list
     * @return List<DppEtlTaskLogRespVO>
     */
     List<DppEtlTaskLogRespVO> convertToRespVOList(List<DppEtlTaskLogDO> dppEtlTaskLogDOList);
}
