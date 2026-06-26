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
 * 数据资产-视频数据 Convert
 *
 * @author qdata
 * @date 2025-04-14
 */
@Mapper
public interface DaAssetVideoConvert {
    DaAssetVideoConvert INSTANCE = Mappers.getMapper(DaAssetVideoConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param daAssetVideoPageReqVO 请求参数
     * @return DaAssetVideoDO
     */
     DaAssetVideoDO convertToDO(DaAssetVideoPageReqVO daAssetVideoPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param daAssetVideoSaveReqVO 保存请求参数
     * @return DaAssetVideoDO
     */
     DaAssetVideoDO convertToDO(DaAssetVideoSaveReqVO daAssetVideoSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param daAssetVideoDO 实体对象
     * @return DaAssetVideoRespVO
     */
     DaAssetVideoRespVO convertToRespVO(DaAssetVideoDO daAssetVideoDO);

    /**
     * DOList 转换为 RespVOList
     * @param daAssetVideoDOList 实体对象列表
     * @return List<DaAssetVideoRespVO>
     */
     List<DaAssetVideoRespVO> convertToRespVOList(List<DaAssetVideoDO> daAssetVideoDOList);
}
