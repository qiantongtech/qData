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
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemAssetRelPageReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemAssetRelRespVO;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemAssetRelSaveReqVO;
import tech.qiantong.qdata.module.dp.dal.dataobject.dataElem.DpDataElemAssetRelDO;

import java.util.List;

/**
 * Data Element Asset Relationship Convert
 *
 * @author qdata
 * @date 2025-01-21
 */
@Mapper
public interface DpDataElemAssetRelConvert {
    DpDataElemAssetRelConvert INSTANCE = Mappers.getMapper(DpDataElemAssetRelConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param dpDataElemAssetRelPageReqVO Request params
     * @return DpDataElemAssetRelDO
     */
     DpDataElemAssetRelDO convertToDO(DpDataElemAssetRelPageReqVO dpDataElemAssetRelPageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param dpDataElemAssetRelSaveReqVO Save request params
     * @return DpDataElemAssetRelDO
     */
     DpDataElemAssetRelDO convertToDO(DpDataElemAssetRelSaveReqVO dpDataElemAssetRelSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param dpDataElemAssetRelDO Entity object
     * @return DpDataElemAssetRelRespVO
     */
     DpDataElemAssetRelRespVO convertToRespVO(DpDataElemAssetRelDO dpDataElemAssetRelDO);

    /**
     * Convert DOList to RespVOList
     * @param dpDataElemAssetRelDOList Entity object list
     * @return List<DpDataElemAssetRelRespVO>
     */
     List<DpDataElemAssetRelRespVO> convertToRespVOList(List<DpDataElemAssetRelDO> dpDataElemAssetRelDOList);
}
