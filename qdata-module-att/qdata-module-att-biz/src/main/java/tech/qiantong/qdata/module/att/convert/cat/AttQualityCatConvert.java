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
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttQualityCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttQualityCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttQualityCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttQualityCatDO;

import java.util.List;

/**
 * Data Quality Category Convert
 *
 * @author qdata
 * @date 2025-07-19
 */
@Mapper
public interface AttQualityCatConvert {
    AttQualityCatConvert INSTANCE = Mappers.getMapper(AttQualityCatConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param attQualityCatPageReqVO Request parameters
     * @return AttQualityCatDO
     */
     AttQualityCatDO convertToDO(AttQualityCatPageReqVO attQualityCatPageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param attQualityCatSaveReqVO Save request parameters
     * @return AttQualityCatDO
     */
     AttQualityCatDO convertToDO(AttQualityCatSaveReqVO attQualityCatSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param attQualityCatDO Entity object
     * @return AttQualityCatRespVO
     */
     AttQualityCatRespVO convertToRespVO(AttQualityCatDO attQualityCatDO);

    /**
     * Convert DOList to RespVOList
     * @param attQualityCatDOList Entity object list
     * @return List<AttQualityCatRespVO>
     */
     List<AttQualityCatRespVO> convertToRespVOList(List<AttQualityCatDO> attQualityCatDOList);
}
