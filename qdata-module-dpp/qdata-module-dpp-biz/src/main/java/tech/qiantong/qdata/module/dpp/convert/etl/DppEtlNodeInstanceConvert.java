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
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeInstancePageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeInstanceRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeInstanceSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlNodeInstanceDO;

import java.util.List;

/**
 * Data Integration Node Instance Convert
 *
 * @author qdata
 * @date 2025-02-13
 */
@Mapper
public interface DppEtlNodeInstanceConvert {
    DppEtlNodeInstanceConvert INSTANCE = Mappers.getMapper(DppEtlNodeInstanceConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param dppEtlNodeInstancePageReqVO request parameters
     * @return DppEtlNodeInstanceDO
     */
     DppEtlNodeInstanceDO convertToDO(DppEtlNodeInstancePageReqVO dppEtlNodeInstancePageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param dppEtlNodeInstanceSaveReqVO save request parameters
     * @return DppEtlNodeInstanceDO
     */
     DppEtlNodeInstanceDO convertToDO(DppEtlNodeInstanceSaveReqVO dppEtlNodeInstanceSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param dppEtlNodeInstanceDO entity object
     * @return DppEtlNodeInstanceRespVO
     */
     DppEtlNodeInstanceRespVO convertToRespVO(DppEtlNodeInstanceDO dppEtlNodeInstanceDO);

    /**
     * Convert DO List to RespVO List
     * @param dppEtlNodeInstanceDOList entity object list
     * @return List<DppEtlNodeInstanceRespVO>
     */
     List<DppEtlNodeInstanceRespVO> convertToRespVOList(List<DppEtlNodeInstanceDO> dppEtlNodeInstanceDOList);
}
