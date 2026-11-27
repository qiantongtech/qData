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
 * Business License: https://docs.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qdata.module.mc.convert.domain;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.mc.controller.admin.domain.vo.McDomainPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.domain.vo.McDomainRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.domain.vo.McDomainSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.domain.McDomainDO;

import java.util.List;

/**
 * Business domain Convert
 *
 * @author qdata
 * @date 2026-02-12
 */
@Mapper
public interface McDomainConvert {
    McDomainConvert INSTANCE = Mappers.getMapper(McDomainConvert.class);

    /**
     * PageReqVO converted to DO
     * @param mcDomainPageReqVO request parameters
     * @return McDomainDO
     */
     McDomainDO convertToDO(McDomainPageReqVO mcDomainPageReqVO);

    /**
     * SaveReqVO converted to DO
     * @param mcDomainSaveReqVO Save request parameters
     * @return McDomainDO
     */
     McDomainDO convertToDO(McDomainSaveReqVO mcDomainSaveReqVO);

    /**
     * DO to RespVO
     * @param mcDomainDO entity object
     * @return McDomainRespVO
     */
     McDomainRespVO convertToRespVO(McDomainDO mcDomainDO);

    /**
     * DOList to RespVOList
     * @param mcDomainDOList entity object list
     * @return List<McDomainRespVO>
     */
     List<McDomainRespVO> convertToRespVOList(List<McDomainDO> mcDomainDOList);
}
