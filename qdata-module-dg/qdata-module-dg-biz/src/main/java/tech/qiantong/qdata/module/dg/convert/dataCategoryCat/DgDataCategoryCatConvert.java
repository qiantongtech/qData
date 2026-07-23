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

package tech.qiantong.qdata.module.dg.convert.dataCategoryCat;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.dg.controller.admin.dataCategoryCat.vo.DgDataCategoryCatPageReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.dataCategoryCat.vo.DgDataCategoryCatRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.dataCategoryCat.vo.DgDataCategoryCatSaveReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.dataCategoryCat.DgDataCategoryCatDO;

/**
 * Data Category Category Convert
 *
 * @author FXB
 * @date 2026-04-07
 */
@Mapper
public interface DgDataCategoryCatConvert {
    DgDataCategoryCatConvert INSTANCE = Mappers.getMapper(DgDataCategoryCatConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param dgDataCategoryCatPageReqVO request params
     * @return DgDataCategoryCatDO
     */
     DgDataCategoryCatDO convertToDO(DgDataCategoryCatPageReqVO dgDataCategoryCatPageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param dgDataCategoryCatSaveReqVO save request params
     * @return DgDataCategoryCatDO
     */
     DgDataCategoryCatDO convertToDO(DgDataCategoryCatSaveReqVO dgDataCategoryCatSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param dgDataCategoryCatDO entity object
     * @return DgDataCategoryCatRespVO
     */
     DgDataCategoryCatRespVO convertToRespVO(DgDataCategoryCatDO dgDataCategoryCatDO);

    /**
     * Convert DO List to RespVO List
     * @param dgDataCategoryCatDOList entity object list
     * @return List<DgDataCategoryCatRespVO>
     */
     List<DgDataCategoryCatRespVO> convertToRespVOList(List<DgDataCategoryCatDO> dgDataCategoryCatDOList);
}
