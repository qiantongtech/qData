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
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDataElemCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDataElemCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDataElemCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttDataElemCatDO;

import java.util.List;

/**
 * Data Element Category Management Convert
 *
 * @author qdata
 * @date 2025-01-20
 */
@Mapper
public interface AttDataElemCatConvert {
    AttDataElemCatConvert INSTANCE = Mappers.getMapper(AttDataElemCatConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param attDataElemCatPageReqVO Request parameters
     * @return AttDataElemCatDO
     */
     AttDataElemCatDO convertToDO(AttDataElemCatPageReqVO attDataElemCatPageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param attDataElemCatSaveReqVO Save request parameters
     * @return AttDataElemCatDO
     */
     AttDataElemCatDO convertToDO(AttDataElemCatSaveReqVO attDataElemCatSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param attDataElemCatDO Entity object
     * @return AttDataElemCatRespVO
     */
     AttDataElemCatRespVO convertToRespVO(AttDataElemCatDO attDataElemCatDO);

    /**
     * Convert DOList to RespVOList
     * @param attDataElemCatDOList Entity object list
     * @return List<AttDataElemCatRespVO>
     */
     List<AttDataElemCatRespVO> convertToRespVOList(List<AttDataElemCatDO> attDataElemCatDOList);
}
