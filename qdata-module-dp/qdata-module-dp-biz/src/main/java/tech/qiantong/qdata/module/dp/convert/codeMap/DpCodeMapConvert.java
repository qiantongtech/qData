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

package tech.qiantong.qdata.module.dp.convert.codeMap;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.dp.controller.admin.codeMap.vo.DpCodeMapPageReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.codeMap.vo.DpCodeMapRespVO;
import tech.qiantong.qdata.module.dp.controller.admin.codeMap.vo.DpCodeMapSaveReqVO;
import tech.qiantong.qdata.module.dp.dal.dataobject.codeMap.DpCodeMapDO;

import java.util.List;

/**
 * Data Element Code Map Convert
 *
 * @author qdata
 * @date 2025-01-21
 */
@Mapper
public interface DpCodeMapConvert {
    DpCodeMapConvert INSTANCE = Mappers.getMapper(DpCodeMapConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param dpCodeMapPageReqVO Request params
     * @return DpCodeMapDO
     */
     DpCodeMapDO convertToDO(DpCodeMapPageReqVO dpCodeMapPageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param dpCodeMapSaveReqVO Save request params
     * @return DpCodeMapDO
     */
     DpCodeMapDO convertToDO(DpCodeMapSaveReqVO dpCodeMapSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param dpCodeMapDO Entity object
     * @return DpCodeMapRespVO
     */
     DpCodeMapRespVO convertToRespVO(DpCodeMapDO dpCodeMapDO);

    /**
     * Convert DOList to RespVOList
     * @param dpCodeMapDOList Entity object list
     * @return List<DpCodeMapRespVO>
     */
     List<DpCodeMapRespVO> convertToRespVOList(List<DpCodeMapDO> dpCodeMapDOList);
}
