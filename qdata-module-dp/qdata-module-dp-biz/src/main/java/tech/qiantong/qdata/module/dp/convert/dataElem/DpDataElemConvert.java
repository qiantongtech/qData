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

package tech.qiantong.qdata.module.dp.convert.dataElem;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemPageReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemRespVO;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemSaveReqVO;
import tech.qiantong.qdata.module.dp.dal.dataobject.dataElem.DpDataElemDO;

import java.util.List;

/**
 * Data Element Convert
 *
 * @author qdata
 * @date 2025-01-21
 */
@Mapper
public interface DpDataElemConvert {
    DpDataElemConvert INSTANCE = Mappers.getMapper(DpDataElemConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param dpDataElemPageReqVO Request params
     * @return DpDataElemDO
     */
     DpDataElemDO convertToDO(DpDataElemPageReqVO dpDataElemPageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param dpDataElemSaveReqVO Save request params
     * @return DpDataElemDO
     */
     DpDataElemDO convertToDO(DpDataElemSaveReqVO dpDataElemSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param dpDataElemDO Entity object
     * @return DpDataElemRespVO
     */
     DpDataElemRespVO convertToRespVO(DpDataElemDO dpDataElemDO);

    /**
     * Convert DOList to RespVOList
     * @param dpDataElemDOList Entity object list
     * @return List<DpDataElemRespVO>
     */
     List<DpDataElemRespVO> convertToRespVOList(List<DpDataElemDO> dpDataElemDOList);
}
