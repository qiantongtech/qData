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

package tech.qiantong.qdata.module.da.convert.asset;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.da.controller.admin.asset.vo.DaAssetPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.asset.vo.DaAssetRespVO;
import tech.qiantong.qdata.module.da.controller.admin.asset.vo.DaAssetSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.asset.DaAssetDO;

import java.util.List;

/**
 * Data Asset Convert
 *
 * @author lhs
 * @date 2025-01-21
 */
@Mapper
public interface DaAssetConvert {
    DaAssetConvert INSTANCE = Mappers.getMapper(DaAssetConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param daAssetPageReqVO request parameters
     * @return DaAssetDO
     */
     DaAssetDO convertToDO(DaAssetPageReqVO daAssetPageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param daAssetSaveReqVO save request parameters
     * @return DaAssetDO
     */
     DaAssetDO convertToDO(DaAssetSaveReqVO daAssetSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param daAssetDO entity object
     * @return DaAssetRespVO
     */
     DaAssetRespVO convertToRespVO(DaAssetDO daAssetDO);

    /**
     * Convert DOList to RespVOList
     * @param daAssetDOList entity object list
     * @return List<DaAssetRespVO>
     */
     List<DaAssetRespVO> convertToRespVOList(List<DaAssetDO> daAssetDOList);
}
