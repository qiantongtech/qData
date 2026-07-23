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

package tech.qiantong.qdata.module.att.convert.cat;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttAssetCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttAssetCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttAssetCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttAssetCatDO;

import java.util.List;

/**
 * Data Asset Category Management Convert
 *
 * @author qdata
 * @date 2025-01-20
 */
@Mapper
public interface AttAssetCatConvert {
    AttAssetCatConvert INSTANCE = Mappers.getMapper(AttAssetCatConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param attAssetCatPageReqVO Request parameters
     * @return AttAssetCatDO
     */
     AttAssetCatDO convertToDO(AttAssetCatPageReqVO attAssetCatPageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param attAssetCatSaveReqVO Save request parameters
     * @return AttAssetCatDO
     */
     AttAssetCatDO convertToDO(AttAssetCatSaveReqVO attAssetCatSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param attAssetCatDO Entity object
     * @return AttAssetCatRespVO
     */
     AttAssetCatRespVO convertToRespVO(AttAssetCatDO attAssetCatDO);

    /**
     * Convert DOList to RespVOList
     * @param attAssetCatDOList Entity object list
     * @return List<AttAssetCatRespVO>
     */
     List<AttAssetCatRespVO> convertToRespVOList(List<AttAssetCatDO> attAssetCatDOList);
}
