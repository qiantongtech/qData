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

package tech.qiantong.qdata.module.ds.convert.api;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.ds.controller.admin.api.vo.DsApiPageReqVO;
import tech.qiantong.qdata.module.ds.controller.admin.api.vo.DsApiRespVO;
import tech.qiantong.qdata.module.ds.controller.admin.api.vo.DsApiSaveReqVO;
import tech.qiantong.qdata.module.ds.dal.dataobject.api.DsApiDO;

import java.util.List;

/**
 * API service converter
 *
 * @author lhs
 * @date 2025-02-12
 */
@Mapper
public interface DsApiConvert {
    DsApiConvert INSTANCE = Mappers.getMapper(DsApiConvert.class);

    /**
     * Converts a PageReqVO to a DO.
     * @param dsApiPageReqVO request parameters
     * @return DsApiDO
     */
     DsApiDO convertToDO(DsApiPageReqVO dsApiPageReqVO);

    /**
     * Converts a SaveReqVO to a DO.
     * @param dsApiSaveReqVO save request parameters
     * @return DsApiDO
     */
     DsApiDO convertToDO(DsApiSaveReqVO dsApiSaveReqVO);

    /**
     * Converts a DO to a RespVO.
     * @param dsApiDO entity
     * @return DsApiRespVO
     */
     DsApiRespVO convertToRespVO(DsApiDO dsApiDO);

    /**
     * Converts a DO list to a RespVO list.
     * @param dsApiDOList entity list
     * @return List<DsApiRespVO>
     */
     List<DsApiRespVO> convertToRespVOList(List<DsApiDO> dsApiDOList);
}
