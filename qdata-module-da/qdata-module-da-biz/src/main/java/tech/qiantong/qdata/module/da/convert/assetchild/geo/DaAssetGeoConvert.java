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
 * 数据资产-矢量 Convert
 *
 * @author qdata
 * @date 2025-04-14
 */
@Mapper
public interface DaAssetGeoConvert {
    DaAssetGeoConvert INSTANCE = Mappers.getMapper(DaAssetGeoConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param daAssetGeoPageReqVO 请求参数
     * @return DaAssetGeoDO
     */
     DaAssetGeoDO convertToDO(DaAssetGeoPageReqVO daAssetGeoPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param daAssetGeoSaveReqVO 保存请求参数
     * @return DaAssetGeoDO
     */
     DaAssetGeoDO convertToDO(DaAssetGeoSaveReqVO daAssetGeoSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param daAssetGeoDO 实体对象
     * @return DaAssetGeoRespVO
     */
     DaAssetGeoRespVO convertToRespVO(DaAssetGeoDO daAssetGeoDO);

    /**
     * DOList 转换为 RespVOList
     * @param daAssetGeoDOList 实体对象列表
     * @return List<DaAssetGeoRespVO>
     */
     List<DaAssetGeoRespVO> convertToRespVOList(List<DaAssetGeoDO> daAssetGeoDOList);
}
