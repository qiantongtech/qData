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
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttTagCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttTagCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttTagCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttTagCatDO;

import java.util.List;

/**
 * Tag Category Management Convert
 *
 * @author qdata
 * @date 2025-07-11
 */
@Mapper
public interface AttTagCatConvert {
    AttTagCatConvert INSTANCE = Mappers.getMapper(AttTagCatConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param attTagCatPageReqVO Request parameters
     * @return AttTagCatDO
     */
     AttTagCatDO convertToDO(AttTagCatPageReqVO attTagCatPageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param attTagCatSaveReqVO Save request parameters
     * @return AttTagCatDO
     */
     AttTagCatDO convertToDO(AttTagCatSaveReqVO attTagCatSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param attTagCatDO Entity object
     * @return AttTagCatRespVO
     */
     AttTagCatRespVO convertToRespVO(AttTagCatDO attTagCatDO);

    /**
     * Convert DOList to RespVOList
     * @param attTagCatDOList Entity object list
     * @return List<AttTagCatRespVO>
     */
     List<AttTagCatRespVO> convertToRespVOList(List<AttTagCatDO> attTagCatDOList);
}
