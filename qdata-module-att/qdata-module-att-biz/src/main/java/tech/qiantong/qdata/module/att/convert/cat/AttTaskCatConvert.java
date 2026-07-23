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
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttTaskCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttTaskCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttTaskCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttTaskCatDO;

import java.util.List;

/**
 * Data Integration Task Category Management Convert
 *
 * @author qdata
 * @date 2025-03-11
 */
@Mapper
public interface AttTaskCatConvert {
    AttTaskCatConvert INSTANCE = Mappers.getMapper(AttTaskCatConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param attTaskCatPageReqVO Request parameters
     * @return AttTaskCatDO
     */
     AttTaskCatDO convertToDO(AttTaskCatPageReqVO attTaskCatPageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param attTaskCatSaveReqVO Save request parameters
     * @return AttTaskCatDO
     */
     AttTaskCatDO convertToDO(AttTaskCatSaveReqVO attTaskCatSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param attTaskCatDO Entity object
     * @return AttTaskCatRespVO
     */
     AttTaskCatRespVO convertToRespVO(AttTaskCatDO attTaskCatDO);

    /**
     * Convert DOList to RespVOList
     * @param attTaskCatDOList Entity object list
     * @return List<AttTaskCatRespVO>
     */
     List<AttTaskCatRespVO> convertToRespVOList(List<AttTaskCatDO> attTaskCatDOList);
}
