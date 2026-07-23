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
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDocumentCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDocumentCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDocumentCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttDocumentCatDO;

import java.util.List;

/**
 * Standard Information Category Management Convert
 *
 * @author qdata
 * @date 2025-08-21
 */
@Mapper
public interface AttDocumentCatConvert {
    AttDocumentCatConvert INSTANCE = Mappers.getMapper(AttDocumentCatConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param attDocumentCatPageReqVO Request parameters
     * @return AttDocumentCatDO
     */
     AttDocumentCatDO convertToDO(AttDocumentCatPageReqVO attDocumentCatPageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param attDocumentCatSaveReqVO Save request parameters
     * @return AttDocumentCatDO
     */
     AttDocumentCatDO convertToDO(AttDocumentCatSaveReqVO attDocumentCatSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param attDocumentCatDO Entity object
     * @return AttDocumentCatRespVO
     */
     AttDocumentCatRespVO convertToRespVO(AttDocumentCatDO attDocumentCatDO);

    /**
     * Convert DOList to RespVOList
     * @param attDocumentCatDOList Entity object list
     * @return List<AttDocumentCatRespVO>
     */
     List<AttDocumentCatRespVO> convertToRespVOList(List<AttDocumentCatDO> attDocumentCatDOList);
}
