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
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttModelCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttModelCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttModelCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttModelCatDO;

import java.util.List;

/**
 * Logical Model Category Management Convert
 *
 * @author qdata
 * @date 2025-01-20
 */
@Mapper
public interface AttModelCatConvert {
    AttModelCatConvert INSTANCE = Mappers.getMapper(AttModelCatConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param attModelCatPageReqVO Request parameters
     * @return AttModelCatDO
     */
     AttModelCatDO convertToDO(AttModelCatPageReqVO attModelCatPageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param attModelCatSaveReqVO Save request parameters
     * @return AttModelCatDO
     */
     AttModelCatDO convertToDO(AttModelCatSaveReqVO attModelCatSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param attModelCatDO Entity object
     * @return AttModelCatRespVO
     */
     AttModelCatRespVO convertToRespVO(AttModelCatDO attModelCatDO);

    /**
     * Convert DOList to RespVOList
     * @param attModelCatDOList Entity object list
     * @return List<AttModelCatRespVO>
     */
     List<AttModelCatRespVO> convertToRespVOList(List<AttModelCatDO> attModelCatDOList);
}
