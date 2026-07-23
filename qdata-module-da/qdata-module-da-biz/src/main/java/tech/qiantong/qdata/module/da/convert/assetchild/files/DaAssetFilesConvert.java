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

package tech.qiantong.qdata.module.da.convert.assetchild.files;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.files.vo.DaAssetFilesPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.files.vo.DaAssetFilesRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.files.vo.DaAssetFilesSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.files.DaAssetFilesDO;

import java.util.List;

/**
 * Data Asset - File Service Convert
 *
 * @author qdata
 * @date 2025-06-26
 */
@Mapper
public interface DaAssetFilesConvert {
    DaAssetFilesConvert INSTANCE = Mappers.getMapper(DaAssetFilesConvert.class);

    /**
     * PageReqVO Convert to DO
     * @param daAssetFilesPageReqVO request parameters
     * @return DaAssetFilesDO
     */
     DaAssetFilesDO convertToDO(DaAssetFilesPageReqVO daAssetFilesPageReqVO);

    /**
     * SaveReqVO Convert to DO
     * @param daAssetFilesSaveReqVO save request parameters
     * @return DaAssetFilesDO
     */
     DaAssetFilesDO convertToDO(DaAssetFilesSaveReqVO daAssetFilesSaveReqVO);

    /**
     * DO Convert to RespVO
     * @param daAssetFilesDO entity object
     * @return DaAssetFilesRespVO
     */
     DaAssetFilesRespVO convertToRespVO(DaAssetFilesDO daAssetFilesDO);

    /**
     * DOList Convert to RespVOList
     * @param daAssetFilesDOList entity object list
     * @return List<DaAssetFilesRespVO>
     */
     List<DaAssetFilesRespVO> convertToRespVOList(List<DaAssetFilesDO> daAssetFilesDOList);
}
