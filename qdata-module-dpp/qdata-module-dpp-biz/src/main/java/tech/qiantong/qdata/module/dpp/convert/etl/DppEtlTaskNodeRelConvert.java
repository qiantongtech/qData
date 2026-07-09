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
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskNodeRelPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskNodeRelRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskNodeRelSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskNodeRelDO;

import java.util.List;

/**
 * Data Integration Task Node Relation Convert
 *
 * @author qdata
 * @date 2025-02-13
 */
@Mapper
public interface DppEtlTaskNodeRelConvert {
    DppEtlTaskNodeRelConvert INSTANCE = Mappers.getMapper(DppEtlTaskNodeRelConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param dppEtlTaskNodeRelPageReqVO request parameters
     * @return DppEtlTaskNodeRelDO
     */
     DppEtlTaskNodeRelDO convertToDO(DppEtlTaskNodeRelPageReqVO dppEtlTaskNodeRelPageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param dppEtlTaskNodeRelSaveReqVO save request parameters
     * @return DppEtlTaskNodeRelDO
     */
     DppEtlTaskNodeRelDO convertToDO(DppEtlTaskNodeRelSaveReqVO dppEtlTaskNodeRelSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param dppEtlTaskNodeRelDO entity object
     * @return DppEtlTaskNodeRelRespVO
     */
     DppEtlTaskNodeRelRespVO convertToRespVO(DppEtlTaskNodeRelDO dppEtlTaskNodeRelDO);

    /**
     * Convert DO List to RespVO List
     * @param dppEtlTaskNodeRelDOList entity object list
     * @return List<DppEtlTaskNodeRelRespVO>
     */
     List<DppEtlTaskNodeRelRespVO> convertToRespVOList(List<DppEtlTaskNodeRelDO> dppEtlTaskNodeRelDOList);
}
