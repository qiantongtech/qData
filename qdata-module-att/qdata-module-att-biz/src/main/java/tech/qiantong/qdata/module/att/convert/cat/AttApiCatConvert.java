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
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttApiCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttApiCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttApiCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttApiCatDO;

import java.util.List;

/**
 * Data Service Category Management Convert
 *
 * @author qdata
 * @date 2025-03-11
 */
@Mapper
public interface AttApiCatConvert {
    AttApiCatConvert INSTANCE = Mappers.getMapper(AttApiCatConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param attApiCatPageReqVO Request parameters
     * @return AttApiCatDO
     */
     AttApiCatDO convertToDO(AttApiCatPageReqVO attApiCatPageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param attApiCatSaveReqVO Save request parameters
     * @return AttApiCatDO
     */
     AttApiCatDO convertToDO(AttApiCatSaveReqVO attApiCatSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param attApiCatDO Entity object
     * @return AttApiCatRespVO
     */
     AttApiCatRespVO convertToRespVO(AttApiCatDO attApiCatDO);

    /**
     * Convert DOList to RespVOList
     * @param attApiCatDOList Entity object list
     * @return List<AttApiCatRespVO>
     */
     List<AttApiCatRespVO> convertToRespVOList(List<AttApiCatDO> attApiCatDOList);
}
