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

package tech.qiantong.qdata.module.da.convert.assetchild.geo;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.geo.vo.DaAssetGeoPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.geo.vo.DaAssetGeoRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.geo.vo.DaAssetGeoSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.geo.DaAssetGeoDO;

import java.util.List;

/**
 * Data Asset - Vector Convert
 *
 * @author qdata
 * @date 2025-04-14
 */
@Mapper
public interface DaAssetGeoConvert {
    DaAssetGeoConvert INSTANCE = Mappers.getMapper(DaAssetGeoConvert.class);

    /**
     * PageReqVO Convert to DO
     * @param daAssetGeoPageReqVO request parameters
     * @return DaAssetGeoDO
     */
     DaAssetGeoDO convertToDO(DaAssetGeoPageReqVO daAssetGeoPageReqVO);

    /**
     * SaveReqVO Convert to DO
     * @param daAssetGeoSaveReqVO save request parameters
     * @return DaAssetGeoDO
     */
     DaAssetGeoDO convertToDO(DaAssetGeoSaveReqVO daAssetGeoSaveReqVO);

    /**
     * DO Convert to RespVO
     * @param daAssetGeoDO entity object
     * @return DaAssetGeoRespVO
     */
     DaAssetGeoRespVO convertToRespVO(DaAssetGeoDO daAssetGeoDO);

    /**
     * DOList Convert to RespVOList
     * @param daAssetGeoDOList entity object list
     * @return List<DaAssetGeoRespVO>
     */
     List<DaAssetGeoRespVO> convertToRespVOList(List<DaAssetGeoDO> daAssetGeoDOList);
}
