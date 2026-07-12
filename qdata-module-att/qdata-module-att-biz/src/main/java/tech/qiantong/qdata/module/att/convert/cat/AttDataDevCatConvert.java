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
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDataDevCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDataDevCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDataDevCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttDataDevCatDO;

import java.util.List;

/**
 * Data Development Category Management Convert
 *
 * @author qdata
 * @date 2025-03-11
 */
@Mapper
public interface AttDataDevCatConvert {
    AttDataDevCatConvert INSTANCE = Mappers.getMapper(AttDataDevCatConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param attDataDevCatPageReqVO Request parameters
     * @return AttDataDevCatDO
     */
     AttDataDevCatDO convertToDO(AttDataDevCatPageReqVO attDataDevCatPageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param attDataDevCatSaveReqVO Save request parameters
     * @return AttDataDevCatDO
     */
     AttDataDevCatDO convertToDO(AttDataDevCatSaveReqVO attDataDevCatSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param attDataDevCatDO Entity object
     * @return AttDataDevCatRespVO
     */
     AttDataDevCatRespVO convertToRespVO(AttDataDevCatDO attDataDevCatDO);

    /**
     * Convert DOList to RespVOList
     * @param attDataDevCatDOList Entity object list
     * @return List<AttDataDevCatRespVO>
     */
     List<AttDataDevCatRespVO> convertToRespVOList(List<AttDataDevCatDO> attDataDevCatDOList);
}
