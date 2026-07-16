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

package tech.qiantong.qdata.module.da.convert.assetchild.gis;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.gis.vo.DaAssetGisPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.gis.vo.DaAssetGisRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.gis.vo.DaAssetGisSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.gis.DaAssetGisDO;

import java.util.List;

/**
 * Data Asset - Geospatial Service Convert
 *
 * @author qdata
 * @date 2025-04-14
 */
@Mapper
public interface DaAssetGisConvert {
    DaAssetGisConvert INSTANCE = Mappers.getMapper(DaAssetGisConvert.class);

    /**
     * PageReqVO Convert to DO
     * @param daAssetGisPageReqVO request parameters
     * @return DaAssetGisDO
     */
     DaAssetGisDO convertToDO(DaAssetGisPageReqVO daAssetGisPageReqVO);

    /**
     * SaveReqVO Convert to DO
     * @param daAssetGisSaveReqVO save request parameters
     * @return DaAssetGisDO
     */
     DaAssetGisDO convertToDO(DaAssetGisSaveReqVO daAssetGisSaveReqVO);

    /**
     * DO Convert to RespVO
     * @param daAssetGisDO entity object
     * @return DaAssetGisRespVO
     */
     DaAssetGisRespVO convertToRespVO(DaAssetGisDO daAssetGisDO);

    /**
     * DOList Convert to RespVOList
     * @param daAssetGisDOList entity object list
     * @return List<DaAssetGisRespVO>
     */
     List<DaAssetGisRespVO> convertToRespVOList(List<DaAssetGisDO> daAssetGisDOList);
}
