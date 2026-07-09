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

package tech.qiantong.qdata.module.da.convert.assetColumn;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.da.controller.admin.assetColumn.vo.DaAssetColumnPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetColumn.vo.DaAssetColumnRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetColumn.vo.DaAssetColumnSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetColumn.DaAssetColumnDO;

import java.util.List;

/**
 * Data Asset Column Convert
 *
 * @author lhs
 * @date 2025-01-21
 */
@Mapper
public interface DaAssetColumnConvert {
    DaAssetColumnConvert INSTANCE = Mappers.getMapper(DaAssetColumnConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param daAssetColumnPageReqVO request parameters
     * @return DaAssetColumnDO
     */
     DaAssetColumnDO convertToDO(DaAssetColumnPageReqVO daAssetColumnPageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param daAssetColumnSaveReqVO save request parameters
     * @return DaAssetColumnDO
     */
     DaAssetColumnDO convertToDO(DaAssetColumnSaveReqVO daAssetColumnSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param daAssetColumnDO entity object
     * @return DaAssetColumnRespVO
     */
     DaAssetColumnRespVO convertToRespVO(DaAssetColumnDO daAssetColumnDO);

    /**
     * Convert DOList to RespVOList
     * @param daAssetColumnDOList entity object list
     * @return List<DaAssetColumnRespVO>
     */
     List<DaAssetColumnRespVO> convertToRespVOList(List<DaAssetColumnDO> daAssetColumnDOList);
}
