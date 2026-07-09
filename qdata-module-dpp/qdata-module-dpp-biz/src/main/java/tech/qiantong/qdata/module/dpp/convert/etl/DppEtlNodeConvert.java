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
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodePageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlNodeDO;

import java.util.List;

/**
 * Data Integration Node Convert
 *
 * @author qdata
 * @date 2025-02-13
 */
@Mapper
public interface DppEtlNodeConvert {
    DppEtlNodeConvert INSTANCE = Mappers.getMapper(DppEtlNodeConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param dppEtlNodePageReqVO request parameters
     * @return DppEtlNodeDO
     */
     DppEtlNodeDO convertToDO(DppEtlNodePageReqVO dppEtlNodePageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param dppEtlNodeSaveReqVO save request parameters
     * @return DppEtlNodeDO
     */
     DppEtlNodeDO convertToDO(DppEtlNodeSaveReqVO dppEtlNodeSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param dppEtlNodeDO entity object
     * @return DppEtlNodeRespVO
     */
     DppEtlNodeRespVO convertToRespVO(DppEtlNodeDO dppEtlNodeDO);

    /**
     * Convert DO List to RespVO List
     * @param dppEtlNodeDOList entity object list
     * @return List<DppEtlNodeRespVO>
     */
     List<DppEtlNodeRespVO> convertToRespVOList(List<DppEtlNodeDO> dppEtlNodeDOList);
}
