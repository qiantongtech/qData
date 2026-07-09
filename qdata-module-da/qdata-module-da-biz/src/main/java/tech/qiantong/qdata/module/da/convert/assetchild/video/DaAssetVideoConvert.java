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

package tech.qiantong.qdata.module.da.convert.assetchild.video;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.video.vo.DaAssetVideoPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.video.vo.DaAssetVideoRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.video.vo.DaAssetVideoSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.video.DaAssetVideoDO;

import java.util.List;

/**
 * Data Asset-Video Convert
 *
 * @author qdata
 * @date 2025-04-14
 */
@Mapper
public interface DaAssetVideoConvert {
    DaAssetVideoConvert INSTANCE = Mappers.getMapper(DaAssetVideoConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param daAssetVideoPageReqVO request parameters
     * @return DaAssetVideoDO
     */
     DaAssetVideoDO convertToDO(DaAssetVideoPageReqVO daAssetVideoPageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param daAssetVideoSaveReqVO save request parameters
     * @return DaAssetVideoDO
     */
     DaAssetVideoDO convertToDO(DaAssetVideoSaveReqVO daAssetVideoSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param daAssetVideoDO entity object
     * @return DaAssetVideoRespVO
     */
     DaAssetVideoRespVO convertToRespVO(DaAssetVideoDO daAssetVideoDO);

    /**
     * Convert DOList to RespVOList
     * @param daAssetVideoDOList entity object list
     * @return List<DaAssetVideoRespVO>
     */
     List<DaAssetVideoRespVO> convertToRespVOList(List<DaAssetVideoDO> daAssetVideoDOList);
}
