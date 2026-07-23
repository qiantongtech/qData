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
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttCleanCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttCleanCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttCleanCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttCleanCatDO;

import java.util.List;

/**
 * Cleaning Rule Category Convert
 *
 * @author qdata
 * @date 2025-08-11
 */
@Mapper
public interface AttCleanCatConvert {
    AttCleanCatConvert INSTANCE = Mappers.getMapper(AttCleanCatConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param attCleanCatPageReqVO Request parameters
     * @return AttCleanCatDO
     */
     AttCleanCatDO convertToDO(AttCleanCatPageReqVO attCleanCatPageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param attCleanCatSaveReqVO Save request parameters
     * @return AttCleanCatDO
     */
     AttCleanCatDO convertToDO(AttCleanCatSaveReqVO attCleanCatSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param attCleanCatDO Entity object
     * @return AttCleanCatRespVO
     */
     AttCleanCatRespVO convertToRespVO(AttCleanCatDO attCleanCatDO);

    /**
     * Convert DOList to RespVOList
     * @param attCleanCatDOList Entity object list
     * @return List<AttCleanCatRespVO>
     */
     List<AttCleanCatRespVO> convertToRespVOList(List<AttCleanCatDO> attCleanCatDOList);
}
